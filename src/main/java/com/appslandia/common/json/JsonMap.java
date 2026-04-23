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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.temporal.Temporal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.appslandia.common.base.MapWrapper;
import com.appslandia.common.base.Unsupported;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class JsonMap extends MapWrapper<String, Object> {
  private static final long serialVersionUID = 1L;

  public JsonMap() {
    super(new LinkedHashMap<>());
  }

  public JsonMap(Map<String, Object> map) {
    super(map);
  }

  public JsonMap set(String key, Object value) {
    Arguments.notNull(key);
    validateValue(value);

    super.put(key, value);
    return this;
  }

  public Object getReq(String key) {
    return JsonMapUtils.getReq(this.map, key);
  }

  public JsonMap getJsonMapReq(String key) {
    return JsonMapUtils.getJsonMapReq(this.map, key);
  }

  public String getStringReq(String key) {
    return JsonMapUtils.getStringReq(this.map, key);
  }

  public boolean getBool(String key) {
    return JsonMapUtils.getBool(this.map, key);
  }

  public int getInt(String key) {
    return JsonMapUtils.getInt(this.map, key);
  }

  public long getLong(String key) {
    return JsonMapUtils.getLong(this.map, key);
  }

  public double getDouble(String key) {
    return JsonMapUtils.getDouble(this.map, key);
  }

  public BigDecimal getDecimalReq(String key) {
    return JsonMapUtils.getDecimalReq(this.map, key);
  }

  public LocalDate getLocalDateReq(String key) {
    return JsonMapUtils.getLocalDateReq(this.map, key);
  }

  public LocalTime getLocalTimeReq(String key) {
    return JsonMapUtils.getLocalTimeReq(this.map, key);
  }

  public LocalDateTime getLocalDateTimeReq(String key) {
    return JsonMapUtils.getLocalDateTimeReq(this.map, key);
  }

  public OffsetTime getOffsetTimeReq(String key) {
    return JsonMapUtils.getOffsetTimeReq(this.map, key);
  }

  public OffsetDateTime getOffsetDateTimeReq(String key) {
    return JsonMapUtils.getOffsetDateTimeReq(this.map, key);
  }

  public JsonMap getJsonMap(String key) {
    return JsonMapUtils.getJsonMap(this.map, key);
  }

  public String getString(String key) {
    return JsonMapUtils.getString(this.map, key);
  }

  public Boolean getBoolOpt(String key) {
    return JsonMapUtils.getBoolOpt(this.map, key);
  }

  public Integer getIntOpt(String key) {
    return JsonMapUtils.getIntOpt(this.map, key);
  }

  public Long getLongOpt(String key) {
    return JsonMapUtils.getLongOpt(this.map, key);
  }

  public Double getDoubleOpt(String key) {
    return JsonMapUtils.getDoubleOpt(this.map, key);
  }

  public BigDecimal getDecimal(String key) {
    return JsonMapUtils.getDecimal(this.map, key);
  }

  public LocalDate getLocalDate(String key) {
    return JsonMapUtils.getLocalDate(this.map, key);
  }

  public LocalTime getLocalTime(String key) {
    return JsonMapUtils.getLocalTime(this.map, key);
  }

  public LocalDateTime getLocalDateTime(String key) {
    return JsonMapUtils.getLocalDateTime(this.map, key);
  }

  public OffsetTime getOffsetTime(String key) {
    return JsonMapUtils.getOffsetTime(this.map, key);
  }

  public OffsetDateTime getOffsetDateTime(String key) {
    return JsonMapUtils.getOffsetDateTime(this.map, key);
  }

  protected void validateValue(Object value) throws IllegalArgumentException {
    if ((value == null) || isBasicValue(value)) {
      return;
    }
    if (value instanceof List) {
      validateList((List<?>) value);
      return;
    }
    if (value instanceof Map) {
      validateMap((Map<?, ?>) value);
      return;
    }
    throw new IllegalArgumentException("Unsupported JSON value type: " + value.getClass());
  }

  protected boolean isBasicValue(Object value) {
    return value instanceof String || value instanceof Number || value instanceof Boolean || value instanceof Temporal
        || value instanceof Character || value instanceof Enum<?>;
  }

  private void validateMap(Map<?, ?> map) {
    if (map instanceof JsonMap) {
      return;
    }
    map.forEach((key, value) -> {
      Arguments.isTrue(key instanceof String, "Unsupported JSON key type: " + ObjectUtils.getClass(key));

      validateValue(value);
    });
  }

  private void validateList(List<?> list) {
    list.forEach(value -> {
      validateValue(value);
    });
  }

  @Override
  public Object putIfAbsent(String key, Object value) {
    Arguments.notNull(key);
    validateValue(value);

    return super.putIfAbsent(key, value);
  }

  @Override
  public Object put(String key, Object value) {
    Arguments.notNull(key);
    validateValue(value);

    return super.put(key, value);
  }

  @Override
  public void putAll(Map<? extends String, ? extends Object> map) {
    for (Entry<? extends String, ? extends Object> entry : map.entrySet()) {

      validateValue(entry.getValue());
      super.put(entry.getKey(), entry.getValue());
    }
  }

  // UnsupportedOperationException

  @Unsupported
  @Override
  public Object compute(String key, BiFunction<? super String, ? super Object, ? extends Object> remappingFunction) {
    throw new UnsupportedOperationException();
  }

  @Unsupported
  @Override
  public Object computeIfAbsent(String key, Function<? super String, ? extends Object> mappingFunction) {
    throw new UnsupportedOperationException();
  }

  @Unsupported
  @Override
  public Object computeIfPresent(String key,
      BiFunction<? super String, ? super Object, ? extends Object> remappingFunction) {
    throw new UnsupportedOperationException();
  }

  @Unsupported
  @Override
  public Object merge(String key, Object value,
      BiFunction<? super Object, ? super Object, ? extends Object> remappingFunction) {
    throw new UnsupportedOperationException();
  }

  @Unsupported
  @Override
  public boolean replace(String key, Object oldValue, Object newValue) {
    throw new UnsupportedOperationException();
  }

  @Unsupported
  @Override
  public Object replace(String key, Object value) {
    throw new UnsupportedOperationException();
  }

  @Unsupported
  @Override
  public void replaceAll(BiFunction<? super String, ? super Object, ? extends Object> function) {
    throw new UnsupportedOperationException();
  }

  @Unsupported
  @Override
  public boolean remove(Object key, Object value) {
    throw new UnsupportedOperationException();
  }
}
