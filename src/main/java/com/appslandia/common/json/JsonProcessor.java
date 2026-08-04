// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.json;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import com.appslandia.common.base.DestroyingSupport;
import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.base.MemoryStream;
import com.appslandia.common.base.StringOutput;
import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author Loc Ha
 *
 */
public abstract class JsonProcessor extends InitializingObject implements DestroyingSupport {

  public abstract void write(Writer out, Object obj) throws JsonException;

  public abstract <T> T read(Reader reader, Class<T> resultClass) throws JsonException;

  public abstract <T> T read(Reader reader, Type type) throws JsonException;

  public <V> Map<String, V> readAsMap(Reader reader) throws JsonException {
    return ObjectUtils.cast(read(reader, LinkedHashMap.class));
  }

  public <T> T read(String jsonString, Class<T> resultClass) throws JsonException {
    return read(new StringReader(jsonString), resultClass);
  }

  public <T> T read(String jsonString, Type type) throws JsonException {
    return read(new StringReader(jsonString), type);
  }

  public String toString(Object obj) throws JsonException {
    var out = new StringOutput(512);
    write(out, obj);
    return out.toString();
  }

  public byte[] toByteArray(Object obj) throws JsonException {
    var content = new MemoryStream(512);
    try (var out = new OutputStreamWriter(content, StandardCharsets.UTF_8)) {

      write(out, obj);
    } catch (IOException ex) {
      throw new JsonException(ex);
    }
    return content.toByteArray();
  }
}
