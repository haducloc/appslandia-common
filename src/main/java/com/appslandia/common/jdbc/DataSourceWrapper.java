// The MIT License (MIT)
// Copyright © 2015 Loc Ha

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

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

import javax.sql.DataSource;

import com.appslandia.common.utils.ObjectUtils;

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
    return this.name;
  }

  // javax.sql.DataSource

  @Override
  public Connection getConnection() throws SQLException {
    return this.ds.getConnection();
  }

  @Override
  public Connection getConnection(String arg0, String arg1) throws SQLException {
    return this.ds.getConnection(arg0, arg1);
  }

  // javax.sql.CommonDataSource

  @Override
  public PrintWriter getLogWriter() throws SQLException {
    return this.ds.getLogWriter();
  }

  @Override
  public int getLoginTimeout() throws SQLException {
    return this.ds.getLoginTimeout();
  }

  @Override
  public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
    return this.ds.getParentLogger();
  }

  @Override
  public void setLogWriter(PrintWriter arg0) throws SQLException {
    this.ds.setLogWriter(arg0);
  }

  @Override
  public void setLoginTimeout(int arg0) throws SQLException {
    this.ds.setLoginTimeout(arg0);
  }

  // Wrapper

  @Override
  public boolean isWrapperFor(Class<?> arg0) throws SQLException {
    return this.ds.isWrapperFor(arg0);
  }

  @Override
  public <T> T unwrap(Class<T> arg0) throws SQLException {
    return this.ds.unwrap(arg0);
  }

  @Override
  public String toString() {
    return ObjectUtils.toStringWrapper(this, this.ds);
  }
}
