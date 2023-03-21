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

package com.appslandia.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CsvEscaperTest {

    @Test
    public void test_null() {
	CsvEscaper escaper = new CsvEscaper();

	String value = null;
	String escaped = escaper.escape(value);
	Assertions.assertEquals("", escaped);
    }

    @Test
    public void test_writeNull() {
	CsvEscaper escaper = new CsvEscaper().writeNull();

	String value = null;
	String escaped = escaper.escape(value);
	Assertions.assertEquals("null", escaped);
    }

    @Test
    public void test_empty() {
	CsvEscaper escaper = new CsvEscaper();

	String value = "";
	String escaped = escaper.escape(value);
	Assertions.assertEquals("", escaped);
    }

    @Test
    public void test_blank() {
	CsvEscaper escaper = new CsvEscaper();

	String value = " ";
	String escaped = escaper.escape(value);
	Assertions.assertEquals(" ", escaped);
    }

    @Test
    public void test_wrapped() {
	CsvEscaper escaper = new CsvEscaper();

	String value = "abc";
	String escaped = escaper.escape(value);

	Assertions.assertEquals("abc", escaped);
    }

    @Test
    public void test_comma() {
	CsvEscaper escaper = new CsvEscaper();

	String value = "abc,def";
	String escaped = escaper.escape(value);

	Assertions.assertEquals("\"abc,def\"", escaped);
    }

    @Test
    public void test_quotes() {
	CsvEscaper escaper = new CsvEscaper();

	String value = "abc\"def";
	String escaped = escaper.escape(value);

	Assertions.assertEquals("\"abc\"\"def\"", escaped);
    }

    @Test
    public void test_crLf() {
	CsvEscaper escaper = new CsvEscaper();

	String value = "abc\r\ndef";
	String escaped = escaper.escape(value);

	Assertions.assertEquals("\"abc\r\ndef\"", escaped);
    }

    @Test
    public void test_escCrLf() {
	CsvEscaper escaper = new CsvEscaper().escCrLf();

	String value = "abc\r\ndef";
	String escaped = escaper.escape(value);

	Assertions.assertEquals("\"abc\\r\\ndef\"", escaped);
    }

    @Test
    public void test_separator() {
	CsvEscaper escaper = new CsvEscaper().separator(';');

	String value = "abc;def";
	String escaped = escaper.escape(value);

	Assertions.assertEquals("\"abc;def\"", escaped);
    }

    @Test
    public void test_unescape() {
	CsvEscaper escaper = new CsvEscaper();

	String value = "abc";
	try {
	    String unescaped = escaper.parse(new BufferedReader(new StringReader(value))).get(0)[0];
	    Assertions.assertEquals("abc", unescaped);

	} catch (IOException ex) {
	    Assertions.fail(ex);
	}
    }

    @Test
    public void test_unescape_empty() {
	CsvEscaper escaper = new CsvEscaper();

	String value = "";
	try {
	    String unescaped = escaper.parse(new BufferedReader(new StringReader(value))).get(0)[0];
	    Assertions.assertNull(unescaped);

	} catch (IOException ex) {
	    Assertions.fail(ex);
	}
    }

    @Test
    public void test_unescape_blank() {
	CsvEscaper escaper = new CsvEscaper();

	String value = " ";
	try {
	    String unescaped = escaper.parse(new BufferedReader(new StringReader(value))).get(0)[0];

	    Assertions.assertEquals(" ", unescaped);
	} catch (IOException ex) {
	    Assertions.fail(ex);
	}
    }

    @Test
    public void test_unescape_null() {
	CsvEscaper escaper = new CsvEscaper();
	String value = "null";

	try {
	    String unescaped = escaper.parse(new BufferedReader(new StringReader(value))).get(0)[0];

	    Assertions.assertEquals("null", unescaped);
	} catch (IOException ex) {
	    Assertions.fail(ex);
	}
    }

    @Test
    public void test_unescape_writeNull() {
	CsvEscaper escaper = new CsvEscaper().writeNull();

	String value = "null";
	try {
	    String unescaped = escaper.parse(new BufferedReader(new StringReader(value))).get(0)[0];

	    Assertions.assertNull(unescaped);
	} catch (IOException ex) {
	    Assertions.fail(ex);
	}
    }

    @Test
    public void test_unescape_crLf() {
	CsvEscaper escaper = new CsvEscaper().escCrLf();

	String value = "abc\\r\\ndef";
	try {
	    String unescaped = escaper.parse(new BufferedReader(new StringReader(value))).get(0)[0];

	    Assertions.assertEquals("abc\r\ndef", unescaped);
	} catch (IOException ex) {
	    Assertions.fail(ex);
	}
    }
}
