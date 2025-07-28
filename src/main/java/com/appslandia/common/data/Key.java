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

import java.util.LinkedHashMap;

import com.appslandia.common.base.CaseInsensitiveMap;
import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class Key extends CaseInsensitiveMap<Object> {
  private static final long serialVersionUID = 1L;

  public Key() {
    super(new LinkedHashMap<>());
  }

  public Key(String keyColumn, Object value) {
    super(new LinkedHashMap<>());

    super.put(keyColumn, value);
  }

  public <T> T get(String keyColumn) {
    return ObjectUtils.cast(super.get(keyColumn));
  }

  public Key set(String keyColumn, Object value) {
    super.put(keyColumn, value);
    return this;
  }
}
