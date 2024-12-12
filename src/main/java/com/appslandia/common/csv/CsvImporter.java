// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

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
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLXML;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import com.appslandia.common.base.DangerTaskConfirm;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.data.Column;
import com.appslandia.common.data.DataRecord;
import com.appslandia.common.data.RecordContext;
import com.appslandia.common.data.Table;
import com.appslandia.common.jdbc.ConnectionImpl;
import com.appslandia.common.jdbc.JdbcParam;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.IOUtils;
import com.appslandia.common.utils.STR;
import com.appslandia.common.utils.TypeUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CsvImporter extends InitializeObject {

  private BufferedReader csvInput;
  private boolean closeStream;

  private boolean csvHeader;
  private ConnectionImpl connection;
  private String tableName;
  private CsvProcessor csvProcessor;

  private DangerTaskConfirm taskConfirm;
  private CsvDebugger csvDebugger;
  final Map<Integer, String> mappedColumns = new TreeMap<>();

  private Collection<String> datePatterns;
  private Collection<String> timePatterns;
  private Collection<String> dateTimePatterns;

  private Collection<String> offsetTimePatterns;
  private Collection<String> offsetDateTimePatterns;

  final Map<Integer, CsvToDbConverter> converters = new TreeMap<>();

  @Override
  protected void init() throws Exception {
    Asserts.notNull(this.tableName);
    Asserts.notNull(this.csvInput);

    if (this.connection == null) {
      this.connection = ConnectionImpl.getCurrent();
    }
    if (this.csvProcessor == null) {
      this.csvProcessor = CsvProcessor.INSTANCE;
    }

    // Default patterns
    if (this.datePatterns == null) {
      this.datePatterns = CsvUtils.PATTERNS_DATE;
    }

    if (this.timePatterns == null) {
      this.timePatterns = CsvUtils.PATTERNS_TIME;
    }

    if (this.dateTimePatterns == null) {
      this.dateTimePatterns = CsvUtils.PATTERNS_DATETIME;
    }

    if (this.offsetTimePatterns == null) {
      this.offsetTimePatterns = CsvUtils.PATTERNS_TIMEZ;
    }

    if (this.offsetDateTimePatterns == null) {
      this.offsetDateTimePatterns = CsvUtils.PATTERNS_DATETIMEZ;
    }
  }

  protected boolean isTaskConfirmed() {
    return this.taskConfirm == DangerTaskConfirm.DANGER_TASK_CONFIRMED;
  }

  public int execute() throws Exception {
    initialize();

    try (RecordContext ctx = new RecordContext(this.connection)) {
      Table table = ctx.getTable(this.tableName);

      try {
        // Transactional
        if (isTaskConfirmed()) {
          ctx.setTransactional(true);
        }

        final AtomicInteger counter = new AtomicInteger(0);

        this.csvProcessor.parse(this.csvInput, (idx, csvRecord) -> {
          Asserts.isTrue(table.getColumns().size() == csvRecord.length(), "Unmatched column count.");

          if (this.csvHeader) {
            if (idx == 0) {
              return;
            }
          }

          // Build record
          DataRecord dataRecord = new DataRecord();
          for (int csvIdx = 0; csvIdx < csvRecord.length(); csvIdx++) {

            Column col = getColumn(table, csvIdx);
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

          int inserts = counter.incrementAndGet();

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

        // Commit all batches
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

      } finally {
        if (this.closeStream) {
          this.csvInput.close();
        }
      }
    }
  }

  protected Column getColumn(Table table, int csvIndex) {
    String mappedCol = this.mappedColumns.get(csvIndex);

    if (mappedCol == null) {
      return table.getColumns().get(csvIndex);
    }
    return table.getColumns().stream().filter(c -> c.getName().equalsIgnoreCase(mappedCol)).findFirst()
        .orElseThrow(() -> new NoSuchElementException("No such column named " + mappedCol));
  }

  protected Object toColumnValue(CsvRecord csv, int csvIdx, Column column, ConnectionImpl conn) throws Exception {
    String value = csv.getString(csvIdx);

    // dbConverter
    CsvToDbConverter dbConverter = this.converters.get(csvIdx);
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

    // Temporals
    if (type == LocalDate.class) {
      return csv.getLocalDate(csvIdx, this.datePatterns);
    }
    if (type == LocalTime.class) {
      return csv.getLocalTime(csvIdx, this.timePatterns);
    }
    if (type == LocalDateTime.class) {
      return csv.getLocalDateTime(csvIdx, this.dateTimePatterns);
    }
    if (type == OffsetTime.class) {
      return csv.getOffsetTime(csvIdx, this.offsetTimePatterns);
    }
    if (type == OffsetDateTime.class) {
      return csv.getOffsetDateTime(csvIdx, this.offsetDateTimePatterns);
    }

    // SQLXML
    if (type == SQLXML.class) {
      SQLXML xml = conn.createSQLXML();
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

  public CsvImporter setCsvInput(String csvLocation, String altEncoding) throws IOException {
    return setCsvInput(IOUtils.readerBOM(csvLocation, altEncoding), true);
  }

  public CsvImporter setCsvInput(BufferedReader csvInput) {
    return setCsvInput(csvInput, false);
  }

  public CsvImporter setCsvInput(BufferedReader csvInput, boolean closeStream) {
    assertNotInitialized();
    this.csvInput = csvInput;
    this.closeStream = closeStream;
    return this;
  }

  public CsvImporter setCsvHeader(boolean csvHeader) {
    assertNotInitialized();
    this.csvHeader = csvHeader;
    return this;
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

  public CsvImporter setDatePatterns(Collection<String> datePatterns) {
    assertNotInitialized();
    this.datePatterns = datePatterns;
    return this;
  }

  public CsvImporter setTimePatterns(Collection<String> timePatterns) {
    assertNotInitialized();
    this.timePatterns = timePatterns;
    return this;
  }

  public CsvImporter setDateTimePatterns(Collection<String> dateTimePatterns) {
    assertNotInitialized();
    this.dateTimePatterns = dateTimePatterns;
    return this;
  }

  public CsvImporter setOffsetTimePatterns(Collection<String> offsetTimePatterns) {
    assertNotInitialized();
    this.offsetTimePatterns = offsetTimePatterns;
    return this;
  }

  public CsvImporter setOffsetDateTimePatterns(Collection<String> offsetDateTimePatterns) {
    assertNotInitialized();
    this.offsetDateTimePatterns = offsetDateTimePatterns;
    return this;
  }

  public CsvImporter setCsvToDbConverter(int csvIndex, CsvToDbConverter converter) {
    assertNotInitialized();
    Asserts.notNull(converter);

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
