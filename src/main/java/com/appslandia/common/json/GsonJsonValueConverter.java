// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
