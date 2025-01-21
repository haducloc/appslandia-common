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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.STR;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class GroupFormat {
  private static final Pattern GROUP_PATTERN = Pattern.compile("\\{\\s*\\d+\\s*}");

  final String format;
  final boolean validate;

  private Group[] groups;
  private int inputLength;
  private int outputLength;

  public GroupFormat(String format) {
    this(format, false);
  }

  public GroupFormat(String format, boolean validate) {
    this.format = Arguments.notNull(format, "format is required.");
    this.validate = validate;

    this.parseFormat(format);
  }

  private void parseFormat(String format) {
    Matcher matcher = GROUP_PATTERN.matcher(format);
    List<Group> groups = new ArrayList<>();

    int inputLength = 0;
    int outputLength = 0;

    int prevEnd = 0;
    while (matcher.find()) {

      // Non group
      String chunk = format.substring(prevEnd, matcher.start());
      if (!chunk.isEmpty()) {
        groups.add(new Group(chunk, 0));
        outputLength += chunk.length();
      }

      // {\d+}
      String paramGroup = matcher.group();
      String paramLen = paramGroup.substring(paramGroup.indexOf('{') + 1, paramGroup.length() - 1).strip();
      int groupLen = Integer.parseInt(paramLen);

      groups.add(new Group(null, groupLen));
      inputLength += groupLen;

      prevEnd = matcher.end();
    }

    if (prevEnd < format.length()) {
      String chunk = format.substring(prevEnd);
      if (!chunk.isEmpty()) {
        groups.add(new Group(chunk, 0));
        outputLength += chunk.length();
      }
    }
    Arguments.hasElements(groups, () -> STR.fmt("The format '{}' is invalid.", format));

    this.groups = groups.toArray(new Group[groups.size()]);
    this.inputLength = inputLength;
    this.outputLength = outputLength;
  }

  public int getInputLength() {
    return this.inputLength;
  }

  public boolean isValidate() {
    return this.validate;
  }

  public String format(String str) {
    if (str == null) {
      return null;
    }
    if (this.inputLength == 0) {
      return str;
    }
    if (str.length() != this.inputLength) {
      if (this.validate) {
        throw new IllegalArgumentException(
            STR.fmt("The given string '{}' must have {} length to be formatted.", str, this.inputLength));
      }
      return str;
    }

    StringBuilder sb = new StringBuilder(this.outputLength);
    int pos = 0;
    for (Group group : this.groups) {

      if (group.length > 0) {
        sb.append(str.substring(pos, group.length + pos));
        pos += group.length;
      } else {
        sb.append(group.text);
      }
    }
    return sb.toString();
  }

  private static class Group {
    final String text;
    final int length;

    public Group(String text, int length) {
      this.text = text;
      this.length = length;
    }
  }
}
