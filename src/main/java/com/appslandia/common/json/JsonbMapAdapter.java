// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.json;

import java.util.Map;
import java.util.function.Function;

import com.appslandia.common.utils.ObjectUtils;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.bind.adapter.JsonbAdapter;

/**
 *
 * @author Loc Ha
 *
 */
public abstract class JsonbMapAdapter<T extends Map<String, Object>> implements JsonbAdapter<T, JsonObject> {

  final JsonValueParser jsonValueParser = new JsonValueParser().setJsonValueConverter(JsonbJsonValueConverter.INSTANCE);

  public JsonbMapAdapter(Function<Map<String, Object>, T> rootConverter) {
    jsonValueParser.setRootConverter(ObjectUtils.cast(rootConverter));
  }

  public <F, V> JsonbMapAdapter<T> setValueConverter(String[] pathOrPatterns, Function<F, V> converter) {
    jsonValueParser.setValueConverter(pathOrPatterns, converter);
    return this;
  }

  public <F, V> JsonbMapAdapter<T> setMapConverter(Function<Map<String, Object>, Object> mapConverter) {
    jsonValueParser.setMapConverter(mapConverter);
    return this;
  }

  @Override
  public JsonObject adaptToJson(T obj) throws Exception {
    return Json.createObjectBuilder(obj).build();
  }

  @Override
  public T adaptFromJson(JsonObject obj) throws Exception {
    var value = jsonValueParser.parseRoot(obj);
    return ObjectUtils.cast(value);
  }
}
