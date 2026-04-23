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
import java.util.Map;

import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.STR;

/**
 *
 * @author Loc Ha
 *
 */
public class JsonMapUtils {

  public static Object getReq(Map<String, Object> jsonMap, String key) {
    var value = jsonMap.get(key);
    if (value == null) {
      throw new JsonValueException(STR.fmt("Failed to getReq('{}')", key));
    }
    return value;
  }

  public static JsonMap getJsonMapReq(Map<String, Object> jsonMap, String key) {
    var value = getReq(jsonMap, key);
    if (value instanceof JsonMap jm) {
      return jm;
    }
    throw new JsonValueException(STR.fmt("Failed to getJsonMapReq('{}')", key));
  }

  public static String getStringReq(Map<String, Object> jsonMap, String key) {
    var value = getReq(jsonMap, key);
    if (value instanceof String str) {
      return str;
    }
    throw new JsonValueException(STR.fmt("Failed to getStringReq('{}')", key));
  }

  public static boolean getBool(Map<String, Object> jsonMap, String key) {
    var value = getReq(jsonMap, key);
    if (value instanceof Boolean b) {
      return b;
    }
    throw new JsonValueException(STR.fmt("Failed to getBool('{}')", key));
  }

  public static int getInt(Map<String, Object> jsonMap, String key) {
    var value = getReq(jsonMap, key);
    try {
      return switch (value) {
      case Byte b -> b.intValue();
      case Short s -> s.intValue();
      case Integer i -> i;
      case Long l -> Math.toIntExact(l);
      case BigDecimal bd -> bd.intValueExact();
      default -> throw new JsonValueException(STR.fmt("Failed to getInt('{}')", key));
      };
    } catch (ArithmeticException ex) {
      throw new JsonValueException(STR.fmt("Failed to getInt('{}')", key));
    }
  }

  public static long getLong(Map<String, Object> jsonMap, String key) {
    var value = getReq(jsonMap, key);
    try {
      return switch (value) {
      case Byte b -> b.longValue();
      case Short s -> s.longValue();
      case Integer i -> i.longValue();
      case Long l -> l;
      case BigDecimal bd -> bd.longValueExact();
      default -> throw new JsonValueException(STR.fmt("Failed to getLong('{}')", key));
      };
    } catch (ArithmeticException ex) {
      throw new JsonValueException(STR.fmt("Failed to getLong('{}')", key));
    }
  }

  public static double getDouble(Map<String, Object> jsonMap, String key) {
    var value = getReq(jsonMap, key);
    if (value instanceof Number num) {
      return num.doubleValue();
    }
    throw new JsonValueException(STR.fmt("Failed to getDouble('{}')", key));
  }

  public static BigDecimal getDecimalReq(Map<String, Object> jsonMap, String key) {
    var value = getReq(jsonMap, key);
    if (value instanceof BigDecimal bd) {
      return bd;
    }
    if (value instanceof Number num) {
      return new BigDecimal(num.toString());
    }
    throw new JsonValueException(STR.fmt("Failed to getDecimalReq('{}')", key));
  }

  public static LocalDate getLocalDateReq(Map<String, Object> jsonMap, String key) {
    var value = getReq(jsonMap, key);
    if (value instanceof LocalDate date) {
      return date;
    }
    if (value instanceof String str) {
      return DateUtils.parseLocalDate(str);
    }
    throw new JsonValueException(STR.fmt("Failed to getLocalDateReq('{}')", key));
  }

  public static LocalTime getLocalTimeReq(Map<String, Object> jsonMap, String key) {
    var value = getReq(jsonMap, key);
    if (value instanceof LocalTime time) {
      return time;
    }
    if (value instanceof String str) {
      return DateUtils.parseLocalTime(str);
    }
    throw new JsonValueException(STR.fmt("Failed to getLocalTimeReq('{}')", key));
  }

  public static LocalDateTime getLocalDateTimeReq(Map<String, Object> jsonMap, String key) {
    var value = getReq(jsonMap, key);
    if (value instanceof LocalDateTime dt) {
      return dt;
    }
    if (value instanceof String str) {
      return DateUtils.parseLocalDateTime(str);
    }
    throw new JsonValueException(STR.fmt("Failed to getLocalDateTimeReq('{}')", key));
  }

  public static OffsetTime getOffsetTimeReq(Map<String, Object> jsonMap, String key) {
    var value = getReq(jsonMap, key);
    if (value instanceof OffsetTime ot) {
      return ot;
    }
    if (value instanceof String str) {
      return DateUtils.parseOffsetTime(str);
    }
    throw new JsonValueException(STR.fmt("Failed to getOffsetTimeReq('{}')", key));
  }

  public static OffsetDateTime getOffsetDateTimeReq(Map<String, Object> jsonMap, String key) {
    var value = getReq(jsonMap, key);
    if (value instanceof OffsetDateTime odt) {
      return odt;
    }
    if (value instanceof String str) {
      return DateUtils.parseOffsetDateTime(str);
    }
    throw new JsonValueException(STR.fmt("Failed to getOffsetDateTimeReq('{}')", key));
  }

  public static JsonMap getJsonMap(Map<String, Object> jsonMap, String key) {
    var value = jsonMap.get(key);
    if (value == null) {
      return null;
    }
    if (value instanceof JsonMap jm) {
      return jm;
    }
    throw new JsonValueException(STR.fmt("Failed to getJsonMap('{}')", key));
  }

  public static String getString(Map<String, Object> jsonMap, String key) {
    var value = jsonMap.get(key);
    if (value == null) {
      return null;
    }
    if (value instanceof String str) {
      return str;
    }
    throw new JsonValueException(STR.fmt("Failed to getString('{}')", key));
  }

  public static Boolean getBoolOpt(Map<String, Object> jsonMap, String key) {
    var value = jsonMap.get(key);
    if (value == null) {
      return null;
    }
    if (value instanceof Boolean b) {
      return b;
    }
    throw new JsonValueException(STR.fmt("Failed to getBoolOpt('{}')", key));
  }

  public static Integer getIntOpt(Map<String, Object> jsonMap, String key) {
    var value = jsonMap.get(key);
    try {
      return switch (value) {
      case null -> null;
      case Byte b -> b.intValue();
      case Short s -> s.intValue();
      case Integer i -> i;
      case Long l -> Math.toIntExact(l);
      case BigDecimal bd -> bd.intValueExact();
      default -> throw new JsonValueException(STR.fmt("Failed to getIntOpt('{}')", key));
      };
    } catch (ArithmeticException ex) {
      throw new JsonValueException(STR.fmt("Failed to getIntOpt('{}')", key));
    }
  }

  public static Long getLongOpt(Map<String, Object> jsonMap, String key) {
    var value = jsonMap.get(key);
    try {
      return switch (value) {
      case null -> null;
      case Byte b -> b.longValue();
      case Short s -> s.longValue();
      case Integer i -> i.longValue();
      case Long l -> l;
      case BigDecimal bd -> bd.longValueExact();
      default -> throw new JsonValueException(STR.fmt("Failed to getLongOpt('{}')", key));
      };
    } catch (ArithmeticException ex) {
      throw new JsonValueException(STR.fmt("Failed to getLongOpt('{}')", key));
    }
  }

  public static Double getDoubleOpt(Map<String, Object> jsonMap, String key) {
    var value = jsonMap.get(key);
    return switch (value) {
    case null -> null;
    case Double d -> d;
    case Number num -> num.doubleValue();
    default -> throw new JsonValueException(STR.fmt("Failed to getDoubleOpt('{}')", key));
    };
  }

  public static BigDecimal getDecimal(Map<String, Object> jsonMap, String key) {
    var value = jsonMap.get(key);
    return switch (value) {
    case null -> null;
    case BigDecimal bd -> bd;
    case Number num -> new BigDecimal(num.toString());
    default -> throw new JsonValueException(STR.fmt("Failed to getDecimal('{}')", key));
    };
  }

  public static LocalDate getLocalDate(Map<String, Object> jsonMap, String key) {
    var value = jsonMap.get(key);
    return switch (value) {
    case null -> null;
    case LocalDate date -> date;
    case String str -> DateUtils.parseLocalDate(str);
    default -> throw new JsonValueException(STR.fmt("Failed to getLocalDate('{}')", key));
    };
  }

  public static LocalTime getLocalTime(Map<String, Object> jsonMap, String key) {
    var value = jsonMap.get(key);
    return switch (value) {
    case null -> null;
    case LocalTime time -> time;
    case String str -> DateUtils.parseLocalTime(str);
    default -> throw new JsonValueException(STR.fmt("Failed to getLocalTime('{}')", key));
    };
  }

  public static LocalDateTime getLocalDateTime(Map<String, Object> jsonMap, String key) {
    var value = jsonMap.get(key);
    return switch (value) {
    case null -> null;
    case LocalDateTime dt -> dt;
    case String str -> DateUtils.parseLocalDateTime(str);
    default -> throw new JsonValueException(STR.fmt("Failed to getLocalDateTime('{}')", key));
    };
  }

  public static OffsetTime getOffsetTime(Map<String, Object> jsonMap, String key) {
    var value = jsonMap.get(key);
    return switch (value) {
    case null -> null;
    case OffsetTime ot -> ot;
    case String str -> DateUtils.parseOffsetTime(str);
    default -> throw new JsonValueException(STR.fmt("Failed to getOffsetTime('{}')", key));
    };
  }

  public static OffsetDateTime getOffsetDateTime(Map<String, Object> jsonMap, String key) {
    var value = jsonMap.get(key);
    return switch (value) {
    case null -> null;
    case OffsetDateTime odt -> odt;
    case String str -> DateUtils.parseOffsetDateTime(str);
    default -> throw new JsonValueException(STR.fmt("Failed to getOffsetDateTime('{}')", key));
    };
  }
}
