// The MIT License (MIT)
// Copyright © 2015 Loc Ha

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.appslandia.common.csv;

import java.io.BufferedReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLXML;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import com.appslandia.common.base.DangerTaskConfirm;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.base.TemporalPatterns;
import com.appslandia.common.data.Column;
import com.appslandia.common.data.DataRecord;
import com.appslandia.common.data.RecordContext;
import com.appslandia.common.data.Table;
import com.appslandia.common.jdbc.ConnectionImpl;
import com.appslandia.common.jdbc.JdbcParam;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.IOUtils;
import com.appslandia.common.utils.STR;
import com.appslandia.common.utils.TypeUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class CsvImporter extends InitializeObject {

  private ConnectionImpl connection;
  private String tableName;
  private CsvProcessor csvProcessor;

  private DangerTaskConfirm taskConfirm;
  private CsvDebugger csvDebugger;
  final Map<Integer, String> mappedColumns = new TreeMap<>();

  private TemporalPatterns temporalPatterns;
  final Map<Integer, CsvToDbConverter> converters = new TreeMap<>();

  @Override
  protected void init() throws Exception {
    Arguments.notNull(this.tableName);

    if (this.connection == null) {
      this.connection = ConnectionImpl.getCurrent();
    }
    if (this.csvProcessor == null) {
      this.csvProcessor = CsvProcessor.INSTANCE;
    }
    if (this.temporalPatterns == null) {
      this.temporalPatterns = TemporalPatterns.DEFAULT.initialize();
    }
  }

  protected boolean isTaskConfirmed() {
    return this.taskConfirm == DangerTaskConfirm.DANGER_TASK_CONFIRMED;
  }

  public int execute(String csvFileLocation, boolean csvHeader) throws Exception {
    this.initialize();
    try (var in = IOUtils.readerBOM(csvFileLocation, StandardCharsets.UTF_8.name())) {
      return execute(in, csvHeader);
    }
  }

  public int execute(BufferedReader csvInput, boolean csvHeader) throws Exception {
    this.initialize();

    try (var ctx = new RecordContext(this.connection)) {
      var table = ctx.getTable(this.tableName);

      try {
        // Transactional
        if (isTaskConfirmed()) {
          ctx.setTransactional(true);
        }

        final var counter = new AtomicInteger(0);

        this.csvProcessor.parse(csvInput, (idx, csvRecord) -> {
          Asserts.isTrue(table.getColumns().size() == csvRecord.length(), "Column count mismatch.");

          if (csvHeader) {
            if (idx == 0) {
              return;
            }
          }

          // Build record
          var dataRecord = new DataRecord();
          for (var csvIdx = 0; csvIdx < csvRecord.length(); csvIdx++) {

            var col = getColumn(table, csvIdx);
            dataRecord.set(col.getName(), toColumnValue(csvRecord, csvIdx, col, ctx.getConnection()));
          }

          // csvDebugger
          if (this.csvDebugger != null) {
            this.csvDebugger.apply(counter.get(), csvRecord, dataRecord);
          }

          // Insert the record (batch)
          if (isTaskConfirmed()) {
            ctx.insert(table.getName(), dataRecord, true);
          }

          var inserts = counter.incrementAndGet();

          // executeBatch markers
          if (inserts > 0 && inserts % 100 == 0) {

            if (isTaskConfirmed()) {
              ctx.executeBatch();
            }
          }
        });

        // last executeBatch
        if (isTaskConfirmed()) {
          ctx.executeBatch();
        }

        // Commit
        if (isTaskConfirmed()) {
          ctx.commit();
        }
        return counter.get();

      } catch (Exception ex) {

        // Rollback
        if (isTaskConfirmed()) {
          ctx.rollback();
        }
        throw ex;
      }
    }
  }

  protected Column getColumn(Table table, int csvIndex) {
    var mappedCol = this.mappedColumns.get(csvIndex);

    if (mappedCol == null) {
      return table.getColumns().get(csvIndex);
    }
    return table.getColumns().stream().filter(c -> c.getName().equalsIgnoreCase(mappedCol)).findFirst()
        .orElseThrow(() -> new NoSuchElementException("No such column named " + mappedCol));
  }

  protected Object toColumnValue(CsvRecord csv, int csvIdx, Column column, ConnectionImpl conn) throws Exception {
    var value = csv.getString(csvIdx);

    // dbConverter
    var dbConverter = this.converters.get(csvIdx);
    if (dbConverter != null) {
      return dbConverter.apply(value, conn);
    }

    // Null
    if (value == null) {
      return new JdbcParam(null, column.getSqlType());
    }
    Class<?> type = TypeUtils.wrap(column.getJavaType());

    // String
    if (type == String.class || type == Reader.class) {
      return value;
    }
    // Boolean
    if (type == Boolean.class) {
      return csv.getBool(csvIdx);
    }

    // Numeric Types
    if (type == Byte.class) {
      return csv.getByte(csvIdx);
    }
    if (type == Short.class) {
      return csv.getShort(csvIdx);
    }
    if (type == Integer.class) {
      return csv.getInt(csvIdx);
    }
    if (type == Long.class) {
      return csv.getLong(csvIdx);
    }
    if (type == Float.class) {
      return csv.getFloat(csvIdx);
    }
    if (type == Double.class) {
      return csv.getDouble(csvIdx);
    }
    if (type == BigDecimal.class) {
      return csv.getDecimalReq(csvIdx);
    }

    // Temporal Types
    if (type == LocalDate.class) {
      return csv.getLocalDate(csvIdx, this.temporalPatterns);
    }
    if (type == LocalTime.class) {
      return csv.getLocalTime(csvIdx, this.temporalPatterns);
    }
    if (type == LocalDateTime.class) {
      return csv.getLocalDateTime(csvIdx, this.temporalPatterns);
    }
    if (type == OffsetTime.class) {
      return csv.getOffsetTime(csvIdx, this.temporalPatterns);
    }
    if (type == OffsetDateTime.class) {
      return csv.getOffsetDateTime(csvIdx, this.temporalPatterns);
    }

    // SQLXML
    if (type == SQLXML.class) {
      var xml = conn.createSQLXML();
      xml.setString(value);

      return xml;
    }

    // URL
    if (type == URL.class) {
      try {
        return new URI(value).toURL();
      } catch (URISyntaxException | MalformedURLException ex) {
      }
    }
    throw new IllegalArgumentException(STR.fmt("Failed to convert value for the column {}.", column.toString()));
  }

  public CsvImporter setConnection(ConnectionImpl connection) {
    assertNotInitialized();
    this.connection = connection;
    return this;
  }

  public CsvImporter setTableName(String tableName) {
    assertNotInitialized();
    this.tableName = tableName;
    return this;
  }

  public CsvImporter setCsvProcessor(CsvProcessor csvProcessor) {
    assertNotInitialized();
    this.csvProcessor = csvProcessor;
    return this;
  }

  public CsvImporter setTaskConfirm(DangerTaskConfirm taskConfirm) {
    assertNotInitialized();
    this.taskConfirm = taskConfirm;
    return this;
  }

  public CsvImporter setCsvDebugger(CsvDebugger csvDebugger) {
    assertNotInitialized();
    this.csvDebugger = csvDebugger;
    return this;
  }

  public CsvImporter setTemporalPatterns(TemporalPatterns temporalPatterns) {
    assertNotInitialized();
    this.temporalPatterns = temporalPatterns;
    return this;
  }

  public CsvImporter setCsvToDbConverter(int csvIndex, CsvToDbConverter converter) {
    assertNotInitialized();
    Arguments.notNull(converter);

    this.converters.put(csvIndex, converter);
    return this;
  }

  public CsvImporter setTableColumns(String... columnLabels) {
    assertNotInitialized();
    IntStream.range(0, columnLabels.length).forEach(idx -> this.mappedColumns.put(idx, columnLabels[idx]));
    return this;
  }

  public CsvImporter setMappedColumn(int csvIndex, String columnLabel) {
    assertNotInitialized();
    this.mappedColumns.put(csvIndex, columnLabel);
    return this;
  }
}
