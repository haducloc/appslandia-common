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

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.base.MemoryStream;
import com.appslandia.common.base.StringOutput;
import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author Loc Ha
 *
 */
public abstract class JsonProcessor extends InitializeObject {

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
