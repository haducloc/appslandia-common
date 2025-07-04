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

package com.appslandia.common.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

import com.appslandia.common.utils.Asserts;

/**
 *
 * @author Loc Ha
 *
 */
public abstract class BaseGenPk implements Serializable {
  private static final long serialVersionUID = 1L;

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || this.getClass() != obj.getClass()) {
      return false;
    }
    var tableMtdt = this.getClass().getDeclaredAnnotation(TableMtdt.class);
    Asserts.notNull(tableMtdt);

    return Arrays.stream(tableMtdt.keys())
        .allMatch(key -> Objects.equals(RecordUtils.getFieldValue(this, key), RecordUtils.getFieldValue(obj, key)));
  }

  @Override
  public int hashCode() {
    var tableMtdt = this.getClass().getDeclaredAnnotation(TableMtdt.class);
    Asserts.notNull(tableMtdt);

    int hash = 1, p = 31;
    for (String key : tableMtdt.keys()) {
      hash = p * hash + Objects.hashCode(RecordUtils.getFieldValue(this, key));
    }
    return hash;
  }
}
