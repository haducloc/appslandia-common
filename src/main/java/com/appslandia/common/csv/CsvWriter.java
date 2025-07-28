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

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;

import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.IOUtils;
import com.appslandia.common.utils.ModelUtils;
import com.appslandia.common.utils.ReflectionUtils;
import com.appslandia.common.utils.STR;

/**
 *
 * @author Loc Ha
 *
 */
public class CsvWriter implements AutoCloseable {

  final BufferedWriter out;
  final CsvProcessor csvProcessor;
  final boolean internalOut;

  public CsvWriter(BufferedWriter out) {
    this(out, CsvProcessor.INSTANCE);
  }

  public CsvWriter(BufferedWriter out, CsvProcessor csvProcessor) {
    this.out = out;
    this.internalOut = false;
    this.csvProcessor = csvProcessor;
  }

  public CsvWriter(String csvLocation, String encoding) throws IOException {
    this(csvLocation, encoding, CsvProcessor.INSTANCE);
  }

  public CsvWriter(String csvLocation, String encoding, CsvProcessor csvProcessor) throws IOException {
    this.out = IOUtils.writerBOM(csvLocation, encoding);
    this.internalOut = true;
    this.csvProcessor = csvProcessor;
  }

  public CsvWriter newLine() throws IOException {
    this.out.newLine();
    return this;
  }

  @Override
  public void close() throws Exception {
    if (this.internalOut) {
      this.out.close();
    }
  }

  public CsvWriter outSeparator() throws IOException {
    this.out.write(this.csvProcessor.getSeparator());
    return this;
  }

  public CsvWriter out(Object value) throws IOException {
    return out(value, false);
  }

  public CsvWriter out(Object value, boolean firstSeparator) throws IOException {
    if (firstSeparator) {
      outSeparator();
    }

    var escapedValue = this.csvProcessor.escape(value);
    this.out.write(escapedValue);

    return this;
  }

  public CsvWriter outValues(Object... values) throws IOException {
    for (var i = 0; i < values.length; i++) {
      out(values[i], false);

      if (i < values.length - 1) {
        outSeparator();
      }
    }
    return this;
  }

  public CsvWriter out(CsvRecord record) throws IOException {
    for (var i = 0; i < record.length(); i++) {
      out(record.getString(i), false);

      if (i < record.length() - 1) {
        outSeparator();
      }
    }
    return this;
  }

  public <T extends Record> CsvWriter outRecord(T record, String... fieldNames) throws IOException {
    Arguments.notNull(record);

    // recordFields
    var recordFields = record.getClass().getRecordComponents();

    if (fieldNames.length == 0) {
      fieldNames = Arrays.stream(recordFields).map(f -> f.getName()).toArray(String[]::new);
    }

    for (var i = 0; i < fieldNames.length; i++) {
      var fieldName = fieldNames[i];

      // recordField
      var recordField = Arrays.stream(recordFields).filter(f -> fieldName.equals(f.getName())).findFirst().orElse(null);

      if (recordField == null) {
        throw new IllegalArgumentException(STR.fmt("The field '{}' is not found.", fieldName));
      }

      // value
      var value = ReflectionUtils.invoke(recordField.getAccessor(), record);
      out(value, false);

      if (i < fieldNames.length - 1) {
        outSeparator();
      }
    }

    return this;
  }

  public CsvWriter outModel(Object model, String... fieldNames) throws IOException {
    Arguments.notNull(model);

    var beanInfo = ModelUtils.getBeanInfo(model.getClass());
    var propDescs = beanInfo.getPropertyDescriptors();

    for (var i = 0; i < fieldNames.length; i++) {
      var fieldName = fieldNames[i];

      // propDesc
      var propDesc = Arrays.stream(propDescs).filter(p -> fieldName.equals(p.getName())).findFirst().orElse(null);

      if (propDesc == null) {
        throw new IllegalArgumentException(STR.fmt("The property '{}' is not found.", fieldName));
      }
      Asserts.notNull(propDesc.getReadMethod());

      // value
      var value = ReflectionUtils.invoke(propDesc.getReadMethod(), model);
      out(value, false);

      if (i < fieldNames.length - 1) {
        outSeparator();
      }
    }
    return this;
  }
}
