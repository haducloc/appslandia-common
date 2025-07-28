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

package com.appslandia.common.base;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class SimpleConfig implements Config {

  protected final Map<String, String> cfg;

  public SimpleConfig() {
    this(new HashMap<>());
  }

  public SimpleConfig(Map<String, String> newCfg) {
    this.cfg = newCfg;
  }

  @Override
  public Iterator<String> getKeys() {
    return new UnmodifiableIterator<>(cfg.keySet().iterator());
  }

  @Override
  public String getString(String key) {
    Arguments.notNull(key);

    return this.cfg.get(key);
  }

  public SimpleConfig putIfAbsent(String key, String value) {
    Arguments.notNull(key);
    Arguments.notNull(value);

    this.cfg.putIfAbsent(key, value.strip());
    return this;
  }

  public SimpleConfig set(String key, String value) {
    Arguments.notNull(key);
    Arguments.notNull(value);

    this.cfg.put(key, value.strip());
    return this;
  }

  public SimpleConfig set(String key, boolean value) {
    Arguments.notNull(key);

    this.cfg.put(key, Boolean.toString(value));
    return this;
  }

  public SimpleConfig set(String key, int value) {
    Arguments.notNull(key);

    this.cfg.put(key, Integer.toString(value));
    return this;
  }

  public SimpleConfig set(String key, long value) {
    Arguments.notNull(key);

    this.cfg.put(key, Long.toString(value));
    return this;
  }

  public SimpleConfig set(String key, double value) {
    Arguments.notNull(key);

    this.cfg.put(key, Double.toString(value));
    return this;
  }

  public SimpleConfig set(String key, BigDecimal value) {
    Arguments.notNull(key);
    Arguments.notNull(value);

    this.cfg.put(key, value.toPlainString());
    return this;
  }

  public void load(Reader reader) throws IOException {
    var props = new Properties();
    props.load(reader);

    fromProperties(props);
  }

  public void load(InputStream inStream) throws IOException {
    var props = new Properties();
    props.load(inStream);

    fromProperties(props);
  }

  public void store(Writer reader, String comments) throws IOException {
    toProperties().store(reader, comments);
  }

  public void store(OutputStream outStream, String comments) throws IOException {
    toProperties().store(outStream, comments);
  }

  protected void fromProperties(Properties props) {
    for (Entry<Object, Object> prop : props.entrySet()) {
      this.cfg.put((String) prop.getKey(), ((String) prop.getValue()).strip());
    }
  }

  protected Properties toProperties() {
    var props = new Properties(this.cfg.size());
    props.putAll(this.cfg);
    return props;
  }

  @Override
  public String toString() {
    try {
      var out = new StringOutput(this.cfg.size() * 64);
      store(out, getClass().getName());

      return out.toString();
    } catch (IOException ex) {
      throw new Error(ex);
    }
  }
}
