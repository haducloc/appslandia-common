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
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.appslandia.common.base.CaseInsensitiveMap;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.base.Out;
import com.appslandia.common.data.RecordContext;
import com.appslandia.common.jdbc.ConnectionImpl;
import com.appslandia.common.jdbc.ResultSetColumn;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.IOUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CsvExporter extends InitializeObject {

  private BufferedWriter csvOutput;
  private boolean closeStream;

  private ConnectionImpl connection;
  private String pQuery;
  private Map<String, Object> pQueryParams;

  private CsvProcessor csvProcessor;

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
              this.csvOutput.write(this.csvProcessor.getSeparator());
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
            this.csvOutput.write(this.csvProcessor.getSeparator());
          }
          this.csvOutput.write(this.csvProcessor.escape(value));

        }
        this.csvOutput.newLine();
      });

      this.csvOutput.flush();
    } finally {
      if (this.closeStream) {
        this.csvOutput.close();
      }
    }
    return counter.get();
  }

  public CsvExporter setCsvOutput(String csvLocation, String encoding) throws IOException {
    return setCsvOutput(IOUtils.writerBOM(csvLocation, encoding), true);
  }

  public CsvExporter setCsvOutput(BufferedWriter csvOutput) {
    return setCsvOutput(csvOutput, false);
  }

  public CsvExporter setCsvOutput(BufferedWriter csvOutput, boolean closeStream) {
    assertNotInitialized();
    this.csvOutput = csvOutput;
    this.closeStream = closeStream;
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

  public CsvExporter setDbToCsvConverter(String columnLabel, DbToCsvConverter converter) {
    assertNotInitialized();
    Asserts.notNull(converter);

    this.converters.put(columnLabel, converter);
    return this;
  }
}
