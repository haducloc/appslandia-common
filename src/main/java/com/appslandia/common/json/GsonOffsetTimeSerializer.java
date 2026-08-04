// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.json;

import java.lang.reflect.Type;
import java.time.OffsetTime;

import com.appslandia.common.utils.DateUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 *
 * @author Loc Ha
 *
 */
public class GsonOffsetTimeSerializer extends GsonTemporalSerializer<OffsetTime> {

  public GsonOffsetTimeSerializer() {
    this(DateUtils.ISO8601_TIMEZ_F3);
  }

  public GsonOffsetTimeSerializer(String serializeIsoPattern) {
    super(serializeIsoPattern);
  }

  @Override
  public OffsetTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    return DateUtils.parseOffsetTime(json.getAsString());
  }
}
