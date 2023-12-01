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

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.nio.Buffer;
import java.time.Clock;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.UUID;

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

  @Target({ ElementType.FIELD })
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface TSExcluded {
  }

  public static class ToStringDecision {

    public boolean tsIdHash(Object value, Field field) {
      Asserts.notNull(value);

      return checkAnnotation(value, field, TSIdHash.class);
    }

    public boolean tsToString(Class<?> type) {
      try {
        return type.getMethod("toString").getDeclaringClass() != Object.class;

      } catch (NoSuchMethodException ex) {
        return false;
      } catch (SecurityException ex) {
        return false;
      }
    }

    public boolean tsExcluded(Field field) {
      return field.getAnnotation(TSExcluded.class) != null;
    }

    public boolean isBasicType(Class<?> type) {
      if (TypeUtils.isPrimitiveOrWrapper(type)) {
        return true;
      }
      if (CharSequence.class.isAssignableFrom(type) || Enum.class.isAssignableFrom(type)) {
        return true;
      }
      if ((type == BigDecimal.class) || (type == UUID.class)) {
        return true;
      }

      if (Date.class.isAssignableFrom(type) || Calendar.class.isAssignableFrom(type)) {
        return true;
      }
      if (Temporal.class.isAssignableFrom(type) || Period.class.isAssignableFrom(type)) {
        return true;
      }

      if (TimeZone.class.isAssignableFrom(type) || ZoneId.class.isAssignableFrom(type)
          || Clock.class.isAssignableFrom(type)) {
        return true;
      }

      if (Locale.class.isAssignableFrom(type)) {
        return true;
      }
      return false;
    }

    protected boolean checkAnnotation(Object value, Field field, Class<? extends Annotation> annotationType) {
      // field
      if (field != null) {
        if (field.getAnnotation(annotationType) != null) {
          return true;
        }
      }

      // value type
      if (value.getClass().getAnnotation(annotationType) != null) {
        return true;
      }
      return false;
    }
  }

  public static final ToStringDecision TS_DECISION = new ToStringDecision();

  private int level;
  private ToStringDecision tsDecision = TS_DECISION;

  private int identTabs;
  private boolean toOneLine;
  private int iterLength;

  public ToStringBuilder() {
    this(2);
  }

  public ToStringBuilder(int level) {
    setLevel(level);
  }

  public ToStringBuilder setLevel(int level) {
    this.level = ValueUtils.valueOrMin(level, 1);
    return this;
  }

  public ToStringBuilder setToStringDecision(ToStringDecision tsDecision) {
    this.tsDecision = tsDecision;
    return this;
  }

  public ToStringBuilder setIdentTabs(int identTabs) {
    this.identTabs = ValueUtils.valueOrMin(identTabs, 0);
    return this;
  }

  public ToStringBuilder setToOneLine(boolean toOneLine) {
    this.toOneLine = toOneLine;
    return this;
  }

  public ToStringBuilder setIterLength(int iterLength) {
    this.iterLength = iterLength;
    return this;
  }

  public String toString(Object obj) {
    TextBuilder builder = new TextBuilder();
    appendtab(builder, this.identTabs);
    if (obj == null) {
      return builder.append("null").toString();
    }
    this.toStringObject(obj, 1, builder, null, null);
    return builder.toString();
  }

  public String toStringFields(Object obj) {
    TextBuilder builder = new TextBuilder();
    appendtab(builder, this.identTabs);
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

    // Basic Types
    if (this.tsDecision.isBasicType(obj.getClass())) {

      // Character
      if (obj.getClass() == Character.class) {
        builder.append("'").append(obj).append("'");
        return;
      }

      // String
      if (obj.getClass() == String.class) {
        builder.append("\"").append(obj).append("\"");
        return;
      }

      // *
      if (CharSequence.class.isAssignableFrom(obj.getClass()) || Date.class.isAssignableFrom(obj.getClass())
          || Temporal.class.isAssignableFrom(obj.getClass()) || Clock.class.isAssignableFrom(obj.getClass())
          || obj.getClass() == Period.class) {

        builder.append("\"").append(obj).append("\"?");
        return;
      }

      // TimeZone
      if (TimeZone.class.isAssignableFrom(obj.getClass())) {
        TimeZone tz = (TimeZone) obj;
        builder.append("\"").append(tz.getID()).append("\"?");
        return;
      }

      // ZoneId
      if (ZoneId.class.isAssignableFrom(obj.getClass())) {
        ZoneId z = (ZoneId) obj;
        builder.append("\"").append(z.getId()).append("\"?");
        return;
      }

      // Calendar
      if (Calendar.class.isAssignableFrom(obj.getClass())) {
        Calendar c = (Calendar) obj;
        builder.append("Calendar(\"").append(c.getTime()).append("\", \"").append(c.getTimeZone().getID())
            .append("\")");
        return;
      }

      // Locale
      if (obj.getClass() == Locale.class) {
        Locale l = (Locale) obj;
        builder.append("Locale(\"").append(l.getLanguage()).append("\", \"").append(l.getCountry()).append("\", \"")
            .append(l.getVariant()).append("\")");
        return;
      }

      if (obj.getClass() == BigDecimal.class) {
        builder.append(((BigDecimal) obj).toPlainString());
        return;
      }

      builder.append(obj);
      return;
    }

    // Try to determine iterLen and iterElementType
    iterLen = (obj instanceof Collection) ? ((Collection<?>) obj).size() : iterLen;
    iterElementType = (obj instanceof Collection) ? getIterElementType((Collection<?>) obj) : iterElementType;

    if (obj instanceof Iterable) {
      toStringIterator(obj,
          new IteratorIterator(((Iterable<?>) obj).iterator(), iterLen, iterElementType, this.tsDecision), level,
          builder);
      return;
    }
    if (obj instanceof Iterator) {
      toStringIterator(obj, new IteratorIterator((Iterator<?>) obj, iterLen, iterElementType, this.tsDecision), level,
          builder);
      return;
    }
    if (obj instanceof Enumeration) {
      toStringIterator(obj, new EnumerationIterator((Enumeration<?>) obj, iterLen, iterElementType, this.tsDecision),
          level, builder);
      return;
    }
    if (obj.getClass().isArray()) {
      toStringIterator(obj, new ArrayIterator(obj, this.tsDecision), level, builder);
      return;
    }
    if (obj instanceof Buffer) {
      toStringIterator(obj, new ArrayIterator(((Buffer) obj).array(), this.tsDecision), level, builder);
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

    // Use toString()
    if (this.tsDecision.tsToString(obj.getClass())) {
      builder.append(obj);
      return;
    }

    // Fields
    toStringFields(obj, level, builder);
  }

  private void toStringFields(Object obj, int level, TextBuilder builder) {
    builder.append(ObjectUtils.toIdHash(obj));
    if (level > this.level) {
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
        if (this.tsDecision.tsExcluded(field)) {
          continue;
        }
        if (!isFirst) {
          builder.append(this.toOneLine ? ", " : ",");
        } else {
          isFirst = false;
        }
        appendln(builder, level + this.identTabs, false);
        builder.append(field.getName()).append(": ");

        try {
          field.setAccessible(true);
          Object fieldVal = field.get(obj);

          if (fieldVal == null) {
            builder.append("null");
          } else {
            if (this.tsDecision.tsIdHash(fieldVal, field)) {
              builder.append(ObjectUtils.toIdHash(fieldVal));

            } else {

              Class<?> iterElementType = getIterElementType(field);
              this.toStringObject(fieldVal, level + 1, builder, iterElementType, null);
            }
          }
        } catch (Exception ex) {
          builder.append("error=").append(ExceptionUtils.buildMessage(ex));
        }
      }
      clazz = clazz.getSuperclass();
    }
    if (isFirst) {
      builder.append(" no fields ]");
    } else {
      appendln(builder, level - 1 + this.identTabs, false);
      builder.append("]");
    }
  }

  private void toStringIterator(Object obj, ElementIterator iterator, int level, TextBuilder builder) {
    builder.append(ObjectUtils.toIdHash(obj));
    if (level > this.level) {
      return;
    }
    builder.append("[");
    boolean isFirst = true;

    while (iterator.hasNext()) {
      Object element = iterator.next();

      // Sub-levels ONLY
      if ((level > 1)) {
        if ((this.iterLength > 0) && (iterator.getIndex() > this.iterLength)) {
          builder.append(", ...");
          break;
        }
      }

      if (!isFirst) {
        builder.append(this.toOneLine || iterator.isCompact() ? ", " : ",");
      } else {
        isFirst = false;
      }
      appendln(builder, level + this.identTabs, iterator.isCompact());

      if (element == null) {
        builder.append("null");
      } else {
        if (this.tsDecision.tsIdHash(element, null)) {
          builder.append(ObjectUtils.toIdHash(element));

        } else {
          this.toStringObject(element, level + 1, builder, null, null);
        }
      }
    }
    if (isFirst) {
      builder.append(" no elements ]");
    } else {
      appendln(builder, level - 1 + this.identTabs, iterator.isCompact());
      builder.append("] (").append(iterator.getIterLen() != null ? iterator.getIterLen() : "?").append(")");
    }
  }

  private void toStringMap(Map<?, ?> map, int level, TextBuilder builder) {
    builder.append(ObjectUtils.toIdHash(map));
    if (level > this.level) {
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
      appendln(builder, level + this.identTabs, false);

      builder.append(key).append(": ");
      Object entryVal = map.get(key);
      if (entryVal == null) {
        builder.append("null");
      } else {
        if (this.tsDecision.tsIdHash(entryVal, null)) {
          builder.append(ObjectUtils.toIdHash(entryVal));

        } else {
          this.toStringObject(entryVal, level + 1, builder, null, null);
        }
      }
    }
    if (isFirst) {
      builder.append(" no entries ]");
    } else {
      appendln(builder, level - 1 + this.identTabs, false);
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
      appendln(builder, level + this.identTabs, false);
      builder.append(attribute).append(": ");

      try {
        Object element = getAttributeMethod.invoke(obj, attribute);
        if (element == null) {
          builder.append("null");
        } else {
          if ("jakarta.servlet.error.exception".equals(attribute)
              || "javax.servlet.error.exception".equals(attribute)) {
            builder.append(ExceptionUtils.buildMessage((Throwable) element));

          } else if (this.tsDecision.tsIdHash(element, null)) {
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
      appendln(builder, level - 1 + this.identTabs, false);
      builder.append("]");
    }
  }

  public String toStringAttributes(Object obj) {
    TextBuilder builder = new TextBuilder();
    appendtab(builder, this.identTabs);
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
    appendtab(builder, this.identTabs);
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

  private TextBuilder appendtab(TextBuilder builder, int n) {
    if (!this.toOneLine) {
      builder.appendsp(2 * n);
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

    public ArrayIterator(Object obj, ToStringDecision tsDecision) {
      this.obj = obj;
      this.len = Array.getLength(obj);

      this.elementType = obj.getClass().getComponentType();
      this.isCompact = tsDecision.isBasicType(this.elementType);
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

    public IteratorIterator(Iterator<?> obj, Integer iterLen, Class<?> elementType, ToStringDecision tsDecision) {
      this.obj = obj;
      this.len = iterLen;
      this.elementType = elementType;
      this.isCompact = (elementType != null) && tsDecision.isBasicType(elementType);
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

    public EnumerationIterator(Enumeration<?> obj, Integer iterLen, Class<?> elementType, ToStringDecision tsDecision) {
      this.obj = obj;
      this.len = iterLen;
      this.elementType = elementType;
      this.isCompact = (elementType != null) && tsDecision.isBasicType(elementType);
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
