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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.STR;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SqlAdminUtils {

    public static long fixIdSeq(Connection conn, String tableName, String idPkCol, boolean idPkInt, String idPkAlter1, String idPkAlter2, String idPkAlter3) throws SQLException {

	Asserts.notNull(tableName);
	Asserts.notNull(idPkCol);

	long seq = 0;
	try (Statement stat = conn.createStatement()) {

	    // Add tempIdPkCol
	    String tempIdPkCol = idPkCol + "_temp";
	    stat.executeUpdate(STR.fmt("ALTER TABLE {} ADD {} {}", tableName, tempIdPkCol, (idPkInt ? "INT" : "BIGINT")));

	    // Update tempIdPkCol
	    try (PreparedStatement updStat = conn.prepareStatement(STR.fmt("UPDATE {} SET {}=? WHERE {}=?", tableName, tempIdPkCol, idPkCol))) {
		try (ResultSet rs = stat.executeQuery(STR.fmt("SELECT {} FROM {} ORDER BY {}", idPkCol, tableName, idPkCol))) {

		    while (rs.next()) {
			seq++;

			if (idPkInt) {
			    updStat.setInt(1, (int) seq);
			    updStat.setInt(2, rs.getInt(1));
			} else {
			    updStat.setLong(1, seq);
			    updStat.setLong(2, rs.getLong(1));
			}
			updStat.addBatch();

			if (seq % 50 == 0)
			    updStat.executeBatch();
		    }
		}

		updStat.executeBatch();
	    }

	    // Re-create idPkCol
	    stat.executeUpdate(STR.fmt("ALTER TABLE {} DROP COLUMN {}", tableName, idPkCol));
	    stat.executeUpdate(STR.fmt("ALTER TABLE {} ADD {} {}", tableName, idPkCol, (idPkInt ? "INT" : "BIGINT")));
	    stat.executeUpdate(STR.fmt("UPDATE {} SET {}={}", tableName, idPkCol, tempIdPkCol));

	    // Execute Alters
	    if (idPkAlter1 != null)
		stat.executeUpdate(STR.fmt("ALTER TABLE {} {}", tableName, idPkAlter1));

	    if (idPkAlter2 != null)
		stat.executeUpdate(STR.fmt("ALTER TABLE {} {}", tableName, idPkAlter2));

	    if (idPkAlter3 != null)
		stat.executeUpdate(STR.fmt("ALTER TABLE {} {}", tableName, idPkAlter3));

	    // Drop tempIdPkCol
	    stat.executeUpdate(STR.fmt("ALTER TABLE {} DROP COLUMN {}", tableName, tempIdPkCol));

	    return seq;
	}
    }
}
