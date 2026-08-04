// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.json;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;

import com.appslandia.common.base.DestroyingException;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ToNumberPolicy;

/**
 *
 * @author Loc Ha
 *
 */
public class GsonProcessor extends JsonProcessor {

  private Gson gson;
  private GsonBuilder builder;

  @Override
  protected void init() throws Exception {
    if (builder == null) {
      builder = newBuilder(true, false);
    }
    gson = builder.create();
  }

  @Override
  public void destroy() throws DestroyingException {
  }

  @Override
  public void write(Writer out, Object obj) throws JsonException {
    initialize();
    try {
      gson.toJson(obj, out);
    } catch (JsonIOException ex) {
      throw new JsonException(ex);
    }
  }

  @Override
  public <T> T read(Reader reader, Class<T> resultClass) throws JsonException {
    initialize();
    try {
      return gson.fromJson(reader, resultClass);
    } catch (JsonSyntaxException | JsonIOException ex) {
      throw new JsonException(ex);
    }
  }

  @Override
  public <T> T read(Reader reader, Type type) throws JsonException {
    initialize();
    try {
      return gson.fromJson(reader, type);
    } catch (JsonSyntaxException | JsonIOException ex) {
      throw new JsonException(ex);
    }
  }

  public GsonProcessor setBuilder(GsonBuilder builder) {
    assertNotInitialized();
    this.builder = builder;
    return this;
  }

  public static GsonBuilder newBuilder(boolean serializeNulls, boolean prettyPrinting) {
    var builder = new GsonBuilder();

    if (serializeNulls) {
      builder.serializeNulls();
    }

    if (prettyPrinting) {
      builder.setPrettyPrinting();
    }

    builder.setObjectToNumberStrategy(ToNumberPolicy.BIG_DECIMAL);
    builder.setFieldNamingStrategy(FieldNamingPolicy.IDENTITY);

    builder.setExclusionStrategies(new ExclusionStrategy() {

      @Override
      public boolean shouldSkipField(FieldAttributes attrs) {
        return attrs.getAnnotation(JsonIgnore.class) != null;
      }

      @Override
      public boolean shouldSkipClass(Class<?> clazz) {
        return clazz.getDeclaredAnnotation(JsonIgnore.class) != null;
      }
    });

    // Java8 Date/Time
    builder.registerTypeAdapter(LocalDate.class, new GsonLocalDateSerializer());
    builder.registerTypeAdapter(LocalTime.class, new GsonLocalTimeSerializer());
    builder.registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeSerializer());

    builder.registerTypeAdapter(OffsetDateTime.class, new GsonOffsetDateTimeSerializer());
    builder.registerTypeAdapter(OffsetTime.class, new GsonOffsetTimeSerializer());

    // Adapter for JsonMap
    builder.registerTypeAdapter(JsonMap.class,
        new GsonMapAdapter<>(m -> new JsonMap(m)).setMapConverter(m -> new JsonMap(m)));

    return builder;
  }
}
