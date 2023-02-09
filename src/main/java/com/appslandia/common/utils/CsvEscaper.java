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

import com.appslandia.common.base.InitializeObject;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CsvEscaper extends InitializeObject {

    private boolean writeNull;
    private char separator = ',';
    private boolean escCrLf;

    @Override
    protected void init() throws Exception {
	// Validate separator?
    }

    public CsvEscaper writeNull() {
	assertNotInitialized();

	this.writeNull = true;
	return this;
    }

    public CsvEscaper separator(char separator) {
	assertNotInitialized();

	this.separator = separator;
	return this;
    }

    public CsvEscaper escCrLf() {
	assertNotInitialized();

	this.escCrLf = true;
	return this;
    }

    public String escape(String value) {
	initialize();

	if (value == null) {
	    if (this.writeNull) {
		return "null";
	    }
	    return "";
	}

	if (value.isEmpty()) {
	    return "";
	}

	StringBuilder out = new StringBuilder((int) (value.length() * 1.25f));
	out.append('"');

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
		    out.append(srcChars, start, i - start);
		}

		// add escaped
		out.append("\"\"");
		start = i + 1;

	    } else if (this.escCrLf && (c == '\r' || c == '\n')) {

		// add un_escaped portion
		if (start < i) {
		    out.append(srcChars, start, i - start);
		}

		// add escaped
		out.append("\\").append(c == '\r' ? 'r' : 'n');
		start = i + 1;
	    }
	}

	// add rest of un_escaped portion
	if (start < length) {
	    out.append(srcChars, start, length - start);
	}

	out.append('"');
	return useWrap ? out.toString() : value;
    }

    public String unescape(String value) {
	// ,,
	if (value.isEmpty()) {
	    return null;
	}

	// ,null,
	if ("null".equals(value)) {
	    return this.writeNull ? null : "null";
	}

	// ,value,
	if (!this.escCrLf) {
	    return value;
	}

	// \\r \\n
	StringBuilder out = new StringBuilder(value.length());
	boolean crlf = false;

	for (int i = 0; i < value.length(); i++) {
	    char c = value.charAt(i);

	    if (c != '\\') {
		out.append(c);
		continue;
	    }

	    // c = '\\'
	    if (i + 1 < value.length()) {
		char nc = value.charAt(i + 1);

		if (nc == 'r' || nc == 'n') {
		    out.append(nc == 'r' ? '\r' : '\n');
		    i += 1;

		    crlf = true;
		    continue;
		}
	    }
	}
	return crlf ? out.toString() : value;
    }
}
