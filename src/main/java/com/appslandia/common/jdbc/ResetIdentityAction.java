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

import com.appslandia.common.utils.STR;

/**
 * 
 * 
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public interface ResetIdentityAction {

  boolean resetIdentity(ConnectionImpl conn, String tableName) throws java.sql.SQLException;

  public static class UnimplementedResetIdentityAction implements ResetIdentityAction {

    public static final UnimplementedResetIdentityAction INSTANCE = new UnimplementedResetIdentityAction();

    @Override
    public boolean resetIdentity(ConnectionImpl conn, String tableName) throws java.sql.SQLException {
      throw new UnsupportedOperationException(
          "The method resetIdentity() is not implemented for the database type: " + conn.getDbDialect().getType());
    }
  }

  public static class MSSQLResetIdentityAction implements ResetIdentityAction {

    @Override
    public boolean resetIdentity(ConnectionImpl conn, String tableName) throws java.sql.SQLException {
      String idPk = JdbcUtils.getIdentityPK(conn, null, null, tableName);
      if (idPk == null) {
        return false;
      }

      DbDialect dbDialect = conn.getDbDialect();
      String quotedTable = dbDialect.quoteIdentifier(tableName);

      long curMaxPk = conn
          .executeScalar(STR.fmt("SELECT MAX({}) FROM {}", dbDialect.quoteIdentifier(idPk), quotedTable), Long.class);

      conn.executeUpdate(STR.fmt("DBCC CHECKIDENT ('{}', RESEED, {})", quotedTable, curMaxPk));
      return true;
    }
  }
}
