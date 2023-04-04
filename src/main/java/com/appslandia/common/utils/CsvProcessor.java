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
import java.util.ArrayList;
import java.util.List;

import com.appslandia.common.base.InitializeObject;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CsvProcessor extends InitializeObject {

    private boolean writeNull;
    private char separator = ',';
    private boolean escCrLf;

    @Override
    protected void init() throws Exception {
	// Validate separator?
    }

    public CsvProcessor writeNull() {
	assertNotInitialized();

	this.writeNull = true;
	return this;
    }

    public CsvProcessor separator(char separator) {
	assertNotInitialized();

	this.separator = separator;
	return this;
    }

    public CsvProcessor escCrLf() {
	assertNotInitialized();

	this.escCrLf = true;
	return this;
    }

    public String escape(String value) {
	return escape(value, (value != null) ? new StringBuilder((int) (value.length() * 1.25f)) : new StringBuilder());
    }

    public String escape(String value, StringBuilder buf) {
	this.initialize();
	buf.setLength(0);

	if (value == null) {
	    if (this.writeNull) {
		return "null";
	    }
	    return "";
	}
	if (value.isEmpty()) {
	    return "";
	}
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
		buf.append("\\").append(c == '\r' ? 'r' : 'n');
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

    public List<String[]> parse(BufferedReader reader) throws IOException {
	this.initialize();
	List<String[]> records = new ArrayList<>();

	String line;
	StringBuilder currentRecord = new StringBuilder();
	Integer recordLen = 0;

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
		    recordLen = values.length;
		}

		records.add(values);
		currentRecord.setLength(0);
	    }
	}
	if (records.isEmpty()) {
	    records.add(new String[] { null });
	}
	return records;
    }

    private String[] splitRecord(String record, Integer recordLen) {
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
	return values.toArray(new String[0]);
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
	    return value.toString();
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
	return value.toString();
    }
}