// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.json;

import java.lang.reflect.Type;
import java.time.temporal.Temporal;

import com.appslandia.common.utils.DateUtils;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 *
 * @author Loc Ha
 *
 */
public abstract class GsonTemporalSerializer<T extends Temporal> extends JsonTemporalSerializer
    implements JsonSerializer<T>, JsonDeserializer<T> {

  public GsonTemporalSerializer(String serializeIsoPattern) {
    super(serializeIsoPattern);
  }

  @Override
  public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(DateUtils.getFormatter(serializeIsoPattern).format(src));
  }
}
