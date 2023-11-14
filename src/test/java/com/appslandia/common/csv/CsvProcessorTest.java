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
import java.io.StringReader;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CsvProcessorTest {

  @Test
  public void test_null() {
    CsvProcessor csv = new CsvProcessor();

    String csvContent = null;
    String escaped = csv.escape(csvContent);
    Assertions.assertEquals("", escaped);
  }

  @Test
  public void test_writeNull() {
    CsvProcessor csv = new CsvProcessor().setWriteNull(true);

    String csvContent = null;
    String escaped = csv.escape(csvContent);
    Assertions.assertEquals("null", escaped);
  }

  @Test
  public void test_empty() {
    CsvProcessor csv = new CsvProcessor();

    String csvContent = "";
    String escaped = csv.escape(csvContent);
    Assertions.assertEquals("", escaped);
  }

  @Test
  public void test_blank() {
    CsvProcessor csv = new CsvProcessor();

    String csvContent = " ";
    String escaped = csv.escape(csvContent);
    Assertions.assertEquals(" ", escaped);
  }

  @Test
  public void test_wrapped() {
    CsvProcessor csv = new CsvProcessor();

    String csvContent = "abc";
    String escaped = csv.escape(csvContent);

    Assertions.assertEquals("abc", escaped);
  }

  @Test
  public void test_comma() {
    CsvProcessor csv = new CsvProcessor();

    String csvContent = "abc,def";
    String escaped = csv.escape(csvContent);

    Assertions.assertEquals("\"abc,def\"", escaped);
  }

  @Test
  public void test_quotes() {
    CsvProcessor csv = new CsvProcessor();

    String csvContent = "abc\"def";
    String escaped = csv.escape(csvContent);

    Assertions.assertEquals("\"abc\"\"def\"", escaped);
  }

  @Test
  public void test_crLf() {
    CsvProcessor csv = new CsvProcessor();

    String csvContent = "abc\r\ndef";
    String escaped = csv.escape(csvContent);

    Assertions.assertEquals("\"abc\r\ndef\"", escaped);
  }

  @Test
  public void test_escCrLf() {
    CsvProcessor csv = new CsvProcessor().setEscCrLf(true);

    String csvContent = "abc\r\ndef";
    String escaped = csv.escape(csvContent);

    Assertions.assertEquals("\"abc\\r\\ndef\"", escaped);
  }

  @Test
  public void test_separator() {
    CsvProcessor csv = new CsvProcessor().setSeparator(';');

    String csvContent = "abc;def";
    String escaped = csv.escape(csvContent);

    Assertions.assertEquals("\"abc;def\"", escaped);
  }

  @Test
  public void test_parse() {
    CsvProcessor csv = new CsvProcessor();
    String csvContent = "abc\r\n";

    try {
      List<CsvRecord> records = csv.parseRecords(new BufferedReader(new StringReader(csvContent)));
      Assertions.assertTrue(records.size() == 1);

      CsvRecord rec = records.get(0);
      Assertions.assertTrue(rec.length() == 1);
      Assertions.assertEquals("abc", rec.getString(0));

    } catch (Exception ex) {
      Assertions.fail(ex);
    }
  }

  @Test
  public void test_parse_empty() {
    CsvProcessor csv = new CsvProcessor();
    String csvContent = "\r\n";

    try {
      List<CsvRecord> records = csv.parseRecords(new BufferedReader(new StringReader(csvContent)));
      Assertions.assertTrue(records.size() == 1);

      CsvRecord rec = records.get(0);
      Assertions.assertTrue(rec.length() == 1);
      Assertions.assertNull(rec.getString(0));

    } catch (Exception ex) {
      Assertions.fail(ex);
    }
  }

  @Test
  public void test_parse_blank() {
    CsvProcessor csv = new CsvProcessor();
    String csvContent = " \r\n";

    try {
      List<CsvRecord> records = csv.parseRecords(new BufferedReader(new StringReader(csvContent)));
      Assertions.assertTrue(records.size() == 1);

      CsvRecord rec = records.get(0);
      Assertions.assertTrue(rec.length() == 1);
      Assertions.assertNull(rec.getString(0));

    } catch (Exception ex) {
      Assertions.fail(ex);
    }
  }

  @Test
  public void test_parse_null() {
    CsvProcessor csv = new CsvProcessor();
    String csvContent = "null\r\n";

    try {
      List<CsvRecord> records = csv.parseRecords(new BufferedReader(new StringReader(csvContent)));
      Assertions.assertTrue(records.size() == 1);

      CsvRecord rec = records.get(0);
      Assertions.assertTrue(rec.length() == 1);
      Assertions.assertEquals("null", rec.getString(0));

    } catch (Exception ex) {
      Assertions.fail(ex);
    }
  }

  @Test
  public void test_parse_writeNull() {
    CsvProcessor csv = new CsvProcessor().setWriteNull(true);
    String csvContent = "null\r\n";

    try {
      List<CsvRecord> records = csv.parseRecords(new BufferedReader(new StringReader(csvContent)));
      Assertions.assertTrue(records.size() == 1);

      CsvRecord rec = records.get(0);
      Assertions.assertTrue(rec.length() == 1);
      Assertions.assertNull(rec.getString(0));

    } catch (Exception ex) {
      Assertions.fail(ex);
    }
  }

  @Test
  public void test_parse_crLf() {
    CsvProcessor csv = new CsvProcessor().setEscCrLf(true);

    String csvContent = "abc\\r\\ndef\r\n";
    try {
      List<CsvRecord> records = csv.parseRecords(new BufferedReader(new StringReader(csvContent)));
      Assertions.assertTrue(records.size() == 1);

      CsvRecord rec = records.get(0);
      Assertions.assertTrue(rec.length() == 1);
      Assertions.assertEquals("abc\r\ndef", rec.getString(0));

    } catch (Exception ex) {
      Assertions.fail(ex);
    }
  }
}
