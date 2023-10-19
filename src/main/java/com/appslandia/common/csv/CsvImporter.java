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
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.base.Language;
import com.appslandia.common.data.Column;
import com.appslandia.common.data.DataRecord;
import com.appslandia.common.data.RecordContext;
import com.appslandia.common.data.Table;
import com.appslandia.common.jdbc.ConnectionImpl;
import com.appslandia.common.jdbc.JdbcParam;
import com.appslandia.common.jdbc.UncheckedSQLException;
import com.appslandia.common.utils.ArrayUtils;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.STR;
import com.appslandia.common.utils.TypeUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CsvImporter extends InitializeObject {

    private BufferedReader csvInput;
    private boolean csvHeader;
    private ConnectionImpl connection;
    private String tableName;
    private CsvProcessor csvReader;

    private boolean executeInserts;
    private CsvDebugger csvDebugger;

    private String[] datePatterns;
    private String[] timePatterns;
    private String[] dateTimePatterns;

    private String[] offsetTimePatterns;
    private String[] offsetDateTimePatterns;

    final Map<Indexes, Function<String, String>> processors = new LinkedHashMap<>();

    private static final Language DEFAULT_LANGUAGE;
    static {
	String defDatePattern = DateUtils.toDatePattern(Locale.getDefault());
	DEFAULT_LANGUAGE = (defDatePattern != null) ? new Language().setLocale(Locale.getDefault()).setDatePattern(defDatePattern) : null;
    }

    @Override
    protected void init() throws Exception {
	Asserts.notNull(this.tableName);
	Asserts.notNull(this.csvInput);

	if (this.connection == null) {
	    this.connection = ConnectionImpl.getCurrent();
	}
	if (this.csvReader == null) {
	    this.csvReader = CsvProcessor.INSTANCE;
	}

	// Default patterns

	if (!ArrayUtils.hasElements(this.datePatterns)) {
	    this.datePatterns = new DTPatterns(DateUtils.ISO8601_DATE).toArray();
	}

	if (!ArrayUtils.hasElements(this.timePatterns)) {
	    // @formatter:off
	    this.timePatterns = new String[] {
		    DateUtils.ISO8601_TIME_M,
		    DateUtils.ISO8601_TIME_S,
		    DateUtils.ISO8601_TIME_N1,
		    DateUtils.ISO8601_TIME_N2,
		    DateUtils.ISO8601_TIME_N3,
		    DateUtils.ISO8601_TIME_N4,
		    DateUtils.ISO8601_TIME_N5,
		    DateUtils.ISO8601_TIME_N6,
		    DateUtils.ISO8601_TIME_N7
	    };
	    // @formatter:on
	}

	if (!ArrayUtils.hasElements(this.dateTimePatterns)) {
	    // @formatter:off
	    this.dateTimePatterns = new DTPatterns(
		        DateUtils.ISO8601_DATETIME_M,
		        DateUtils.ISO8601_DATETIME_S,
		        DateUtils.ISO8601_DATETIME_N1,
		        DateUtils.ISO8601_DATETIME_N2,
		        DateUtils.ISO8601_DATETIME_N3,
		        DateUtils.ISO8601_DATETIME_N4,
		        DateUtils.ISO8601_DATETIME_N5,
		        DateUtils.ISO8601_DATETIME_N6,
		        DateUtils.ISO8601_DATETIME_N7 
		    ).toArray();
	    // @formatter:on
	}

	if (!ArrayUtils.hasElements(this.offsetTimePatterns)) {
	    // @formatter:off
	    this.offsetTimePatterns = new String[] {
		        DateUtils.ISO8601_TIMEZ_M,
		        DateUtils.ISO8601_TIMEZ_S,
		        DateUtils.ISO8601_TIMEZ_N1,
		        DateUtils.ISO8601_TIMEZ_N2,
		        DateUtils.ISO8601_TIMEZ_N3,
		        DateUtils.ISO8601_TIMEZ_N4,
		        DateUtils.ISO8601_TIMEZ_N5,
		        DateUtils.ISO8601_TIMEZ_N6,
		        DateUtils.ISO8601_TIMEZ_N7		    
	    };
	    // @formatter:on
	}

	if (!ArrayUtils.hasElements(this.offsetDateTimePatterns)) {
	    // @formatter:off
	    this.offsetDateTimePatterns = new DTPatterns(
		        DateUtils.ISO8601_DATETIMEZ_M,
		        DateUtils.ISO8601_DATETIMEZ_S,
		        DateUtils.ISO8601_DATETIMEZ_N1,
		        DateUtils.ISO8601_DATETIMEZ_N2,
		        DateUtils.ISO8601_DATETIMEZ_N3,
		        DateUtils.ISO8601_DATETIMEZ_N4,
		        DateUtils.ISO8601_DATETIMEZ_N5,
		        DateUtils.ISO8601_DATETIMEZ_N6,
		        DateUtils.ISO8601_DATETIMEZ_N7
		    ).toArray();
	    // @formatter:on
	}
    }

    public int execute() throws IOException, SQLException {
	initialize();

	try (RecordContext ctx = new RecordContext(this.connection)) {
	    Table table = ctx.getTable(this.tableName);

	    try {
		// Transactional
		if (this.executeInserts) {
		    ctx.setTransactional(true);
		}

		final AtomicInteger counter = new AtomicInteger(0);

		this.csvReader.parse(this.csvInput, (idx, csvRecord) -> {
		    Asserts.isTrue(table.getColumns().size() == csvRecord.length(), "The number of columns does not match.");

		    if (this.csvHeader) {
			if (idx == 0) {
			    return;
			}
		    }

		    // Processors
		    for (Map.Entry<Indexes, Function<String, String>> processor : this.processors.entrySet()) {
			csvRecord.applyProcessor(processor.getValue(), processor.getKey().indexes);
		    }

		    try {
			// Build record
			DataRecord dataRecord = new DataRecord();
			for (int colIdx = 0; colIdx < table.getColumns().size(); colIdx++) {

			    Column col = table.getColumns().get(colIdx);
			    dataRecord.set(col.getName(), toColumnValue(csvRecord, colIdx, col, ctx.getConnection()));
			}

			// csvDebugger
			if (this.csvDebugger != null) {
			    this.csvDebugger.apply(counter.get(), csvRecord, dataRecord);
			}

			// Insert the record (batch)
			if (this.executeInserts) {
			    ctx.insert(table.getName(), dataRecord, true);
			}

			int inserts = counter.incrementAndGet();

			// executeBatch markers
			if (inserts > 0 && inserts % 100 == 0) {

			    if (this.executeInserts) {
				ctx.executeBatch();
			    }
			}

		    } catch (SQLException ex) {
			throw new UncheckedSQLException(ex);
		    }
		});

		// last executeBatch
		if (this.executeInserts) {
		    ctx.executeBatch();
		}

		// Commit all batches
		if (this.executeInserts) {
		    ctx.commit();
		}

		return counter.get();

	    } catch (Exception ex) {

		// Rollback
		if (this.executeInserts) {
		    ctx.rollback();
		}
		throw ex;
	    }
	}
    }

    protected Object toColumnValue(CsvRecord csv, int idx, Column column, ConnectionImpl conn) throws SQLException {
	String value = csv.getString(idx);
	if (value == null) {
	    return new JdbcParam(null, column.getSqlType());
	}
	Class<?> type = TypeUtils.wrap(column.getJavaType());

	if (type == String.class || type == Reader.class) {
	    return value;
	}
	if (type == Boolean.class) {
	    return csv.getBool(idx);
	}

	if (type == Byte.class) {
	    return csv.getByte(idx);
	}
	if (type == Short.class) {
	    return csv.getShort(idx);
	}
	if (type == Integer.class) {
	    return csv.getInt(idx);
	}
	if (type == Long.class) {
	    return csv.getLong(idx);
	}
	if (type == Float.class) {
	    return csv.getFloat(idx);
	}
	if (type == Double.class) {
	    return csv.getDouble(idx);
	}
	if (type == BigDecimal.class) {
	    return csv.getDecimal(idx);
	}

	if (type == LocalDate.class) {
	    return csv.getLocalDate(idx, this.datePatterns);
	}
	if (type == LocalTime.class) {
	    return csv.getLocalTime(idx, this.timePatterns);
	}
	if (type == LocalDateTime.class) {
	    return csv.getLocalDateTime(idx, this.dateTimePatterns);
	}
	if (type == OffsetTime.class) {
	    return csv.getOffsetTime(idx, this.offsetTimePatterns);
	}
	if (type == OffsetDateTime.class) {
	    return csv.getOffsetDateTime(idx, this.offsetDateTimePatterns);
	}

	// SQLXML
	if (type == SQLXML.class) {
	    SQLXML xml = conn.createSQLXML();
	    xml.setString(value);

	    return xml;
	}

	// URL
	if (type == URL.class) {
	    try {
		return new URL(value);
	    } catch (MalformedURLException ex) {
	    }
	}
	throw new IllegalArgumentException(STR.fmt("Failed to convert value for the column {}.", column.toString()));
    }

    public CsvImporter setCsvInput(BufferedReader csvInput) {
	assertNotInitialized();
	this.csvInput = csvInput;
	return this;
    }

    public CsvImporter setCsvHeader(boolean csvHeader) {
	assertNotInitialized();
	this.csvHeader = csvHeader;
	return this;
    }

    public CsvImporter setConnection(ConnectionImpl connection) {
	assertNotInitialized();
	this.connection = connection;
	return this;
    }

    public CsvImporter setTableName(String tableName) {
	assertNotInitialized();
	this.tableName = tableName;
	return this;
    }

    public CsvImporter setCsvReader(CsvProcessor csvReader) {
	assertNotInitialized();
	this.csvReader = csvReader;
	return this;
    }

    public CsvImporter setExecuteInserts(boolean executeInserts) {
	assertNotInitialized();
	this.executeInserts = executeInserts;
	return this;
    }

    public CsvImporter setCsvDebugger(CsvDebugger csvDebugger) {
	assertNotInitialized();
	this.csvDebugger = csvDebugger;
	return this;
    }

    public CsvImporter setDatePatterns(String... datePatterns) {
	assertNotInitialized();
	this.datePatterns = ArrayUtils.copy(datePatterns);
	return this;
    }

    public CsvImporter setTimePatterns(String... timePatterns) {
	assertNotInitialized();
	this.timePatterns = ArrayUtils.copy(timePatterns);
	return this;
    }

    public CsvImporter setDateTimePatterns(String... dateTimePatterns) {
	assertNotInitialized();
	this.dateTimePatterns = ArrayUtils.copy(dateTimePatterns);
	return this;
    }

    public CsvImporter setOffsetTimePatterns(String... offsetTimePatterns) {
	assertNotInitialized();
	this.offsetTimePatterns = ArrayUtils.copy(offsetTimePatterns);
	return this;
    }

    public CsvImporter setOffsetDateTimePatterns(String... offsetDateTimePatterns) {
	assertNotInitialized();
	this.offsetDateTimePatterns = ArrayUtils.copy(offsetDateTimePatterns);
	return this;
    }

    public CsvImporter mapProcessor(Function<String, String> processor, int... indexes) {
	assertNotInitialized();

	Asserts.notNull(processor);
	Asserts.hasElements(indexes);

	this.processors.put(new Indexes(indexes), processor);
	return this;
    }

    private static class Indexes {
	final int[] indexes;

	public Indexes(int[] indexes) {
	    this.indexes = ArrayUtils.copy(indexes);
	}

	@Override
	public boolean equals(Object obj) {
	    Indexes other = (Indexes) obj;
	    return Arrays.equals(this.indexes, other.indexes);
	}

	@Override
	public int hashCode() {
	    return Arrays.hashCode(this.indexes);
	}

	@Override
	public String toString() {
	    return Arrays.toString(this.indexes);
	}
    }

    private static class DTPatterns {

	final List<String> values = new ArrayList<>();

	public DTPatterns(String... isoPatterns) {
	    for (String isoPattern : isoPatterns) {
		this.values.add(isoPattern);

		if (DEFAULT_LANGUAGE != null) {
		    this.values.add(DEFAULT_LANGUAGE.getTemporalPattern(isoPattern));
		}
	    }
	}

	public String[] toArray() {
	    return this.values.toArray(new String[this.values.size()]);
	}
    }
}
