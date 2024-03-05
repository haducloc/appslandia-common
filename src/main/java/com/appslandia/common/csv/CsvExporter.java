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

import java.io.BufferedWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.appslandia.common.base.CaseInsensitiveMap;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.base.Out;
import com.appslandia.common.data.RecordContext;
import com.appslandia.common.jdbc.ConnectionImpl;
import com.appslandia.common.jdbc.ResultSetColumn;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.DateUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CsvExporter extends InitializeObject {

  private BufferedWriter csvOutput;
  private ConnectionImpl connection;
  private String pQuery;
  private Map<String, Object> pQueryParams;

  private CsvProcessor csvProcessor;

  private String datePattern;
  private String timePattern;
  private String dateTimePattern;

  private String offsetTimePattern;
  private String offsetDateTimePattern;

  final Map<String, DbToCsvConverter> converters = new CaseInsensitiveMap<>();

  @Override
  protected void init() throws Exception {
    Asserts.notNull(this.pQuery);
    Asserts.notNull(this.csvOutput);

    if (this.connection == null) {
      this.connection = ConnectionImpl.getCurrent();
    }
    if (this.csvProcessor == null) {
      this.csvProcessor = CsvProcessor.INSTANCE;
    }

    // Default patterns
    if (this.datePattern == null) {
      this.datePattern = CsvUtils.getCsvDtPattern(DateUtils.ISO8601_DATE);
    }

    if (this.timePattern == null) {
      this.timePattern = CsvUtils.getCsvDtPattern(DateUtils.ISO8601_TIME_S);
    }

    if (this.dateTimePattern == null) {
      this.dateTimePattern = CsvUtils.getCsvDtPattern(DateUtils.ISO8601_DATETIME_S);
    }

    if (this.offsetTimePattern == null) {
      this.offsetTimePattern = CsvUtils.getCsvDtPattern(DateUtils.ISO8601_TIMEZ_S);
    }

    if (this.offsetDateTimePattern == null) {
      this.offsetDateTimePattern = CsvUtils.getCsvDtPattern(DateUtils.ISO8601_DATETIMEZ_S);
    }
  }

  public int execute() throws Exception {
    initialize();

    AtomicInteger counter = new AtomicInteger(0);
    try (RecordContext ctx = new RecordContext(this.connection)) {

      Out<Boolean> writeHeader = new Out<>();
      ctx.executeQuery(this.pQuery, this.pQueryParams, rs -> {

        // CSV Header
        if (writeHeader.value == null) {

          for (ResultSetColumn column : rs.getColumns()) {
            if (column.getIndex() > 1) {
              this.csvOutput.write(csvProcessor.getSeparator());
            }
            this.csvOutput.write(this.csvProcessor.escape(column.getName()));
          }
          this.csvOutput.newLine();

          writeHeader.value = true;
        }

        // CSV Record
        for (ResultSetColumn column : rs.getColumns()) {
          Object value = rs.getObject(column.getIndex());

          DbToCsvConverter converter = this.converters.get(column.getName());
          if (converter != null) {
            value = converter.apply(value);
          }

          if (column.getIndex() > 1) {
            this.csvOutput.write(csvProcessor.getSeparator());
          }

          if (value == null) {
            this.csvOutput.write(this.csvProcessor.escape(null));

          } else {
            Class<?> type = value.getClass();

            if (Number.class.isAssignableFrom(type) || type == Boolean.class) {
              this.csvOutput.write(value.toString());

            } else if (type == LocalDate.class) {
              this.csvOutput.write(this.csvProcessor.escape(DateUtils.format((LocalDate) value, this.datePattern)));

            } else if (type == LocalTime.class) {
              this.csvOutput.write(this.csvProcessor.escape(DateUtils.format((LocalTime) value, this.timePattern)));

            } else if (type == LocalDateTime.class) {
              this.csvOutput
                  .write(this.csvProcessor.escape(DateUtils.format((LocalDateTime) value, this.dateTimePattern)));

            } else if (type == OffsetTime.class) {
              this.csvOutput
                  .write(this.csvProcessor.escape(DateUtils.format((OffsetTime) value, this.offsetTimePattern)));

            } else if (type == OffsetDateTime.class) {
              this.csvOutput.write(
                  this.csvProcessor.escape(DateUtils.format((OffsetDateTime) value, this.offsetDateTimePattern)));

            } else {
              // Other
              this.csvOutput.write(this.csvProcessor.escape(value.toString()));
            }
          }
        } // record

        this.csvOutput.newLine();
      });

      this.csvOutput.flush();
    }
    return counter.get();
  }

  public CsvExporter setCsvOutput(BufferedWriter csvOutput) {
    assertNotInitialized();
    this.csvOutput = csvOutput;
    return this;
  }

  public CsvExporter setConnection(ConnectionImpl connection) {
    assertNotInitialized();
    this.connection = connection;
    return this;
  }

  public CsvExporter setPQuery(String pQuery) {
    assertNotInitialized();
    this.pQuery = pQuery;
    return this;
  }

  public CsvExporter setPQueryParams(Map<String, Object> pQueryParams) {
    assertNotInitialized();
    this.pQueryParams = pQueryParams;
    return this;
  }

  public CsvExporter setCsvProcessor(CsvProcessor csvProcessor) {
    assertNotInitialized();
    this.csvProcessor = csvProcessor;
    return this;
  }

  public CsvExporter setDatePattern(String datePattern) {
    assertNotInitialized();
    this.datePattern = datePattern;
    return this;
  }

  public CsvExporter setTimePattern(String timePattern) {
    assertNotInitialized();
    this.timePattern = timePattern;
    return this;
  }

  public CsvExporter setDateTimePattern(String dateTimePattern) {
    assertNotInitialized();
    this.dateTimePattern = dateTimePattern;
    return this;
  }

  public CsvExporter setOffsetTimePattern(String offsetTimePattern) {
    assertNotInitialized();
    this.offsetTimePattern = offsetTimePattern;
    return this;
  }

  public CsvExporter setOffsetDateTimePattern(String offsetDateTimePattern) {
    assertNotInitialized();
    this.offsetDateTimePattern = offsetDateTimePattern;
    return this;
  }

  public CsvExporter setDbToCsvConverter(String columnLabel, DbToCsvConverter converter) {
    assertNotInitialized();
    Asserts.notNull(converter);

    this.converters.put(columnLabel, converter);
    return this;
  }
}
