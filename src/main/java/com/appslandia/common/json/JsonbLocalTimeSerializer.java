// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.json;

import java.lang.reflect.Type;
import java.time.LocalTime;

import com.appslandia.common.utils.DateUtils;

import jakarta.json.bind.serializer.DeserializationContext;
import jakarta.json.stream.JsonParser;

/**
 *
 * @author Loc Ha
 *
 */
public class JsonbLocalTimeSerializer extends JsonbTemporalSerializer<LocalTime> {

  public JsonbLocalTimeSerializer() {
    this(DateUtils.ISO8601_TIME_F3);
  }

  public JsonbLocalTimeSerializer(String serializeIsoPattern) {
    super(serializeIsoPattern);
  }

  @Override
  public LocalTime deserialize(JsonParser parser, DeserializationContext ctx, Type rtType) {
    return DateUtils.parseLocalTime(parser.getString());
  }
}
