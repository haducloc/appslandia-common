// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.json;

import java.lang.reflect.Type;
import java.time.LocalDate;

import com.appslandia.common.utils.DateUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 *
 * @author Loc Ha
 *
 */
public class GsonLocalDateSerializer extends GsonTemporalSerializer<LocalDate> {

  public GsonLocalDateSerializer() {
    this(DateUtils.ISO8601_DATE);
  }

  public GsonLocalDateSerializer(String serializeIsoPattern) {
    super(serializeIsoPattern);
  }

  @Override
  public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    return DateUtils.parseLocalDate(json.getAsString());
  }
}
