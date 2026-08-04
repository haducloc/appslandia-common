// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.json;

import java.lang.reflect.Type;
import java.time.OffsetDateTime;

import com.appslandia.common.utils.DateUtils;

import jakarta.json.bind.serializer.DeserializationContext;
import jakarta.json.stream.JsonParser;

/**
 *
 * @author Loc Ha
 *
 */
public class JsonbOffsetDateTimeSerializer extends JsonbTemporalSerializer<OffsetDateTime> {

  public JsonbOffsetDateTimeSerializer() {
    this(DateUtils.ISO8601_DATETIMEZ_F3);
  }

  public JsonbOffsetDateTimeSerializer(String serializeIsoPattern) {
    super(serializeIsoPattern);
  }

  @Override
  public OffsetDateTime deserialize(JsonParser parser, DeserializationContext ctx, Type rtType) {
    return DateUtils.parseOffsetDateTime(parser.getString());
  }
}
