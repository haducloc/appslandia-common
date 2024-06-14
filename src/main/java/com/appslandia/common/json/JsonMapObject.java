// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

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
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.appslandia.common.base.MapWrapper;
import com.appslandia.common.base.Unsupported;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.STR;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JsonMapObject extends MapWrapper<String, Object> {
  private static final long serialVersionUID = 1L;

  public JsonMapObject() {
    super(new LinkedHashMap<>());
  }

  public JsonMapObject(Map<String, Object> map) {
    super(map);
  }

  public JsonMapObject set(String key, Object value) {
    Asserts.notNull(key);
    Asserts.isTrue(isValueSupported(value), "The value is unsuppored.");

    super.put(key, value);
    return this;
  }

  public Object getReq(String key) {
    Object value = this.get(key);
    return Asserts.notNull(value, "The value is required.");
  }

  public String getStringReq(String key) {
    Object value = this.getReq(key);
    if (value.getClass() == String.class) {
      return (String) value;
    }
    throw new JsonValueException(STR.fmt("Failed to getStringReq('{}')", key));
  }

  public boolean getBool(String key) {
    Object value = this.getReq(key);
    if (value.getClass() == Boolean.class) {
      return (Boolean) value;
    }
    throw new JsonValueException(STR.fmt("Failed to getBool('{}')", key));
  }

  public int getInt(String key) {
    Object value = this.getReq(key);
    if (value.getClass() == Integer.class) {
      return (Integer) value;
    }
    if (value instanceof Number) {
      return ((Number) value).intValue();
    }
    throw new JsonValueException(STR.fmt("Failed to getInt('{}')", key));
  }

  public long getLong(String key) {
    Object value = this.getReq(key);
    if (value.getClass() == Long.class) {
      return (Long) value;
    }
    if (value instanceof Number) {
      return ((Number) value).longValue();
    }
    throw new JsonValueException(STR.fmt("Failed to getLong('{}')", key));
  }

  public double getDouble(String key) {
    Object value = this.getReq(key);
    if (value.getClass() == Double.class) {
      return (Double) value;
    }
    if (value instanceof Number) {
      return ((Number) value).doubleValue();
    }
    throw new JsonValueException(STR.fmt("Failed to getDouble('{}')", key));
  }

  public BigDecimal getDecimalReq(String key) {
    Object value = this.getReq(key);
    if (value.getClass() == BigDecimal.class) {
      return (BigDecimal) value;
    }
    if (value instanceof Number) {
      return new BigDecimal(value.toString());
    }
    throw new JsonValueException(STR.fmt("Failed to getDecimalReq('{}')", key));
  }

  public LocalDate getLocalDateReq(String key) {
    Object value = this.getReq(key);
    if (value.getClass() == LocalDate.class) {
      return (LocalDate) value;
    }
    if (value.getClass() == String.class) {
      return DateUtils.parseLocalDate((String) value);
    }
    throw new JsonValueException(STR.fmt("Failed to getLocalDateReq('{}')", key));
  }

  public LocalTime getLocalTimeReq(String key) {
    Object value = this.getReq(key);
    if (value.getClass() == LocalTime.class) {
      return (LocalTime) value;
    }
    if (value.getClass() == String.class) {
      return DateUtils.parseLocalTime((String) value);
    }
    throw new JsonValueException(STR.fmt("Failed to getLocalTimeReq('{}')", key));
  }

  public LocalDateTime getLocalDateTimeReq(String key) {
    Object value = this.getReq(key);
    if (value.getClass() == LocalDateTime.class) {
      return (LocalDateTime) value;
    }
    if (value.getClass() == String.class) {
      return DateUtils.parseLocalDateTime((String) value);
    }
    throw new JsonValueException(STR.fmt("Failed to getLocalDateTimeReq('{}')", key));
  }

  public OffsetTime getOffsetTimeReq(String key) {
    Object value = this.getReq(key);
    if (value.getClass() == OffsetTime.class) {
      return (OffsetTime) value;
    }
    if (value.getClass() == String.class) {
      return DateUtils.parseOffsetTime((String) value);
    }
    throw new JsonValueException(STR.fmt("Failed to getOffsetTimeReq('{}')", key));
  }

  public OffsetDateTime getOffsetDateTimeReq(String key) {
    Object value = this.getReq(key);
    if (value.getClass() == OffsetDateTime.class) {
      return (OffsetDateTime) value;
    }
    if (value.getClass() == String.class) {
      return DateUtils.parseOffsetDateTime((String) value);
    }
    throw new JsonValueException(STR.fmt("Failed to getOffsetDateTimeReq('{}')", key));
  }

  public YearMonth getYearMonthReq(String key) {
    Object value = this.getReq(key);
    if (value.getClass() == YearMonth.class) {
      return (YearMonth) value;
    }
    if (value.getClass() == String.class) {
      return DateUtils.parseYearMonth((String) value);
    }
    throw new JsonValueException(STR.fmt("Failed to getYearMonthReq('{}')", key));
  }

  public String getString(String key) {
    Object value = this.get(key);
    if (value == null || value.getClass() == String.class) {
      return (String) value;
    }
    throw new JsonValueException(STR.fmt("Failed to getString('{}')", key));
  }

  public Boolean getBoolOpt(String key) {
    Object value = this.get(key);
    if (value == null || value.getClass() == Boolean.class) {
      return (Boolean) value;
    }
    throw new JsonValueException(STR.fmt("Failed to getBoolOpt('{}')", key));
  }

  public Integer getIntOpt(String key) {
    Object value = this.get(key);
    if (value == null || value.getClass() == Integer.class) {
      return (Integer) value;
    }
    if (value instanceof Number) {
      return ((Number) value).intValue();
    }
    throw new JsonValueException(STR.fmt("Failed to getIntOpt('{}')", key));
  }

  public Long getLongOpt(String key) {
    Object value = this.get(key);
    if (value == null || value.getClass() == Long.class) {
      return (Long) value;
    }
    if (value instanceof Number) {
      return ((Number) value).longValue();
    }
    throw new JsonValueException(STR.fmt("Failed to getLongOpt('{}')", key));
  }

  public Double getDoubleOpt(String key) {
    Object value = this.get(key);
    if (value == null || value.getClass() == Double.class) {
      return (Double) value;
    }
    if (value instanceof Number) {
      return ((Number) value).doubleValue();
    }
    throw new JsonValueException(STR.fmt("Failed to getDoubleOpt('{}')", key));
  }

  public BigDecimal getDecimal(String key) {
    Object value = this.get(key);
    if (value == null || value.getClass() == BigDecimal.class) {
      return (BigDecimal) value;
    }
    if (value instanceof Number) {
      return new BigDecimal(value.toString());
    }
    throw new JsonValueException(STR.fmt("Failed to getDecimal('{}')", key));
  }

  public LocalDate getLocalDate(String key) {
    Object value = this.get(key);
    if (value == null || value.getClass() == LocalDate.class) {
      return (LocalDate) value;
    }
    if (value.getClass() == String.class) {
      return DateUtils.parseLocalDate((String) value);
    }
    throw new JsonValueException(STR.fmt("Failed to getLocalDate('{}')", key));
  }

  public LocalTime getLocalTime(String key) {
    Object value = this.get(key);
    if (value == null || value.getClass() == LocalTime.class) {
      return (LocalTime) value;
    }
    if (value.getClass() == String.class) {
      return DateUtils.parseLocalTime((String) value);
    }
    throw new JsonValueException(STR.fmt("Failed to getLocalTime('{}')", key));
  }

  public LocalDateTime getLocalDateTime(String key) {
    Object value = this.get(key);
    if (value == null || value.getClass() == LocalDateTime.class) {
      return (LocalDateTime) value;
    }
    if (value.getClass() == String.class) {
      return DateUtils.parseLocalDateTime((String) value);
    }
    throw new JsonValueException(STR.fmt("Failed to getLocalDateTime('{}')", key));
  }

  public OffsetTime getOffsetTime(String key) {
    Object value = this.get(key);
    if (value == null || value.getClass() == OffsetTime.class) {
      return (OffsetTime) value;
    }
    if (value.getClass() == String.class) {
      return DateUtils.parseOffsetTime((String) value);
    }
    throw new JsonValueException(STR.fmt("Failed to getOffsetTime('{}')", key));
  }

  public OffsetDateTime getOffsetDateTime(String key) {
    Object value = this.get(key);
    if (value == null || value.getClass() == OffsetDateTime.class) {
      return (OffsetDateTime) value;
    }
    if (value.getClass() == String.class) {
      return DateUtils.parseOffsetDateTime((String) value);
    }
    throw new JsonValueException(STR.fmt("Failed to getOffsetDateTime('{}')", key));
  }

  public YearMonth getYearMonth(String key) {
    Object value = this.get(key);
    if (value == null || value.getClass() == YearMonth.class) {
      return (YearMonth) value;
    }
    if (value.getClass() == String.class) {
      return DateUtils.parseYearMonth((String) value);
    }
    throw new JsonValueException(STR.fmt("Failed to getYearMonth('{}')", key));
  }

  protected boolean isValueSupported(Object value) {
    if (value == null) {
      return true;
    }

    // Basic Types
    if (isBasicType(value.getClass())) {
      return true;
    }

    // List
    if (value instanceof List) {
      return validateList((List<?>) value);
    }

    // Map
    if (value instanceof Map) {
      return validateMap((Map<?, ?>) value);
    }
    return false;
  }

  protected boolean isBasicType(Class<?> type) {
    return (type == String.class) || Number.class.isAssignableFrom(type) || (type == Boolean.class)
        || (type == LocalDate.class) || (type == LocalTime.class) || (type == LocalDateTime.class)
        || (type == OffsetTime.class) || (type == OffsetDateTime.class) || (type == YearMonth.class);
  }

  private boolean validateMap(Map<?, ?> map) {
    return map.entrySet().stream().allMatch(entry -> {
      return (entry.getKey() instanceof String) && ((entry.getValue() == null) || isValueSupported(entry.getValue()));
    });
  }

  private boolean validateList(List<?> list) {
    return list.stream().allMatch(value -> (value == null) || isValueSupported(value));
  }

  @Override
  public Object putIfAbsent(String key, Object value) {
    Asserts.notNull(key);
    Asserts.isTrue(isValueSupported(value), "The value is unsuppored.");

    return super.putIfAbsent(key, value);
  }

  // UnsupportedOperationException

  @Unsupported
  @Override
  public Object put(String key, Object value) {
    throw new UnsupportedOperationException("Use set(key, value) instead.");
  }

  @Unsupported
  @Override
  public void putAll(Map<? extends String, ? extends Object> m) {
    throw new UnsupportedOperationException();
  }

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
