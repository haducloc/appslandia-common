// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

import javax.sql.DataSource;

/**
 *
 * @author Loc Ha
 *
 */
public class DataSourceWrapper implements DataSource {

  private DataSource ds;
  private String name;

  public DataSourceWrapper(DataSource ds) {
    this(ds, null);
  }

  public DataSourceWrapper(DataSource ds, String name) {
    this.ds = ds;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  // javax.sql.DataSource

  @Override
  public Connection getConnection() throws SQLException {
    return ds.getConnection();
  }

  @Override
  public Connection getConnection(String arg0, String arg1) throws SQLException {
    return ds.getConnection(arg0, arg1);
  }

  // javax.sql.CommonDataSource

  @Override
  public PrintWriter getLogWriter() throws SQLException {
    return ds.getLogWriter();
  }

  @Override
  public int getLoginTimeout() throws SQLException {
    return ds.getLoginTimeout();
  }

  @Override
  public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
    return ds.getParentLogger();
  }

  @Override
  public void setLogWriter(PrintWriter arg0) throws SQLException {
    ds.setLogWriter(arg0);
  }

  @Override
  public void setLoginTimeout(int arg0) throws SQLException {
    ds.setLoginTimeout(arg0);
  }

  // Wrapper

  @Override
  public boolean isWrapperFor(Class<?> arg0) throws SQLException {
    return ds.isWrapperFor(arg0);
  }

  @Override
  public <T> T unwrap(Class<T> arg0) throws SQLException {
    return ds.unwrap(arg0);
  }
}
