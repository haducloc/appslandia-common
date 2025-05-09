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

package com.appslandia.common.json;

import java.util.Iterator;
import java.util.Map.Entry;

import com.appslandia.common.base.Out;
import com.appslandia.common.utils.ObjectUtils;

import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jakarta.json.JsonValue.ValueType;

/**
 *
 * @author Loc Ha
 *
 */
public class JsonbJsonValueConverter implements JsonValueConverter {

  public static final JsonbJsonValueConverter INSTANCE = new JsonbJsonValueConverter();

  @Override
  public boolean isJsonNull(Object element) {
    var JsonValue = (JsonValue) element;
    return JsonValue.getValueType() == ValueType.NULL;
  }

  @Override
  public String asNumber(Object element, Out<Boolean> asResult) {
    var JsonValue = (JsonValue) element;

    if (JsonValue.getValueType() == ValueType.NUMBER) {
      asResult.value = true;
      return JsonValue.toString();
    }
    return null;
  }

  @Override
  public String asString(Object element, Out<Boolean> asResult) {
    var JsonValue = (JsonValue) element;

    if (JsonValue.getValueType() == ValueType.STRING) {
      asResult.value = true;
      return ((JsonString) JsonValue).getString();
    }
    return null;
  }

  @Override
  public boolean asBoolean(Object element, Out<Boolean> asResult) {
    var JsonValue = (JsonValue) element;

    if ((JsonValue.getValueType() == ValueType.TRUE) || (JsonValue.getValueType() == ValueType.FALSE)) {
      asResult.value = true;
      return JsonValue.getValueType() == ValueType.TRUE;
    }
    return false;
  }

  @Override
  public Iterator<Object> asJsonArray(Object element, Out<Boolean> asResult) {
    var JsonValue = (JsonValue) element;

    if (JsonValue.getValueType() == ValueType.ARRAY) {
      asResult.value = true;
      return ObjectUtils.cast(JsonValue.asJsonArray().iterator());
    }
    return null;
  }

  @Override
  public Iterator<Entry<String, Object>> asJsonObject(Object element, Out<Boolean> asResult) {
    var JsonValue = (JsonValue) element;

    if (JsonValue.getValueType() == ValueType.OBJECT) {
      asResult.value = true;
      return ObjectUtils.cast(JsonValue.asJsonObject().entrySet().iterator());
    }
    return null;
  }
}
