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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Pattern;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.base.Out;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class JsonObjectParser extends InitializeObject {

  static final String ROOT_PATH = "";

  final Map<String, Function<Object, Object>> valueConverters = new HashMap<>();
  final Map<String, Pattern> pathPatterns = new ConcurrentHashMap<>();

  private JsonValueConverter jsonValueConverter;
  private Function<Map<String, Object>, Object> mapConverter;

  @Override
  protected void init() throws Exception {
    Arguments.notNull(this.jsonValueConverter);
  }

  public <F, V> JsonObjectParser setValueConverter(String[] pathOrPatterns, Function<F, V> converter) {
    this.assertNotInitialized();

    for (String pathOrPattern : pathOrPatterns) {
      this.valueConverters.put(pathOrPattern, ObjectUtils.cast(converter));
    }
    return this;
  }

  public <V> JsonObjectParser setRootConverter(Function<Object, V> converter) {
    this.assertNotInitialized();

    this.valueConverters.put(ROOT_PATH, ObjectUtils.cast(converter));
    return this;
  }

  public JsonObjectParser setJsonValueConverter(JsonValueConverter jsonValueConverter) {
    this.assertNotInitialized();
    this.jsonValueConverter = jsonValueConverter;
    return this;
  }

  public JsonObjectParser setMapConverter(Function<Map<String, Object>, Object> mapConverter) {
    this.assertNotInitialized();
    this.mapConverter = mapConverter;
    return this;
  }

  public Object parse(Object rootElement) {
    this.initialize();
    Arguments.notNull(rootElement);

    var rootObj = parseValue(rootElement, new StringBuilder(), new Out<>());

    var rootConverter = this.valueConverters.get(ROOT_PATH);
    return (rootConverter != null) ? rootConverter.apply(rootObj) : rootObj;
  }

  @SuppressWarnings("unchecked")
  protected Object convertValue(Object value, String path) {
    var converter = this.valueConverters.get(path);
    if (converter != null) {
      return converter.apply(value);
    }

    for (Map.Entry<String, Function<Object, Object>> converterEntry : this.valueConverters.entrySet()) {
      var pattern = this.pathPatterns.computeIfAbsent(converterEntry.getKey(),
          (p) -> Pattern.compile(p, Pattern.CASE_INSENSITIVE));

      if (pattern.matcher(path).matches()) {
        return converterEntry.getValue().apply(value);
      }
    }

    // mapConverter
    if (this.mapConverter != null && value instanceof Map) {
      value = this.mapConverter.apply((Map<String, Object>) value);
    }
    return value;
  }

  protected Object parseValue(Object element, StringBuilder path, Out<Boolean> asResult) {
    // NULL
    if (this.jsonValueConverter.isJsonNull(element)) {
      return null;
    }

    // String
    var strValue = this.jsonValueConverter.asString(element, asResult.set(false));
    if (Boolean.TRUE.equals(asResult.value)) {
      return strValue;
    }

    // Boolean
    var boolValue = this.jsonValueConverter.asBoolean(element, asResult.set(false));
    if (Boolean.TRUE.equals(asResult.value)) {
      return boolValue;
    }

    // Double/Long
    var numberVal = this.jsonValueConverter.asNumber(element, asResult.set(false));
    if (Boolean.TRUE.equals(asResult.value)) {

      if (numberVal.indexOf('.') >= 0) {
        return Double.parseDouble(numberVal);
      } else {
        try {
          return Long.parseLong(numberVal);

        } catch (NumberFormatException e) {
          return Double.parseDouble(numberVal);
        }
      }
    }

    // Array
    Iterator<?> childElements = this.jsonValueConverter.asJsonArray(element, asResult.set(false));
    if (Boolean.TRUE.equals(asResult.value)) {

      List<Object> list = new ArrayList<>();
      var idx = 0;

      while (childElements.hasNext()) {
        Object childElement = childElements.next();

        var len = path.length();
        path.append("[").append(idx++).append("]");

        var parsedVal = parseValue(childElement, path, asResult.set(false));
        parsedVal = convertValue(parsedVal, path.toString());

        list.add(parsedVal);
        path.setLength(len);
      }
      return list;
    }

    // MAP
    var childElementEntries = this.jsonValueConverter.asJsonObject(element, asResult.set(false));
    if (Boolean.TRUE.equals(asResult.value)) {

      Map<String, Object> map = new LinkedHashMap<>();

      while (childElementEntries.hasNext()) {
        var childElementEntry = childElementEntries.next();

        var len = path.length();
        if (len > 0) {
          path.append('.');
        }
        path.append(childElementEntry.getKey());

        var parsedVal = parseValue(childElementEntry.getValue(), path, asResult.set(false));
        parsedVal = convertValue(parsedVal, path.toString());

        map.put(childElementEntry.getKey(), parsedVal);
        path.setLength(len);
      }
      return map;
    }

    throw new Error();
  }
}
