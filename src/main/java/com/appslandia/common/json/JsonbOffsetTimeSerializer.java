// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.json;

import java.lang.reflect.Type;
import java.time.OffsetTime;

import com.appslandia.common.utils.DateUtils;

import jakarta.json.bind.serializer.DeserializationContext;
import jakarta.json.stream.JsonParser;

/**
 *
 * @author Loc Ha
 *
 */
public class JsonbOffsetTimeSerializer extends JsonbTemporalSerializer<OffsetTime> {

  public JsonbOffsetTimeSerializer() {
    this(DateUtils.ISO8601_TIMEZ_F3);
  }

  public JsonbOffsetTimeSerializer(String serializeIsoPattern) {
    super(serializeIsoPattern);
  }

  @Override
  public OffsetTime deserialize(JsonParser parser, DeserializationContext ctx, Type rtType) {
    return DateUtils.parseOffsetTime(parser.getString());
  }
}
