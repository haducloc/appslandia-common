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

package com.appslandia.common.utils;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 *
 * @author Loc Ha
 *
 */
public class FileNameUtils {

  private static final Pattern VALID_NAME_PATTERN = Pattern.compile("(?=.*[a-z\\d])[a-z\\d_\\-' ]+",
      Pattern.CASE_INSENSITIVE);

  private static final Pattern VALID_EXTENSION_PATTERN = Pattern.compile("[a-z\\d]+", Pattern.CASE_INSENSITIVE);

  private static boolean isValidFileName(String fileName) {
    Arguments.notNull(fileName);

    var idx = fileName.lastIndexOf('.');
    if (idx <= 0 || idx == fileName.length() - 1) {
      return false;
    }
    var namePart = fileName.substring(0, idx);

    if (!VALID_NAME_PATTERN.matcher(namePart).matches()) {
      return false;
    }

    var extension = fileName.substring(idx + 1);
    if (!VALID_EXTENSION_PATTERN.matcher(extension).matches()) {
      return false;
    }
    return true;
  }

  public static String getNamePart(String fileName) {
    Arguments.notNull(fileName);
    Arguments.isTrue(isValidFileName(fileName));

    var idx = fileName.lastIndexOf('.');
    var namePart = fileName.substring(0, idx);
    return NormalizeUtils.normalizeLabel(namePart);
  }

  public static String getExtPart(String fileName) {
    Arguments.notNull(fileName);
    Arguments.isTrue(isValidFileName(fileName));

    var idx = fileName.lastIndexOf('.');
    return fileName.substring(idx);
  }

  public static String toFileNameNow(String fileName) {
    return toFileName(fileName, DateUtils.getFormatter("yyyyMMdd-HHmmss-SSS").format(LocalDateTime.now()));
  }

  public static String toFileNameUUID(String fileName) {
    return toFileName(fileName, UUID.randomUUID().toString());
  }

  public static String toFileName(String fileName) {
    return toFileName(fileName, null);
  }

  public static String toFileName(String fileName, Object extra) {
    Arguments.notNull(fileName);
    Arguments.isTrue(isValidFileName(fileName));

    var idx = fileName.lastIndexOf('.');
    var namePart = fileName.substring(0, idx);
    if (extra != null) {
      namePart = namePart + '-' + extra;
    }

    namePart = NormalizeUtils.normalizeLabel(namePart);
    var extPart = fileName.substring(idx).toLowerCase(Locale.ENGLISH);
    return namePart + extPart;
  }

  public static String toUnixPath(String path) {
    if (path == null || path.indexOf('\\') == -1) {
      return path;
    }
    return path.replace('\\', '/');
  }

  public static String toNamePart(Object... parts) {
    Arguments.hasElements(parts);

    var sb = new StringBuilder(parts.length * 16);
    for (var part : parts) {
      if (sb.length() > 0) {
        sb.append('-');
      }
      sb.append(part);
    }
    return NormalizeUtils.normalizeLabel(sb.toString());
  }
}
