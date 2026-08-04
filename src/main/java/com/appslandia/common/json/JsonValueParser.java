// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.json;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Pattern;

import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.base.Out;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class JsonValueParser extends InitializingObject {

  static final String ROOT_PATH = "";

  final Map<String, Function<Object, Object>> valueConverters = new HashMap<>();
  final Map<String, Pattern> pathPatterns = new ConcurrentHashMap<>();

  private JsonValueConverter jsonValueConverter;
  private Function<Map<String, Object>, Object> mapConverter;

  @Override
  protected void init() throws Exception {
    Arguments.notNull(jsonValueConverter);
  }

  public <F, V> JsonValueParser setValueConverter(String[] pathOrPatterns, Function<F, V> converter) {
    assertNotInitialized();

    for (String pathOrPattern : pathOrPatterns) {
      valueConverters.put(pathOrPattern, ObjectUtils.cast(converter));
    }
    return this;
  }

  public <V> JsonValueParser setRootConverter(Function<Object, V> converter) {
    assertNotInitialized();

    valueConverters.put(ROOT_PATH, ObjectUtils.cast(converter));
    return this;
  }

  public JsonValueParser setJsonValueConverter(JsonValueConverter jsonValueConverter) {
    assertNotInitialized();
    this.jsonValueConverter = jsonValueConverter;
    return this;
  }

  public JsonValueParser setMapConverter(Function<Map<String, Object>, Object> mapConverter) {
    assertNotInitialized();
    this.mapConverter = mapConverter;
    return this;
  }

  public Object parseRoot(Object rootElement) {
    initialize();
    Arguments.notNull(rootElement);

    var rootObj = parseValue(rootElement, new StringBuilder(), new Out<>());

    var rootConverter = valueConverters.get(ROOT_PATH);
    return (rootConverter != null) ? rootConverter.apply(rootObj) : rootObj;
  }

  @SuppressWarnings("unchecked")
  protected Object convertValue(Object value, String path) {
    var converter = valueConverters.get(path);
    if (converter != null) {
      return converter.apply(value);
    }

    for (Map.Entry<String, Function<Object, Object>> converterEntry : valueConverters.entrySet()) {
      var pattern = pathPatterns.computeIfAbsent(converterEntry.getKey(),
          (p) -> Pattern.compile(p, Pattern.CASE_INSENSITIVE));

      if (pattern.matcher(path).matches()) {
        return converterEntry.getValue().apply(value);
      }
    }

    // mapConverter
    if (mapConverter != null && value instanceof Map) {
      value = mapConverter.apply((Map<String, Object>) value);
    }
    return value;
  }

  protected Object parseValue(Object element, StringBuilder path, Out<Boolean> asResult) {
    // NULL
    if (jsonValueConverter.isJsonNull(element)) {
      return null;
    }

    // String
    var strValue = jsonValueConverter.asString(element, asResult.set(false));
    if (Boolean.TRUE.equals(asResult.value)) {
      return strValue;
    }

    // Boolean
    var boolValue = jsonValueConverter.asBoolean(element, asResult.set(false));
    if (Boolean.TRUE.equals(asResult.value)) {
      return boolValue;
    }

    // Double/Long
    var numberVal = jsonValueConverter.asNumber(element, asResult.set(false));
    if (Boolean.TRUE.equals(asResult.value)) {
      try {
        return Long.parseLong(numberVal);

      } catch (NumberFormatException e) {
        return new BigDecimal(numberVal);
      }
    }

    // Array
    Iterator<?> childElements = jsonValueConverter.asJsonArray(element, asResult.set(false));
    if (Boolean.TRUE.equals(asResult.value)) {
      List<Object> list = new ArrayList<>();
      var idx = 0;

      while (childElements.hasNext()) {
        Object childElement = childElements.next();

        var len = path.length();
        path.append('[').append(idx++).append(']');

        var parsedVal = parseValue(childElement, path, asResult.set(false));
        parsedVal = convertValue(parsedVal, path.toString());

        list.add(parsedVal);
        path.setLength(len);
      }
      return list;
    }

    // MAP
    var childElementEntries = jsonValueConverter.asJsonObject(element, asResult.set(false));
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
