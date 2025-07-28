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

import com.appslandia.common.utils.STR;

/**
 *
 * @author Loc Ha
 *
 */
public class JdbcParam {

  final Object value;
  final Integer sqlType;
  final Integer scaleOrLength;

  public JdbcParam(Object value, Integer sqlType) {
    this(value, sqlType, null);
  }

  public JdbcParam(Object value, Integer sqlType, Integer scaleOrLength) {
    this.value = value;
    this.sqlType = sqlType;
    this.scaleOrLength = scaleOrLength;
  }

  public Object getValue() {
    return this.value;
  }

  public Integer getSqlType() {
    return this.sqlType;
  }

  public Integer getScaleOrLength() {
    return this.scaleOrLength;
  }

  @Override
  public String toString() {
    return STR.fmt("value={?}, sqlType={?}, scaleOrLength={?}", this.value, this.sqlType, this.scaleOrLength);
  }
}
