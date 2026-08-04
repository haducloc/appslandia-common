// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.json;

import java.time.temporal.Temporal;

import com.appslandia.common.utils.DateUtils;

import jakarta.json.bind.serializer.JsonbDeserializer;
import jakarta.json.bind.serializer.JsonbSerializer;
import jakarta.json.bind.serializer.SerializationContext;
import jakarta.json.stream.JsonGenerator;

/**
 *
 * @author Loc Ha
 *
 */
public abstract class JsonbTemporalSerializer<T extends Temporal> extends JsonTemporalSerializer
    implements JsonbSerializer<T>, JsonbDeserializer<T> {

  public JsonbTemporalSerializer(String serializeIsoPattern) {
    super(serializeIsoPattern);
  }

  @Override
  public void serialize(T obj, JsonGenerator generator, SerializationContext ctx) {
    var formattedValue = DateUtils.getFormatter(serializeIsoPattern).format(obj);
    generator.write(formattedValue);
  }
}
