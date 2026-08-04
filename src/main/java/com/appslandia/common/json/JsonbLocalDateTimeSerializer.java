// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.json;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

import com.appslandia.common.utils.DateUtils;

import jakarta.json.bind.serializer.DeserializationContext;
import jakarta.json.stream.JsonParser;

/**
 *
 * @author Loc Ha
 *
 */
public class JsonbLocalDateTimeSerializer extends JsonbTemporalSerializer<LocalDateTime> {

  public JsonbLocalDateTimeSerializer() {
    this(DateUtils.ISO8601_DATETIME_F3);
  }

  public JsonbLocalDateTimeSerializer(String serializeIsoPattern) {
    super(serializeIsoPattern);
  }

  @Override
  public LocalDateTime deserialize(JsonParser parser, DeserializationContext ctx, Type rtType) {
    return DateUtils.parseLocalDateTime(parser.getString());
  }
}
