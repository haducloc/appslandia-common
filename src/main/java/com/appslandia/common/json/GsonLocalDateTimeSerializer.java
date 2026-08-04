// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.json;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

import com.appslandia.common.utils.DateUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 *
 * @author Loc Ha
 *
 */
public class GsonLocalDateTimeSerializer extends GsonTemporalSerializer<LocalDateTime> {

  public GsonLocalDateTimeSerializer() {
    this(DateUtils.ISO8601_DATETIME_F3);
  }

  public GsonLocalDateTimeSerializer(String serializeIsoPattern) {
    super(serializeIsoPattern);
  }

  @Override
  public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    return DateUtils.parseLocalDateTime(json.getAsString());
  }
}
