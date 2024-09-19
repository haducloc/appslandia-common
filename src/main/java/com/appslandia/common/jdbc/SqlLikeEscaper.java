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

package com.appslandia.common.jdbc;

import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.StringUtils;

/**
 * 
 * 
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SqlLikeEscaper {

  final String escapeMarker;
  final char[] charsToEscape;

  public SqlLikeEscaper(String escapeMarker) {
    this.escapeMarker = escapeMarker;
    this.charsToEscape = new char[] { '%', '_' };
  }

  public SqlLikeEscaper(String escapeMarker, char[] charsToEscape) {
    this.escapeMarker = escapeMarker;
    this.charsToEscape = charsToEscape.clone();
  }

  public String toLikeEscape(String value) {
    if (value == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder(value.length() + value.length() / 5);
    sb.append(value);

    int i = -1;
    while (true) {
      i++;
      if (i == sb.length()) {
        break;
      }
      for (char c : this.charsToEscape) {
        if (c == sb.charAt(i)) {
          sb.insert(i, this.escapeMarker);
          i++;
          break;
        }
      }
    }
    return sb.length() > 0 ? sb.toString() : value;
  }

  public String toLikePattern(String value, LikeType likeType) {
    Asserts.notNull(likeType);

    if (StringUtils.isNullOrEmpty(value)) {
      return value;
    }
    if (likeType == LikeType.CONTAINS) {
      return "%" + toLikeEscape(value) + "%";
    }
    if (likeType == LikeType.STARTS_WITH) {
      return toLikeEscape(value) + "%";
    }
    return "%" + toLikeEscape(value);
  }
}
