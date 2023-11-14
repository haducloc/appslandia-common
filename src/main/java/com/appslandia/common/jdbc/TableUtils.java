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

package com.appslandia.common.jdbc;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appslandia.common.base.Out;
import com.appslandia.common.base.TextBuilder;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.NormalizeUtils;
import com.appslandia.common.utils.STR;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class TableUtils {

	private static final String TABLE_NAME_PAT = "[a-z_]+[a-z\\d_]*";
	private static final String COLUMN_NAME_PAT = "[a-z_]+[a-z\\d_]*";
	private static final String COLUMN_TYPE_PAT = "[a-z]+[a-z\\d\\s_]*\\s*(\\(\\s*\\d+\\s*(,\\s*\\d+\\s*)?\\))?\\s*(\\s+[^\\s]+.*)?";

	private static final Pattern TABLE_SPEC_PATTERN = Pattern.compile("^\\s*(" + TABLE_NAME_PAT + ")\\s*\\(\\s*(" + COLUMN_NAME_PAT + "\\s+" + COLUMN_TYPE_PAT
			+ "\\s*(,\\s*" + COLUMN_NAME_PAT + "\\s+" + COLUMN_TYPE_PAT + "\\s*)*)\\)\\s*$", Pattern.CASE_INSENSITIVE);

	public static String toTableScript(String tableSpec, Out<String> tableName) {
		tableSpec = NormalizeUtils.toSingleLine(tableSpec);
		Asserts.notNull(tableSpec);

		Matcher matcher = TABLE_SPEC_PATTERN.matcher(tableSpec);
		if (!matcher.matches()) {
			throw new IllegalArgumentException(STR.fmt("The given tableSpec is invalid: {}", tableSpec));
		}

		// Build script
		TextBuilder script = new TextBuilder(tableSpec.length() + 256);
		tableName.value = matcher.group(1);
		script.append("CREATE TABLE ").append(tableName.value).append("(").appendln();

		String columns = matcher.group(2);
		StringBuilder colDef = new StringBuilder();
		boolean inParentheses = false;

		for (char c : columns.toCharArray()) {
			if (c == '(') {
				inParentheses = true;
			} else if (c == ')') {
				inParentheses = false;
			}

			if (c == ',' && !inParentheses) {
				String[] colParts = parseColumnParts(colDef.toString().trim());
				script.appendsp(4).append(colParts[0]).append(" ").append(colParts[1]).append(",").appendln();

				colDef.setLength(0);
			} else {
				colDef.append(c);
			}
		}

		String[] colParts = parseColumnParts(colDef.toString().trim());
		script.appendsp(4).append(colParts[0]).append(" ").append(colParts[1]).appendln();

		script.append(")");
		return script.toString();
	}

	private static String[] parseColumnParts(String columnDef) {
		int idx = columnDef.indexOf(' ');
		return new String[] { columnDef.substring(0, idx).trim(), columnDef.substring(idx).trim().toUpperCase(Locale.ROOT) };
	}
}
