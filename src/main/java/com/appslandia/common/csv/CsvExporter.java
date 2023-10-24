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
import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.base.Out;
import com.appslandia.common.data.RecordContext;
import com.appslandia.common.jdbc.ConnectionImpl;
import com.appslandia.common.jdbc.ResultSetColumn;
import com.appslandia.common.utils.Asserts;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CsvExporter extends InitializeObject {

    private BufferedWriter csvOutput;
    private ConnectionImpl connection;
    private String pQuery;
    private Map<String, Object> pQueryParams;

    private CsvProcessor csvProcessor;

    final Map<String, DbToCsvConverter> converters = new HashMap<>();

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
			    this.csvOutput.write(csvProcessor.getSeparator());
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
			this.csvOutput.write(csvProcessor.getSeparator());
		    }

		    if (value == null) {
			this.csvOutput.write(this.csvProcessor.escape(null));

		    } else if (Number.class.isAssignableFrom(value.getClass()) || Temporal.class.isAssignableFrom(value.getClass()) || value.getClass() == Boolean.class
			    || java.util.Date.class.isAssignableFrom(value.getClass())) {

			this.csvOutput.write(value.toString());
		    } else {
			this.csvOutput.write(this.csvProcessor.escape((value != null) ? value.toString() : null));
		    }

		}
		this.csvOutput.newLine();
	    });

	    this.csvOutput.flush();
	}
	return counter.get();
    }

    public CsvExporter setCsvOutput(BufferedWriter csvOutput) {
	assertNotInitialized();
	this.csvOutput = csvOutput;
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
