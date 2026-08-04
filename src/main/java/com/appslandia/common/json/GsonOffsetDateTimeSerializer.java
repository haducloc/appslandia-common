// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.json;

import java.lang.reflect.Type;
import java.time.OffsetDateTime;

import com.appslandia.common.utils.DateUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 *
 * @author Loc Ha
 *
 */
public class GsonOffsetDateTimeSerializer extends GsonTemporalSerializer<OffsetDateTime> {

  public GsonOffsetDateTimeSerializer() {
    this(DateUtils.ISO8601_DATETIMEZ_F3);
  }

  public GsonOffsetDateTimeSerializer(String serializeIsoPattern) {
    super(serializeIsoPattern);
  }

  @Override
  public OffsetDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    return DateUtils.parseOffsetDateTime(json.getAsString());
  }
}
