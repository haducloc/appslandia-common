// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.STR;

/**
 *
 * @author Loc Ha
 *
 */
public class GroupFormat {
  private static final Pattern GROUP_PATTERN = Pattern.compile("\\{\\s*\\d+\\s*}");

  final String format;
  final boolean validate;

  private FormatPart[] parts;
  private int inputLength;
  private int outputLength;

  public GroupFormat(String format) {
    this(format, false);
  }

  public GroupFormat(String format, boolean validate) {
    this.format = Arguments.notNull(format, "format is required.");
    this.validate = validate;

    parseFormat(format);
  }

  private void parseFormat(String format) {
    var matcher = GROUP_PATTERN.matcher(format);
    List<FormatPart> parts = new ArrayList<>();

    var inputLength = 0;
    var outputLength = 0;

    var prevEnd = 0;
    while (matcher.find()) {

      // Non group
      var chunk = format.substring(prevEnd, matcher.start());
      if (!chunk.isEmpty()) {
        parts.add(new FormatPart(chunk, 0));
        outputLength += chunk.length();
      }

      // {\d+}
      var paramGroup = matcher.group();
      var paramLen = paramGroup.substring(paramGroup.indexOf('{') + 1, paramGroup.length() - 1).strip();
      var groupLen = Integer.parseInt(paramLen);

      parts.add(new FormatPart(null, groupLen));
      inputLength += groupLen;

      prevEnd = matcher.end();
    }

    if (prevEnd < format.length()) {
      var chunk = format.substring(prevEnd);
      if (!chunk.isEmpty()) {
        parts.add(new FormatPart(chunk, 0));
        outputLength += chunk.length();
      }
    }
    Arguments.hasElements(parts, "The format '{}' is invalid.", format);

    this.parts = parts.toArray(new FormatPart[parts.size()]);
    this.inputLength = inputLength;
    this.outputLength = outputLength;
  }

  public int getInputLength() {
    return inputLength;
  }

  public boolean isValidate() {
    return validate;
  }

  public String format(String str) {
    if (str == null) {
      return null;
    }
    if (inputLength == 0) {
      return str;
    }
    if (str.length() != inputLength) {
      if (validate) {
        throw new IllegalArgumentException(
            STR.fmt("The given string '{}' must have {} length to be formatted.", str, inputLength));
      }
      return str;
    }

    var sb = new StringBuilder(outputLength);
    var pos = 0;
    for (FormatPart part : parts) {

      if (part.length > 0) {
        sb.append(str.substring(pos, part.length + pos));
        pos += part.length;
      } else {
        sb.append(part.text);
      }
    }
    return sb.toString();
  }

  private static class FormatPart {
    final String text;
    final int length;

    public FormatPart(String text, int length) {
      this.text = text;
      this.length = length;
    }
  }
}
