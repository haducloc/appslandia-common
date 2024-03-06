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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.List;

import com.appslandia.common.base.InitializeException;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.StringUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CsvProcessor extends InitializeObject {

  public static final CsvProcessor INSTANCE = new CsvProcessor().initialize();

  private boolean writeNull;
  private char separator = ',';
  private boolean escCrLf;

  private String datePattern;
  private String timePattern;
  private String dateTimePattern;

  private String offsetTimePattern;
  private String offsetDateTimePattern;

  @Override
  protected void init() throws Exception {
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

  @Override
  public CsvProcessor initialize() throws InitializeException {
    super.initialize();
    return this;
  }

  protected DateFormat getDateFormat(String pattern) {
    return new SimpleDateFormat(pattern);
  }

  public String escape(Object value) {
    this.initialize();

    if (value == null) {
      if (this.writeNull) {
        return StringUtils.NULL_STRING;
      }
      return StringUtils.EMPTY_STRING;
    }

    if (value instanceof CharSequence) {
      return doEscape(value.toString());
    }
    Class<?> type = value.getClass();

    if (Number.class.isAssignableFrom(type) || type == Boolean.class || Enum.class.isAssignableFrom(type)) {
      return value.toString();
    }

    if (type == LocalDate.class) {
      return doEscape(DateUtils.format((LocalDate) value, this.datePattern));
    }

    if (type == LocalTime.class) {
      return doEscape(DateUtils.format((LocalTime) value, this.timePattern));
    }

    if (type == LocalDateTime.class) {
      return doEscape(DateUtils.format((LocalDateTime) value, this.dateTimePattern));
    }

    if (type == OffsetTime.class) {
      return doEscape(DateUtils.format((OffsetTime) value, this.offsetTimePattern));
    }

    if (type == OffsetDateTime.class) {
      return doEscape(DateUtils.format((OffsetDateTime) value, this.offsetDateTimePattern));
    }

    if (type == java.sql.Date.class) {
      return doEscape(getDateFormat(this.datePattern).format((java.sql.Date) value));
    }

    if (type == java.sql.Time.class) {
      return doEscape(getDateFormat(this.timePattern).format((java.sql.Time) value));
    }

    if (type == java.sql.Timestamp.class) {
      return doEscape(getDateFormat(this.dateTimePattern).format((java.sql.Timestamp) value));
    }

    // Other
    return doEscape(value.toString());
  }

  protected String doEscape(String value) {
    if (value.isEmpty()) {
      return StringUtils.EMPTY_STRING;
    }

    StringBuilder buf = new StringBuilder((int) (value.length() * 1.25f));
    buf.append('"');

    int start = 0;
    char[] srcChars = value.toCharArray();
    int length = value.length();

    boolean useWrap = false;

    for (int i = 0; i < length; i++) {
      char c = srcChars[i];

      if (!useWrap) {
        useWrap = c == '"' || c == '\r' || c == '\n' || c == this.separator;
      }
      if (c == '"') {
        // add un_escaped portion
        if (start < i) {
          buf.append(srcChars, start, i - start);
        }

        // add escaped
        buf.append("\"\"");
        start = i + 1;

      } else if (this.escCrLf && (c == '\r' || c == '\n')) {

        // add un_escaped portion
        if (start < i) {
          buf.append(srcChars, start, i - start);
        }

        // add escaped
        buf.append('\\').append(c == '\r' ? 'r' : 'n');
        start = i + 1;
      }
    }

    // add rest of un_escaped portion
    if (start < length) {
      buf.append(srcChars, start, length - start);
    }
    buf.append('"');
    return useWrap ? buf.toString() : value;
  }

  public List<CsvRecord> parseRecords(BufferedReader reader) throws Exception {
    this.initialize();
    List<CsvRecord> records = new ArrayList<>();

    parse(reader, (idx, csvRecord) -> records.add(csvRecord));
    return records;
  }

  public void parse(BufferedReader reader, CsvConsumer consumer) throws Exception {
    this.initialize();

    String line;
    StringBuilder currentRecord = new StringBuilder();
    Integer recordLen = null;
    int recordIdx = 0;

    while ((line = reader.readLine()) != null) {

      currentRecord.append(line);
      currentRecord.append('\n');

      int numQuotes = (int) currentRecord.chars().filter(ch -> ch == '"').count();

      // Found record?
      if (numQuotes % 2 == 0) {

        // Delete the last add \n
        currentRecord.deleteCharAt(currentRecord.length() - 1);

        String[] values = splitRecord(currentRecord.toString(), recordLen);
        if (recordLen == null) {
          recordLen = (values.length > 0) ? values.length : 1;
        }

        consumer.apply(recordIdx++, new CsvRecord(values));
        currentRecord.setLength(0);
      }
    }
  }

  private String[] splitRecord(String record, Integer recordLen) {
    if (record.isEmpty()) {
      return new String[] { null };
    }
    List<String> values = (recordLen == null) ? new ArrayList<>() : new ArrayList<>(recordLen);
    StringBuilder currentField = new StringBuilder();
    boolean inQuotes = false;

    for (int i = 0; i < record.length(); i++) {
      char c = record.charAt(i);

      if (c == '"') {
        // Handle quotes within CSV values

        if (i < record.length() - 1 && record.charAt(i + 1) == '"') {

          // Add the first quote to the current field
          currentField.append(c);

          // Skip the second quote
          i++;

        } else {
          inQuotes = !inQuotes;
        }

      } else if (c == this.separator && !inQuotes) {
        // Handle commas within CSV values

        values.add(unescape(currentField));
        currentField.setLength(0);
      } else {
        currentField.append(c);
      }
    }

    values.add(unescape(currentField));
    return values.toArray(new String[values.size()]);
  }

  protected String unescape(StringBuilder value) {
    // ,,
    if (value.length() == 0) {
      return null;
    }

    // ,null,
    if (value.length() == 4 && "null".equals(value.toString())) {
      return this.writeNull ? null : "null";
    }

    // ,value,
    if (!this.escCrLf) {
      return StringUtils.trimToNull(value.toString());
    }

    // \\r \\n
    for (int i = 0; i < value.length(); i++) {
      char c = value.charAt(i);

      if (c != '\\') {
        continue;
      }

      // c = '\\'
      if (i + 1 < value.length()) {
        char nc = value.charAt(i + 1);

        if (nc == 'r' || nc == 'n') {
          value.replace(i, i + 2, nc == 'r' ? "\r" : "\n");
          i -= 1;
        }
      }
    }
    return StringUtils.trimToNull(value.toString());
  }

  public boolean isWriteNull() {
    initialize();
    return this.writeNull;
  }

  public CsvProcessor setWriteNull(boolean writeNull) {
    assertNotInitialized();
    this.writeNull = writeNull;
    return this;
  }

  public char getSeparator() {
    initialize();
    return this.separator;
  }

  public CsvProcessor setSeparator(char separator) {
    assertNotInitialized();
    this.separator = separator;
    return this;
  }

  public boolean isEscCrLf() {
    initialize();
    return this.escCrLf;
  }

  public CsvProcessor setEscCrLf(boolean escCrLf) {
    assertNotInitialized();
    this.escCrLf = escCrLf;
    return this;
  }

  public String getDatePattern() {
    initialize();
    return this.datePattern;
  }

  public CsvProcessor setDatePattern(String datePattern) {
    assertNotInitialized();
    this.datePattern = datePattern;
    return this;
  }

  public String getTimePattern() {
    initialize();
    return this.timePattern;
  }

  public CsvProcessor setTimePattern(String timePattern) {
    assertNotInitialized();
    this.timePattern = timePattern;
    return this;
  }

  public String getDateTimePattern() {
    initialize();
    return this.dateTimePattern;
  }

  public CsvProcessor setDateTimePattern(String dateTimePattern) {
    assertNotInitialized();
    this.dateTimePattern = dateTimePattern;
    return this;
  }

  public String getOffsetTimePattern() {
    initialize();
    return this.offsetTimePattern;
  }

  public CsvProcessor setOffsetTimePattern(String offsetTimePattern) {
    assertNotInitialized();
    this.offsetTimePattern = offsetTimePattern;
    return this;
  }

  public String getOffsetDateTimePattern() {
    initialize();
    return this.offsetDateTimePattern;
  }

  public CsvProcessor setOffsetDateTimePattern(String offsetDateTimePattern) {
    assertNotInitialized();
    this.offsetDateTimePattern = offsetDateTimePattern;
    return this;
  }
}
