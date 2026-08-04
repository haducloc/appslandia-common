// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.json;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Function;

import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.ObjectUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 *
 * @author Loc Ha
 *
 */
public class GsonMapAdapter<T extends Map<String, Object>> implements JsonDeserializer<T> {

  final JsonValueParser jsonValueParser = new JsonValueParser().setJsonValueConverter(GsonJsonValueConverter.INSTANCE);

  public GsonMapAdapter(Function<Map<String, Object>, T> rootConverter) {
    jsonValueParser.setRootConverter(ObjectUtils.cast(rootConverter));
  }

  public <F, V> GsonMapAdapter<T> setValueConverter(String[] pathOrPatterns, Function<F, V> converter) {
    jsonValueParser.setValueConverter(pathOrPatterns, converter);
    return this;
  }

  public <F, V> GsonMapAdapter<T> setMapConverter(Function<Map<String, Object>, Object> mapConverter) {
    jsonValueParser.setMapConverter(mapConverter);
    return this;
  }

  @Override
  public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    Arguments.isTrue(json.isJsonObject(), "json.isJsonObject() must be true.");

    var value = jsonValueParser.parseRoot(json);
    return ObjectUtils.cast(value);
  }
}
