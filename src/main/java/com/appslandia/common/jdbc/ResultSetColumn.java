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
public class ResultSetColumn {

  final int index;
  final String name;
  final int sqlType;

  public ResultSetColumn(int index, String name, int sqlType) {
    this.index = index;
    this.name = name;
    this.sqlType = sqlType;
  }

  public int getIndex() {
    return this.index;
  }

  public String getName() {
    return this.name;
  }

  public int getSqlType() {
    return this.sqlType;
  }

  @Override
  public String toString() {
    return STR.fmt("index={}, name={}, sqlType={}", this.name, this.index, this.sqlType);
  }

  @Override
  public int hashCode() {
    return Integer.hashCode(this.index);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof ResultSetColumn that)) {
      return false;
    }
    return this.index == that.index;
  }
}
