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
import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.common.utils.STR;

/**
 *
 * @author Loc Ha
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
    Arguments.notNull(key);
    validateValue(value);

    super.put(key, value);
    return this;
  }

  public Object getReq(String key) {
    var value = this.get(key);
    if (value == null) {
      throw new IllegalStateException("The value is required.");
    }
    return value;
  }

  public JsonMapObject getJsonMapReq(String key) {
    var value = this.getReq(key);
    if (value instanceof JsonMapObject jmo) {
      return jmo;
    }
    throw new JsonValueException(STR.fmt("Failed to getJsonMapReq('{}')", key));
  }

  public String getStringReq(String key) {
    var value = this.getReq(key);
    if (value instanceof String str) {
      return str;
    }
    throw new JsonValueException(STR.fmt("Failed to getStringReq('{}')", key));
  }

  public boolean getBool(String key) {
    var value = this.getReq(key);
    if (value instanceof Boolean b) {
      return b;
    }
    throw new JsonValueException(STR.fmt("Failed to getBool('{}')", key));
  }

  public int getInt(String key) {
    var value = this.getReq(key);
    if (value instanceof Number num) {
      return num.intValue();
    }
    throw new JsonValueException(STR.fmt("Failed to getInt('{}')", key));
  }

  public long getLong(String key) {
    var value = this.getReq(key);
    if (value instanceof Number num) {
      return num.longValue();
    }
    throw new JsonValueException(STR.fmt("Failed to getLong('{}')", key));
  }

  public double getDouble(String key) {
    var value = this.getReq(key);
    if (value instanceof Number num) {
      return num.doubleValue();
    }
    throw new JsonValueException(STR.fmt("Failed to getDouble('{}')", key));
  }

  public BigDecimal getDecimalReq(String key) {
    var value = this.getReq(key);
    if (value instanceof BigDecimal bd) {
      return bd;
    }
    if (value instanceof Number num) {
      return new BigDecimal(num.toString());
    }
    throw new JsonValueException(STR.fmt("Failed to getDecimalReq('{}')", key));
  }

  public LocalDate getLocalDateReq(String key) {
    var value = this.getReq(key);
    if (value instanceof LocalDate date) {
      return date;
    }
    if (value instanceof String str) {
      return DateUtils.parseLocalDate(str);
    }
    throw new JsonValueException(STR.fmt("Failed to getLocalDateReq('{}')", key));
  }

  public LocalTime getLocalTimeReq(String key) {
    var value = this.getReq(key);
    if (value instanceof LocalTime time) {
      return time;
    }
    if (value instanceof String str) {
      return DateUtils.parseLocalTime(str);
    }
    throw new JsonValueException(STR.fmt("Failed to getLocalTimeReq('{}')", key));
  }

  public LocalDateTime getLocalDateTimeReq(String key) {
    var value = this.getReq(key);
    if (value instanceof LocalDateTime dt) {
      return dt;
    }
    if (value instanceof String str) {
      return DateUtils.parseLocalDateTime(str);
    }
    throw new JsonValueException(STR.fmt("Failed to getLocalDateTimeReq('{}')", key));
  }

  public OffsetTime getOffsetTimeReq(String key) {
    var value = this.getReq(key);
    if (value instanceof OffsetTime ot) {
      return ot;
    }
    if (value instanceof String str) {
      return DateUtils.parseOffsetTime(str);
    }
    throw new JsonValueException(STR.fmt("Failed to getOffsetTimeReq('{}')", key));
  }

  public OffsetDateTime getOffsetDateTimeReq(String key) {
    var value = this.getReq(key);
    if (value instanceof OffsetDateTime odt) {
      return odt;
    }
    if (value instanceof String str) {
      return DateUtils.parseOffsetDateTime(str);
    }
    throw new JsonValueException(STR.fmt("Failed to getOffsetDateTimeReq('{}')", key));
  }

  public JsonMapObject getJsonMap(String key) {
    var value = this.get(key);
    if (value == null) {
      return null;
    }
    if (value instanceof JsonMapObject jmo) {
      return jmo;
    }
    throw new JsonValueException(STR.fmt("Failed to getJsonMap('{}')", key));
  }

  public String getString(String key) {
    var value = this.get(key);
    if (value == null) {
      return null;
    }
    if (value instanceof String str) {
      return str;
    }
    throw new JsonValueException(STR.fmt("Failed to getString('{}')", key));
  }

  public Boolean getBoolOpt(String key) {
    var value = this.get(key);
    if (value == null) {
      return null;
    }
    if (value instanceof Boolean b) {
      return b;
    }
    throw new JsonValueException(STR.fmt("Failed to getBoolOpt('{}')", key));
  }

  public Integer getIntOpt(String key) {
    var value = this.get(key);
    return switch (value) {
    case null -> null;
    case Integer i -> i;
    case Number num -> num.intValue();
    default -> throw new JsonValueException(STR.fmt("Failed to getIntOpt('{}')", key));
    };
  }

  public Long getLongOpt(String key) {
    var value = this.get(key);
    return switch (value) {
    case null -> null;
    case Long l -> l;
    case Number num -> num.longValue();
    default -> throw new JsonValueException(STR.fmt("Failed to getLongOpt('{}')", key));
    };
  }

  public Double getDoubleOpt(String key) {
    var value = this.get(key);
    return switch (value) {
    case null -> null;
    case Double d -> d;
    case Number num -> num.doubleValue();
    default -> throw new JsonValueException(STR.fmt("Failed to getDoubleOpt('{}')", key));
    };
  }

  public BigDecimal getDecimal(String key) {
    var value = this.get(key);
    return switch (value) {
    case null -> null;
    case BigDecimal bd -> bd;
    case Number num -> new BigDecimal(num.toString());
    default -> throw new JsonValueException(STR.fmt("Failed to getDecimal('{}')", key));
    };
  }

  public LocalDate getLocalDate(String key) {
    var value = this.get(key);
    return switch (value) {
    case null -> null;
    case LocalDate date -> date;
    case String str -> DateUtils.parseLocalDate(str);
    default -> throw new JsonValueException(STR.fmt("Failed to getLocalDate('{}')", key));
    };
  }

  public LocalTime getLocalTime(String key) {
    var value = this.get(key);
    return switch (value) {
    case null -> null;
    case LocalTime time -> time;
    case String str -> DateUtils.parseLocalTime(str);
    default -> throw new JsonValueException(STR.fmt("Failed to getLocalTime('{}')", key));
    };
  }

  public LocalDateTime getLocalDateTime(String key) {
    var value = this.get(key);
    return switch (value) {
    case null -> null;
    case LocalDateTime dt -> dt;
    case String str -> DateUtils.parseLocalDateTime(str);
    default -> throw new JsonValueException(STR.fmt("Failed to getLocalDateTime('{}')", key));
    };
  }

  public OffsetTime getOffsetTime(String key) {
    var value = this.get(key);
    return switch (value) {
    case null -> null;
    case OffsetTime ot -> ot;
    case String str -> DateUtils.parseOffsetTime(str);
    default -> throw new JsonValueException(STR.fmt("Failed to getOffsetTime('{}')", key));
    };
  }

  public OffsetDateTime getOffsetDateTime(String key) {
    var value = this.get(key);
    return switch (value) {
    case null -> null;
    case OffsetDateTime odt -> odt;
    case String str -> DateUtils.parseOffsetDateTime(str);
    default -> throw new JsonValueException(STR.fmt("Failed to getOffsetDateTime('{}')", key));
    };
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
    return value instanceof String || value instanceof Number || value instanceof Boolean || value instanceof Enum<?>
        || value instanceof Temporal;
  }

  private void validateMap(Map<?, ?> map) {
    if (map instanceof JsonMapObject) {
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
      Arguments.isTrue(entry.getKey() instanceof String,
          "Unsupported JSON key type: " + ObjectUtils.getClass(entry.getKey()));

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
