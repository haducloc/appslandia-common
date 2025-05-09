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
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 *
 * @author Loc Ha
 *
 */
public class GsonJsonValueConverter implements JsonValueConverter {

  public static final GsonJsonValueConverter INSTANCE = new GsonJsonValueConverter();

  @Override
  public boolean isJsonNull(Object element) {
    return ((JsonElement) element).isJsonNull();
  }

  @Override
  public String asNumber(Object element, Out<Boolean> asResult) {
    var jsonElement = (JsonElement) element;

    if (jsonElement.isJsonPrimitive() && ((JsonPrimitive) jsonElement).isNumber()) {
      asResult.value = true;
      return jsonElement.getAsString();
    }
    return null;
  }

  @Override
  public String asString(Object element, Out<Boolean> asResult) {
    var jsonElement = (JsonElement) element;

    if (jsonElement.isJsonPrimitive() && ((JsonPrimitive) jsonElement).isString()) {
      asResult.value = true;
      return jsonElement.getAsString();
    }
    return null;
  }

  @Override
  public boolean asBoolean(Object element, Out<Boolean> asResult) {
    var jsonElement = (JsonElement) element;

    if (jsonElement.isJsonPrimitive() && ((JsonPrimitive) jsonElement).isBoolean()) {
      asResult.value = true;
      return jsonElement.getAsBoolean();
    }
    return false;
  }

  @Override
  public Iterator<Object> asJsonArray(Object element, Out<Boolean> asResult) {
    var jsonElement = (JsonElement) element;

    if (jsonElement.isJsonArray()) {
      asResult.value = true;
      return ObjectUtils.cast(jsonElement.getAsJsonArray().iterator());
    }
    return null;
  }

  @Override
  public Iterator<Entry<String, Object>> asJsonObject(Object element, Out<Boolean> asResult) {
    var jsonElement = (JsonElement) element;

    if (jsonElement.isJsonObject()) {
      asResult.value = true;
      return ObjectUtils.cast(jsonElement.getAsJsonObject().entrySet().iterator());
    }
    return null;
  }
}
