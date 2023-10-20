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
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.appslandia.common.base.BOMInputStream;
import com.appslandia.common.base.BOMOutputStream;
import com.appslandia.common.utils.Asserts;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CsvUtils {

    public static BufferedReader csvReader(InputStream is, String encodingIfNoBOM) throws IOException {
	Asserts.notNull(encodingIfNoBOM);

	var bomIS = new BOMInputStream(is);
	return new BufferedReader(new InputStreamReader(bomIS, bomIS.getBOM() != null ? bomIS.getBOM().getEncoding() : encodingIfNoBOM));
    }

    public static BufferedReader csvReader(String csvFile, String encodingIfNoBOM) throws IOException {
	return csvReader(new FileInputStream(csvFile), encodingIfNoBOM);
    }

    public static BufferedWriter csvWriter(OutputStream os, String encoding) throws IOException {
	Asserts.notNull(encoding);
	return new BufferedWriter(new OutputStreamWriter(new BOMOutputStream(os, encoding), encoding));
    }

    public static BufferedWriter csvWriter(String csvFile, String encoding) throws IOException {
	return csvWriter(new FileOutputStream(csvFile), encoding);
    }
}
