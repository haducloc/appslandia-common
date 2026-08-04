// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
