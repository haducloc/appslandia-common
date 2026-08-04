// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.csv;

import java.io.BufferedReader;
import java.io.StringReader;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class CsvProcessorTest {

  @Test
  public void test_null() {
    var csv = new CsvProcessor();

    String csvContent = null;
    var escaped = csv.escape(csvContent);
    Assertions.assertEquals("", escaped);
  }

  @Test
  public void test_writeNull() {
    var csv = new CsvProcessor().setWriteNull(true);

    String csvContent = null;
    var escaped = csv.escape(csvContent);
    Assertions.assertEquals("null", escaped);
  }

  @Test
  public void test_empty() {
    var csv = new CsvProcessor();

    var csvContent = "";
    var escaped = csv.escape(csvContent);
    Assertions.assertEquals("", escaped);
  }

  @Test
  public void test_blank() {
    var csv = new CsvProcessor();

    var csvContent = " ";
    var escaped = csv.escape(csvContent);
    Assertions.assertEquals(" ", escaped);
  }

  @Test
  public void test_wrapped() {
    var csv = new CsvProcessor();

    var csvContent = "abc";
    var escaped = csv.escape(csvContent);

    Assertions.assertEquals("abc", escaped);
  }

  @Test
  public void test_comma() {
    var csv = new CsvProcessor();

    var csvContent = "abc,def";
    var escaped = csv.escape(csvContent);

    Assertions.assertEquals("\"abc,def\"", escaped);
  }

  @Test
  public void test_quotes() {
    var csv = new CsvProcessor();

    var csvContent = "abc\"def";
    var escaped = csv.escape(csvContent);

    Assertions.assertEquals("\"abc\"\"def\"", escaped);
  }

  @Test
  public void test_crLf() {
    var csv = new CsvProcessor();

    var csvContent = "abc\r\ndef";
    var escaped = csv.escape(csvContent);

    Assertions.assertEquals("\"abc\r\ndef\"", escaped);
  }

  @Test
  public void test_escCrLf() {
    var csv = new CsvProcessor().setEscCrLf(true);

    var csvContent = "abc\r\ndef";
    var escaped = csv.escape(csvContent);

    Assertions.assertEquals("\"abc\\r\\ndef\"", escaped);
  }

  @Test
  public void test_separator() {
    var csv = new CsvProcessor().setSeparator(';');

    var csvContent = "abc;def";
    var escaped = csv.escape(csvContent);

    Assertions.assertEquals("\"abc;def\"", escaped);
  }

  @Test
  public void test_parse() {
    var csv = new CsvProcessor();
    var csvContent = "abc\r\n";

    try {
      var records = csv.parseRecords(new BufferedReader(new StringReader(csvContent)), false, null);
      Assertions.assertTrue(records.size() == 1);

      var rec = records.get(0);
      Assertions.assertTrue(rec.length() == 1);
      Assertions.assertEquals("abc", rec.getString(0));

    } catch (Exception ex) {
      Assertions.fail(ex);
    }
  }

  @Test
  public void test_parse_empty() {
    var csv = new CsvProcessor();
    var csvContent = "\r\n";

    try {
      var records = csv.parseRecords(new BufferedReader(new StringReader(csvContent)), false, null);
      Assertions.assertTrue(records.size() == 1);

      var rec = records.get(0);
      Assertions.assertTrue(rec.length() == 1);
      Assertions.assertNull(rec.getString(0));

    } catch (Exception ex) {
      Assertions.fail(ex);
    }
  }

  @Test
  public void test_parse_blank() {
    var csv = new CsvProcessor();
    var csvContent = " \r\n";

    try {
      var records = csv.parseRecords(new BufferedReader(new StringReader(csvContent)), false, null);
      Assertions.assertTrue(records.size() == 1);

      var rec = records.get(0);
      Assertions.assertTrue(rec.length() == 1);
      Assertions.assertNull(rec.getString(0));

    } catch (Exception ex) {
      Assertions.fail(ex);
    }
  }

  @Test
  public void test_parse_null() {
    var csv = new CsvProcessor();
    var csvContent = "null\r\n";

    try {
      var records = csv.parseRecords(new BufferedReader(new StringReader(csvContent)), false, null);
      Assertions.assertTrue(records.size() == 1);

      var rec = records.get(0);
      Assertions.assertTrue(rec.length() == 1);
      Assertions.assertEquals("null", rec.getString(0));

    } catch (Exception ex) {
      Assertions.fail(ex);
    }
  }

  @Test
  public void test_parse_writeNull() {
    var csv = new CsvProcessor().setWriteNull(true);
    var csvContent = "null\r\n";

    try {
      var records = csv.parseRecords(new BufferedReader(new StringReader(csvContent)), false, null);
      Assertions.assertTrue(records.size() == 1);

      var rec = records.get(0);
      Assertions.assertTrue(rec.length() == 1);
      Assertions.assertNull(rec.getString(0));

    } catch (Exception ex) {
      Assertions.fail(ex);
    }
  }

  @Test
  public void test_parse_crLf() {
    var csv = new CsvProcessor().setEscCrLf(true);

    var csvContent = "abc\\r\\ndef\r\n";
    try {
      var records = csv.parseRecords(new BufferedReader(new StringReader(csvContent)), false, null);
      Assertions.assertTrue(records.size() == 1);

      var rec = records.get(0);
      Assertions.assertTrue(rec.length() == 1);
      Assertions.assertEquals("abc\r\ndef", rec.getString(0));

    } catch (Exception ex) {
      Assertions.fail(ex);
    }
  }
}
