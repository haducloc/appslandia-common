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

package com.appslandia.common.base;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.nio.Buffer;
import java.time.Clock;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.Temporal;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.ExceptionUtils;
import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.common.utils.ReflectionUtils;
import com.appslandia.common.utils.TypeUtils;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ToStringBuilder {

  @Target({ ElementType.TYPE, ElementType.FIELD })
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface TSIdHash {
  }

  public static class TSPolicy {

    public boolean tsIdHash(Field field, Object value) {
      Asserts.notNull(value);

      if (field != null) {
        if (field.getAnnotation(TSIdHash.class) != null) {
          return true;
        }
      }

      if (value.getClass().getAnnotation(TSIdHash.class) != null) {
        return true;
      }
      return false;
    }

    public boolean tsSimpleType(Class<?> type) {
      if (TypeUtils.isPrimitiveOrWrapper(type)) {
        return true;
      }
      if (AtomicBoolean.class.isAssignableFrom(type) || Number.class.isAssignableFrom(type)) {
        return true;
      }

      if (CharSequence.class.isAssignableFrom(type) || Enum.class.isAssignableFrom(type) || (type == UUID.class)) {
        return true;
      }

      if (Date.class.isAssignableFrom(type) || Temporal.class.isAssignableFrom(type)
          || Period.class.isAssignableFrom(type) || Calendar.class.isAssignableFrom(type)) {
        return true;
      }
      if (TimeZone.class.isAssignableFrom(type) || ZoneId.class.isAssignableFrom(type)
          || Clock.class.isAssignableFrom(type)) {
        return true;
      }

      if (type == URL.class || type == URI.class) {
        return true;
      }
      if (type == Locale.class) {
        return true;
      }
      return false;
    }

    public void tsSimpleValue(Object value, TextBuilder builder) {
      // Character
      if (value.getClass() == Character.class || value.getClass() == String.class) {
        builder.append("'").append(value).append("'");
        return;
      }

      // BigDecimal
      if (value.getClass() == BigDecimal.class) {
        builder.append(((BigDecimal) value).toPlainString());
        return;
      }

      // value.getClass().getSimpleName()
      if (Date.class.isAssignableFrom(value.getClass()) || Temporal.class.isAssignableFrom(value.getClass())
          || Clock.class.isAssignableFrom(value.getClass()) || value.getClass() == Period.class ||

          CharSequence.class.isAssignableFrom(value.getClass()) || value.getClass() == URL.class
          || value.getClass() == URI.class || value.getClass() == UUID.class) {

        builder.append(value.getClass().getSimpleName()).append("('").append(value).append("')");
        return;
      }

      // TimeZone
      if (TimeZone.class.isAssignableFrom(value.getClass())) {
        TimeZone tz = (TimeZone) value;
        builder.append("TimeZone('").append(tz.getID()).append("')");
        return;
      }

      // ZoneId
      if (ZoneId.class.isAssignableFrom(value.getClass())) {
        ZoneId z = (ZoneId) value;
        builder.append("ZoneId('").append(z.getId()).append("')");
        return;
      }

      // Calendar
      if (Calendar.class.isAssignableFrom(value.getClass())) {
        Calendar c = (Calendar) value;
        builder.append("Calendar('").append(c.getTime()).append("', '").append(c.getTimeZone().getID()).append("')");
        return;
      }

      // Locale
      if (value.getClass() == Locale.class) {
        Locale l = (Locale) value;
        builder.append("Locale('").append(l.getLanguage()).append("', '").append(l.getCountry()).append("', '")
            .append(l.getVariant()).append("')");
        return;
      }

      // Other
      builder.append(value);
    }

    public boolean tsIterCompact(Class<?> type) {
      if (TypeUtils.isPrimitiveOrWrapper(type)) {
        return true;
      }
      if (Number.class.isAssignableFrom(type)) {
        return true;
      }
      if (type == Character.class || type == String.class) {
        return true;
      }
      return false;
    }
  }

  private static final TSPolicy DEFAULT_TS_POLICY = new TSPolicy();

  private int tsDepthLevel;
  private TSPolicy tsPolicy = DEFAULT_TS_POLICY;

  private int initIndent;
  private boolean toOneLine;
  private int iterMaxSize;

  public ToStringBuilder() {
    this(2);
  }

  public ToStringBuilder(int tsDepthLevel) {
    setTsDepthLevel(tsDepthLevel);
  }

  public ToStringBuilder setTsDepthLevel(int tsDepthLevel) {
    this.tsDepthLevel = ValueUtils.valueOrMin(tsDepthLevel, 1);
    return this;
  }

  public ToStringBuilder setTSPolicy(TSPolicy tsPolicy) {
    this.tsPolicy = tsPolicy;
    return this;
  }

  public ToStringBuilder setInitIndent(int initIndent) {
    this.initIndent = ValueUtils.valueOrMin(initIndent, 0);
    return this;
  }

  public ToStringBuilder setToOneLine(boolean toOneLine) {
    this.toOneLine = toOneLine;
    return this;
  }

  public ToStringBuilder setIterMaxSize(int iterMaxSize) {
    this.iterMaxSize = iterMaxSize;
    return this;
  }

  public String toString(Object obj) {
    TextBuilder builder = new TextBuilder();
    appendInitIndent(builder, this.initIndent);
    if (obj == null) {
      return builder.append("null").toString();
    }
    this.toStringObject(obj, 1, builder, null, null);
    return builder.toString();
  }

  public String toStringFields(Object obj) {
    TextBuilder builder = new TextBuilder();
    appendInitIndent(builder, this.initIndent);
    if (obj == null) {
      return builder.append("null").toString();
    }
    this.toStringFields(obj, 1, builder);
    return builder.toString();
  }

  private void toStringObject(Object obj, int level, TextBuilder builder, Class<?> iterElementType, Integer iterLen) {
    if (obj == null) {
      builder.append("null");
      return;
    }

    // Optional
    if (obj instanceof Optional) {
      obj = ((Optional<?>) obj).orElse(null);

      if (obj == null) {
        builder.append("null?");
        return;
      }
    }

    // toIdHash
    if (this.tsPolicy.tsIdHash(null, obj)) {
      builder.append(ObjectUtils.toIdHash(obj));
      return;
    }

    // Simple Types
    if (this.tsPolicy.tsSimpleType(obj.getClass())) {
      this.tsPolicy.tsSimpleValue(obj, builder);
      return;
    }

    // BitSet
    if (obj.getClass() == BitSet.class) {
      builder.append("BitSet(").append(obj).append(")");
      return;
    }

    // Try to determine iterLen and iterElementType
    if (iterLen == null) {
      iterLen = (obj instanceof Collection) ? ((Collection<?>) obj).size() : null;
    }
    iterElementType = (obj instanceof Collection) ? getIterElementType((Collection<?>) obj) : iterElementType;

    if (obj instanceof Iterable) {
      toStringIterator(obj,
          new IteratorIterator(((Iterable<?>) obj).iterator(), iterLen, iterElementType, this.tsPolicy), level,
          builder);
      return;
    }
    if (obj instanceof Iterator) {
      toStringIterator(obj, new IteratorIterator((Iterator<?>) obj, iterLen, iterElementType, this.tsPolicy), level,
          builder);
      return;
    }
    if (obj instanceof Enumeration) {
      toStringIterator(obj, new EnumerationIterator((Enumeration<?>) obj, iterLen, iterElementType, this.tsPolicy),
          level, builder);
      return;
    }
    if (obj.getClass().isArray()) {
      toStringIterator(obj, new ArrayIterator(obj, this.tsPolicy), level, builder);
      return;
    }
    if (obj instanceof Buffer) {
      toStringIterator(obj, new ArrayIterator(((Buffer) obj).array(), this.tsPolicy), level, builder);
      return;
    }

    if (obj instanceof Map) {
      toStringMap((Map<?, ?>) obj, level, builder);
      return;
    }
    if (obj instanceof Throwable) {
      builder.append(ExceptionUtils.toStackTrace((Throwable) obj));
      return;
    }

    // Fields
    toStringFields(obj, level, builder);
  }

  private void toStringFields(Object obj, int level, TextBuilder builder) {
    builder.append(ObjectUtils.toIdHash(obj));
    if (level > this.tsDepthLevel) {
      return;
    }
    builder.append("[");
    boolean isFirst = true;

    Class<?> clazz = obj.getClass();
    while (clazz != null) {
      Field[] fields = clazz.getDeclaredFields();
      for (Field field : fields) {
        if (field.getName().equals("serialVersionUID")) {
          continue;
        }

        if (!isFirst) {
          builder.append(this.toOneLine ? ", " : ",");
        } else {
          isFirst = false;
        }
        appendln(builder, level + this.initIndent, false);
        builder.append(field.getName()).append(": ");

        try {
          field.setAccessible(true);
          Object fieldVal = field.get(obj);

          if (fieldVal == null) {
            builder.append("null");
          } else {
            if (this.tsPolicy.tsIdHash(field, fieldVal)) {
              builder.append(ObjectUtils.toIdHash(fieldVal));

            } else {

              Class<?> iterElementType = getIterElementType(field);
              this.toStringObject(fieldVal, level + 1, builder, iterElementType, null);
            }
          }
        } catch (Exception ex) {

          if (ex instanceof InaccessibleObjectException) {
            int idx = ex.getMessage().indexOf("accessible: module");
            builder.append("error=...").append(idx > 0 ? ex.getMessage().substring(idx + 12) : ex.getMessage());
          } else {
            builder.append("error=").append(ExceptionUtils.buildMessage(ex));
          }
        }
      }
      clazz = clazz.getSuperclass();
    }
    if (isFirst) {
      builder.append(" no fields ]");
    } else {
      appendln(builder, level - 1 + this.initIndent, false);
      builder.append("]");
    }
  }

  private void toStringIterator(Object obj, ElementIterator iterator, int level, TextBuilder builder) {
    builder.append(ObjectUtils.toIdHash(obj));
    if (level > this.tsDepthLevel) {
      return;
    }
    builder.append("[");
    boolean isFirst = true;

    while (iterator.hasNext()) {
      Object element = iterator.next();

      // Sub-levels ONLY
      if ((level > 1)) {
        if ((this.iterMaxSize > 0) && (iterator.getIndex() > this.iterMaxSize)) {

          builder.append(this.toOneLine || iterator.isCompact() ? ", " : ",");
          appendln(builder, level + this.initIndent, iterator.isCompact()).append("...");
          break;
        }
      }

      if (!isFirst) {
        builder.append(this.toOneLine || iterator.isCompact() ? ", " : ",");
      } else {
        isFirst = false;
      }
      appendln(builder, level + this.initIndent, iterator.isCompact());

      if (element == null) {
        builder.append("null");
      } else {
        if (this.tsPolicy.tsIdHash(null, element)) {
          builder.append(ObjectUtils.toIdHash(element));

        } else {
          this.toStringObject(element, level + 1, builder, null, null);
        }
      }
    }
    if (isFirst) {
      builder.append(" no elements ]");
    } else {
      appendln(builder, level - 1 + this.initIndent, iterator.isCompact());
      builder.append("] (").append(iterator.getIterLen() != null ? iterator.getIterLen() : "?").append(")");
    }
  }

  private void toStringMap(Map<?, ?> map, int level, TextBuilder builder) {
    builder.append(ObjectUtils.toIdHash(map));
    if (level > this.tsDepthLevel) {
      return;
    }
    builder.append("[");
    boolean isFirst = true;

    for (Object key : map.keySet()) {
      if (!isFirst) {
        builder.append(this.toOneLine ? ", " : ",");
      } else {
        isFirst = false;
      }
      appendln(builder, level + this.initIndent, false);

      builder.append(key).append(": ");
      Object entryVal = map.get(key);
      if (entryVal == null) {
        builder.append("null");
      } else {
        if (this.tsPolicy.tsIdHash(null, entryVal)) {
          builder.append(ObjectUtils.toIdHash(entryVal));

        } else {
          this.toStringObject(entryVal, level + 1, builder, null, null);
        }
      }
    }
    if (isFirst) {
      builder.append(" no entries ]");
    } else {
      appendln(builder, level - 1 + this.initIndent, false);
      builder.append("] (").append(map.size()).append(")");
    }
  }

  private void toStringAttributes(Object obj, Method getAttributeMethod, Set<String> attributes, int level,
      TextBuilder builder) {
    builder.append("[");
    boolean isFirst = true;

    for (String attribute : attributes) {
      if (!isFirst) {
        builder.append(this.toOneLine ? ", " : ",");
      } else {
        isFirst = false;
      }
      appendln(builder, level + this.initIndent, false);
      builder.append(attribute).append(": ");

      try {
        Object element = getAttributeMethod.invoke(obj, attribute);
        if (element == null) {
          builder.append("null");
        } else {
          if ("jakarta.servlet.error.exception".equals(attribute)
              || "javax.servlet.error.exception".equals(attribute)) {
            builder.append(ExceptionUtils.buildMessage((Throwable) element));

          } else if (this.tsPolicy.tsIdHash(null, element)) {
            builder.append(ObjectUtils.toIdHash(element));

          } else {
            this.toStringObject(element, level + 1, builder, null, null);
          }
        }
      } catch (Exception ex) {
        builder.append("error=").append(ExceptionUtils.buildMessage(ex));
      }
    }
    if (isFirst) {
      builder.append(" no elements ]");
    } else {
      appendln(builder, level - 1 + this.initIndent, false);
      builder.append("]");
    }
  }

  public String toStringAttributes(Object obj) {
    TextBuilder builder = new TextBuilder();
    appendInitIndent(builder, this.initIndent);
    if (obj == null) {
      builder.append("null");
      return builder.toString();
    }
    try {
      Set<String> attributes = getAttributeNames(obj, "getAttributeNames");
      Method method = ReflectionUtils.findMethod(obj.getClass(), "getAttribute");
      Asserts.notNull(method);

      builder.append(ObjectUtils.toIdHash(obj)).append("-attributes");
      toStringAttributes(obj, method, attributes, 1, builder);
    } catch (Exception ex) {
      builder.append("error=").append(ExceptionUtils.buildMessage(ex));
    }
    return builder.toString();
  }

  public String toStringHeaders(Object obj) {
    TextBuilder builder = new TextBuilder();
    appendInitIndent(builder, this.initIndent);
    if (obj == null) {
      builder.append("null");
      return builder.toString();
    }
    try {
      Set<String> attributes = getAttributeNames(obj, "getHeaderNames");
      Method method = ReflectionUtils.findMethod(obj.getClass(), "getHeaders");
      Asserts.notNull(method);

      builder.append(ObjectUtils.toIdHash(obj)).append("-headers");
      toStringAttributes(obj, method, attributes, 1, builder);
    } catch (Exception ex) {
      builder.append("error=").append(ExceptionUtils.buildMessage(ex));
    }
    return builder.toString();
  }

  private static Set<String> getAttributeNames(Object obj, String methodName) throws Exception {
    Method method = ReflectionUtils.findMethod(obj.getClass(), methodName);
    Asserts.notNull(method);

    Object attrs = method.invoke(obj);
    Set<String> names = new TreeSet<>();
    if (attrs instanceof Enumeration) {

      Enumeration<String> enm = ObjectUtils.cast(attrs);
      while (enm.hasMoreElements()) {
        names.add(enm.nextElement());
      }
    } else {
      Collection<String> attrCol = ObjectUtils.cast(attrs);
      names.addAll(attrCol);
    }
    return names;
  }

  private TextBuilder appendln(TextBuilder builder, int n, boolean iteratorCompact) {
    if (!this.toOneLine && !iteratorCompact) {
      builder.appendln();
      builder.appendsp(2 * n);
    }
    return builder;
  }

  private TextBuilder appendInitIndent(TextBuilder builder, int initIndent) {
    if (!this.toOneLine) {
      builder.appendsp(2 * initIndent);
    }
    return builder;
  }

  static Class<?> getIterElementType(Field field) {
    if (Iterable.class.isAssignableFrom(field.getType()) || Iterator.class.isAssignableFrom(field.getType())
        || Enumeration.class.isAssignableFrom(field.getType())) {

      if (field.getGenericType() instanceof ParameterizedType) {
        return ReflectionUtils.getArgTypes1(field.getGenericType());
      }
    }
    return null;
  }

  static Class<?> getIterElementType(Collection<?> collection) {
    Class<?> firstType = null;

    for (Object ele : collection) {
      if (ele == null) {
        continue;
      }

      if (firstType == null) {
        firstType = ele.getClass();
      } else {
        if (firstType != ele.getClass()) {
          return null;
        }
      }
    }
    return firstType;
  }

  interface ElementIterator {

    boolean hasNext();

    Object next();

    int getIndex();

    Integer getIterLen();

    Class<?> elementType();

    boolean isCompact();
  }

  static class ArrayIterator implements ElementIterator {

    final Object obj;
    final int len;
    final Class<?> elementType;
    final boolean isCompact;

    int index = 0;

    public ArrayIterator(Object obj, TSPolicy tsDecision) {
      this.obj = obj;
      this.len = Array.getLength(obj);

      this.elementType = obj.getClass().getComponentType();
      this.isCompact = tsDecision.tsIterCompact(this.elementType);
    }

    @Override
    public boolean hasNext() {
      return this.index < this.len;
    }

    @Override
    public Object next() {
      return Array.get(this.obj, this.index++);
    }

    @Override
    public int getIndex() {
      return this.index;
    }

    @Override
    public Integer getIterLen() {
      return this.len;
    }

    @Override
    public Class<?> elementType() {
      return this.elementType;
    }

    @Override
    public boolean isCompact() {
      return this.isCompact;
    }
  }

  static class IteratorIterator implements ElementIterator {

    final Iterator<?> obj;
    final Integer len;
    final Class<?> elementType;
    final boolean isCompact;

    int index = 0;

    public IteratorIterator(Iterator<?> obj, Integer iterLen, Class<?> elementType, TSPolicy tsDecision) {
      this.obj = obj;
      this.len = iterLen;
      this.elementType = elementType;
      this.isCompact = (elementType != null) && tsDecision.tsIterCompact(elementType);
    }

    @Override
    public boolean hasNext() {
      return this.obj.hasNext();
    }

    @Override
    public Object next() {
      this.index++;
      return this.obj.next();
    }

    @Override
    public int getIndex() {
      return this.index;
    }

    @Override
    public Integer getIterLen() {
      return this.len;
    }

    @Override
    public Class<?> elementType() {
      return this.elementType;
    }

    @Override
    public boolean isCompact() {
      return this.isCompact;
    }
  }

  static class EnumerationIterator implements ElementIterator {

    final Enumeration<?> obj;
    final Integer len;
    final Class<?> elementType;
    final boolean isCompact;

    int index = 0;

    public EnumerationIterator(Enumeration<?> obj, Integer iterLen, Class<?> elementType, TSPolicy tsDecision) {
      this.obj = obj;
      this.len = iterLen;
      this.elementType = elementType;
      this.isCompact = (elementType != null) && tsDecision.tsIterCompact(elementType);
    }

    @Override
    public boolean hasNext() {
      return this.obj.hasMoreElements();
    }

    @Override
    public Object next() {
      this.index++;
      return this.obj.nextElement();
    }

    @Override
    public int getIndex() {
      return this.index;
    }

    @Override
    public Integer getIterLen() {
      return this.len;
    }

    @Override
    public Class<?> elementType() {
      return this.elementType;
    }

    @Override
    public boolean isCompact() {
      return this.isCompact;
    }
  }
}
