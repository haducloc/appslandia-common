// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.json;

import java.lang.reflect.Type;
import java.time.LocalDate;

import com.appslandia.common.utils.DateUtils;

import jakarta.json.bind.serializer.DeserializationContext;
import jakarta.json.stream.JsonParser;

/**
 *
 * @author Loc Ha
 *
 */
public class JsonbLocalDateSerializer extends JsonbTemporalSerializer<LocalDate> {

  public JsonbLocalDateSerializer() {
    this(DateUtils.ISO8601_DATE);
  }

  public JsonbLocalDateSerializer(String serializeIsoPattern) {
    super(serializeIsoPattern);
  }

  @Override
  public LocalDate deserialize(JsonParser parser, DeserializationContext ctx, Type rtType) {
    return DateUtils.parseLocalDate(parser.getString());
  }
}
