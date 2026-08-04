// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jdbc;

import java.io.Serializable;

import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.StringUtils;

/**
 *
 *
 * @author Loc Ha
 *
 */
public class SqlLikeEscaper implements Serializable {
  private static final long serialVersionUID = 1L;

  final char escapeMarker;
  final char[] charsToEscape;

  public SqlLikeEscaper(char escapeMarker) {
    this.escapeMarker = escapeMarker;
    charsToEscape = new char[] { '%', '_' };
  }

  public SqlLikeEscaper(char escapeMarker, char[] charsToEscape) {
    this.escapeMarker = escapeMarker;
    this.charsToEscape = charsToEscape.clone();
  }

  public String toLikeEscape(String value) {
    if (value == null) {
      return null;
    }
    var sb = new StringBuilder(value.length() + value.length() / 5);
    sb.append(value);

    var i = -1;
    while (true) {
      i++;
      if (i == sb.length()) {
        break;
      }
      for (char c : charsToEscape) {
        if (c == sb.charAt(i)) {
          sb.insert(i, escapeMarker);
          i++;
          break;
        }
      }
    }
    return sb.length() > 0 ? sb.toString() : value;
  }

  public String toLikePattern(String value, LikeType likeType) {
    Arguments.notNull(likeType);

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
