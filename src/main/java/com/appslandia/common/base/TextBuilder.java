// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import java.io.Serializable;

/**
 *
 * @author Loc Ha
 *
 */
public class TextBuilder implements Serializable, CharSequence {
  private static final long serialVersionUID = 1L;

  final String lineSeparator;
  final StringBuilder sb;

  public TextBuilder() {
    sb = new StringBuilder();
    lineSeparator = System.lineSeparator();
  }

  public TextBuilder(int capacity) {
    this(capacity, System.lineSeparator());
  }

  public TextBuilder(int capacity, String lineSeparator) {
    sb = new StringBuilder(capacity);
    this.lineSeparator = lineSeparator;
  }

  public TextBuilder appendln() {
    return appendln(1);
  }

  public TextBuilder appendln(int num) {
    for (var i = 0; i < num; i++) {
      sb.append(lineSeparator);
    }
    return this;
  }

  public TextBuilder appendtab() {
    return appendtab(1);
  }

  public TextBuilder appendtab(int num) {
    return append(num, '\t');
  }

  public TextBuilder appendsp() {
    return appendsp(1);
  }

  public TextBuilder appendsp(int num) {
    return append(num, ' ');
  }

  private TextBuilder append(int num, char chr) {
    for (var i = 0; i < num; i++) {
      sb.append(chr);
    }
    return this;
  }

  public TextBuilder append(Object val) {
    sb.append(val);
    return this;
  }

  public TextBuilder appendln(Object val) {
    sb.append(val);
    appendln(1);
    return this;
  }

  @Override
  public int length() {
    return sb.length();
  }

  public void clear() {
    sb.setLength(0);
  }

  @Override
  public char charAt(int index) {
    return sb.charAt(index);
  }

  @Override
  public CharSequence subSequence(int start, int end) {
    return sb.subSequence(start, end);
  }

  @Override
  public String toString() {
    return sb.toString();
  }
}
