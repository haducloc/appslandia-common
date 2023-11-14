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

package com.appslandia.common.data;

import java.util.Arrays;
import java.util.LinkedHashMap;

import com.appslandia.common.base.CaseInsensitiveMap;
import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DataRecord extends CaseInsensitiveMap<Object> {
  private static final long serialVersionUID = 1L;

  public DataRecord() {
    super(new LinkedHashMap<>());
  }

  public DataRecord set(String columnName, Object value) {
    super.put(columnName, value);
    return this;
  }

  public <T> T get(String columnName) {
    return ObjectUtils.cast(super.get(columnName));
  }

  public Object[] toValues(Table table) {
    return toValues(table.getColumnLabels());
  }

  public Object[] toValues(String[] columnNames) {
    return Arrays.stream(columnNames).map(l -> super.get(l)).toArray();
  }
}
