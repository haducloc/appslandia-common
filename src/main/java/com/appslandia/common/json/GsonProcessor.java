// The MIT License (MIT)
// Copyright © 2015 Loc Ha

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.appslandia.common.json;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;

import com.appslandia.common.base.DestroyException;
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
    if (this.builder == null) {
      this.builder = newBuilder(true, false);
    }
    this.gson = this.builder.create();
  }

  @Override
  public void destroy() throws DestroyException {
  }

  @Override
  public void write(Writer out, Object obj) throws JsonException {
    this.initialize();
    try {
      this.gson.toJson(obj, out);
    } catch (JsonIOException ex) {
      throw new JsonException(ex);
    }
  }

  @Override
  public <T> T read(Reader reader, Class<T> resultClass) throws JsonException {
    this.initialize();
    try {
      return this.gson.fromJson(reader, resultClass);
    } catch (JsonSyntaxException | JsonIOException ex) {
      throw new JsonException(ex);
    }
  }

  @Override
  public <T> T read(Reader reader, Type type) throws JsonException {
    this.initialize();
    try {
      return this.gson.fromJson(reader, type);
    } catch (JsonSyntaxException | JsonIOException ex) {
      throw new JsonException(ex);
    }
  }

  public GsonProcessor setBuilder(GsonBuilder builder) {
    this.assertNotInitialized();
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

    builder.setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE);
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

    // Adapter for JsonMapObject
    builder.registerTypeAdapter(JsonMapObject.class,
        new GsonMapAdapter<>(m -> new JsonMapObject(m)).setMapConverter(m -> new JsonMapObject(m)));

    return builder;
  }
}
