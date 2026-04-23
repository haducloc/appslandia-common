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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import com.appslandia.common.base.DestroyingException;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.JsonbException;
import jakarta.json.bind.config.BinaryDataStrategy;
import jakarta.json.bind.config.PropertyNamingStrategy;
import jakarta.json.bind.config.PropertyVisibilityStrategy;

/**
 *
 * @author Loc Ha
 *
 */
public class JsonbProcessor extends JsonProcessor {

  private Jsonb jsonb;
  private JsonbConfig config;

  @Override
  protected void init() throws Exception {
    if (config == null) {
      config = newConfig(true, false);
    }
    jsonb = JsonbBuilder.create(config);
  }

  @Override
  public void destroy() throws DestroyingException {
    if (jsonb != null) {
      try {
        jsonb.close();
      } catch (Exception ex) {
        throw new DestroyingException(ex.getMessage(), ex);
      }
    }
  }

  @Override
  public void write(Writer out, Object obj) throws JsonException {
    initialize();
    try {
      jsonb.toJson(obj, out);
    } catch (JsonbException ex) {
      throw new JsonException(ex);
    }
  }

  @Override
  public <T> T read(Reader reader, Class<T> resultClass) throws JsonException {
    initialize();
    try {
      return jsonb.fromJson(reader, resultClass);
    } catch (JsonbException ex) {
      throw new JsonException(ex);
    }
  }

  @Override
  public <T> T read(Reader reader, Type type) throws JsonException {
    initialize();
    try {
      return jsonb.fromJson(reader, type);
    } catch (JsonbException ex) {
      throw new JsonException(ex);
    }
  }

  public JsonbProcessor setConfig(JsonbConfig config) {
    assertNotInitialized();
    this.config = config;
    return this;
  }

  public static JsonbConfig newConfig(boolean serializeNulls, boolean prettyPrinting) {
    var config = new JsonbConfig();
    config.withNullValues(serializeNulls);
    config.withFormatting(prettyPrinting);
    config.withPropertyNamingStrategy(PropertyNamingStrategy.IDENTITY);

    config.withPropertyVisibilityStrategy(new PropertyVisibilityStrategy() {

      @Override
      public boolean isVisible(Field field) {
        if (field.getDeclaredAnnotation(JsonIgnore.class) != null) {
          return false;
        }
        return true;
      }

      @Override
      public boolean isVisible(Method method) {
        return false;
      }
    });
    config.withBinaryDataStrategy(BinaryDataStrategy.BASE_64_URL);

    // Adapter for JsonMap
    config.withAdapters(new JsonbMapAdapter<>(m -> new JsonMap(m)) {
    }.setMapConverter(m -> new JsonMap(m)));
    return config;
  }
}
