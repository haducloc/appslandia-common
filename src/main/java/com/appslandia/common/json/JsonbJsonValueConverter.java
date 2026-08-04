// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
    var jsonValue = (JsonValue) element;
    return jsonValue.getValueType() == ValueType.NULL;
  }

  @Override
  public String asNumber(Object element, Out<Boolean> asResult) {
    var jsonValue = (JsonValue) element;

    if (jsonValue.getValueType() == ValueType.NUMBER) {
      asResult.value = true;

      return jsonValue.toString();
    }
    return null;
  }

  @Override
  public String asString(Object element, Out<Boolean> asResult) {
    var jsonValue = (JsonValue) element;

    if (jsonValue.getValueType() == ValueType.STRING) {
      asResult.value = true;
      return ((JsonString) jsonValue).getString();
    }
    return null;
  }

  @Override
  public boolean asBoolean(Object element, Out<Boolean> asResult) {
    var jsonValue = (JsonValue) element;

    if ((jsonValue.getValueType() == ValueType.TRUE) || (jsonValue.getValueType() == ValueType.FALSE)) {
      asResult.value = true;
      return jsonValue.getValueType() == ValueType.TRUE;
    }
    return false;
  }

  @Override
  public Iterator<Object> asJsonArray(Object element, Out<Boolean> asResult) {
    var jsonValue = (JsonValue) element;

    if (jsonValue.getValueType() == ValueType.ARRAY) {
      asResult.value = true;
      return ObjectUtils.cast(jsonValue.asJsonArray().iterator());
    }
    return null;
  }

  @Override
  public Iterator<Entry<String, Object>> asJsonObject(Object element, Out<Boolean> asResult) {
    var jsonValue = (JsonValue) element;

    if (jsonValue.getValueType() == ValueType.OBJECT) {
      asResult.value = true;
      return ObjectUtils.cast(jsonValue.asJsonObject().entrySet().iterator());
    }
    return null;
  }
}
