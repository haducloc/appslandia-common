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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Loc Ha
 *
 */
public final class AntPathMatcher {

  public static final AntPathMatcher DEFAULT = new AntPathMatcher(false, true);

  final boolean caseSensitive;
  final boolean ignoreTrailingSlash;

  public AntPathMatcher() {
    this(true, true);
  }

  public AntPathMatcher(boolean caseSensitive, boolean ignoreTrailingSlash) {
    this.caseSensitive = caseSensitive;
    this.ignoreTrailingSlash = ignoreTrailingSlash;
  }

  // Main match function: returns true if the given path matches the pattern
  public boolean match(String pattern, String path) {
    Arguments.notNull(pattern);
    Arguments.notNull(path);

    if (pattern.equals("**") || pattern.equals("/**")) {
      return true;
    }

    // Apply case normalization if necessary
    if (!caseSensitive) {
      pattern = pattern.toLowerCase();
      path = path.toLowerCase();
    }

    // Normalize slashes for cross-platform paths
    pattern = normalize(pattern, ignoreTrailingSlash);
    path = normalize(path, ignoreTrailingSlash);

    // Split both pattern and path into tokens
    var pattDirs = tokenize(pattern);
    var pathDirs = tokenize(path);

    var pattIdxStart = 0;
    var pattIdxEnd = pattDirs.length - 1;
    var pathIdxStart = 0;
    var pathIdxEnd = pathDirs.length - 1;

    // Match from the start until we hit a ** or a mismatch
    while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
      var patDir = pattDirs[pattIdxStart];
      if (patDir.equals("**")) {
        break;
      }
      if (!matchSegment(patDir, pathDirs[pathIdxStart])) {
        return false;
      }
      pattIdxStart++;
      pathIdxStart++;
    }

    // If path is fully consumed but pattern has remaining parts
    if (pathIdxStart > pathIdxEnd) {
      for (var i = pattIdxStart; i <= pattIdxEnd; i++) {
        if (!pattDirs[i].equals("**")) {
          return false;
        }
      }
      return true;
    }

    // If pattern is exhausted before path
    if (pattIdxStart > pattIdxEnd) {
      return false;
    }

    // Match from the end backward until ** or mismatch
    while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
      var patDir = pattDirs[pattIdxEnd];
      if (patDir.equals("**")) {
        break;
      }
      if (!matchSegment(patDir, pathDirs[pathIdxEnd])) {
        return false;
      }
      pattIdxEnd--;
      pathIdxEnd--;
    }

    // Handle middle sections separated by ** wildcards
    while (pattIdxStart != pattIdxEnd && pathIdxStart <= pathIdxEnd) {
      var nextIdx = -1;

      // Find the next ** in the pattern
      for (var i = pattIdxStart + 1; i <= pattIdxEnd; i++) {
        if (pattDirs[i].equals("**")) {
          nextIdx = i;
          break;
        }
      }

      // Skip consecutive ** wildcards
      if (nextIdx == pattIdxStart + 1) {
        pattIdxStart++;
        continue;
      }

      var pattLength = nextIdx - pattIdxStart - 1;
      var foundIdx = -1;

      // Try to align subpattern within remaining path
      outer: for (var i = pathIdxStart; i <= pathIdxEnd - pattLength + 1; i++) {
        for (var j = 0; j < pattLength; j++) {
          var subPat = pattDirs[pattIdxStart + j + 1];
          var subPath = pathDirs[i + j];
          if (!matchSegment(subPat, subPath)) {
            continue outer;
          }
        }
        foundIdx = i;
        break;
      }

      // No match found for this section
      if (foundIdx == -1) {
        return false;
      }

      // Advance indexes
      pattIdxStart = nextIdx;
      pathIdxStart = foundIdx + pattLength;
    }

    // Remaining pattern parts must all be ** to match
    for (var i = pattIdxStart; i <= pattIdxEnd; i++) {
      if (!pattDirs[i].equals("**")) {
        return false;
      }
    }

    // All segments matched
    return true;
  }

  // Matches a single path segment against a single pattern segment (* and ? only)
  private static boolean matchSegment(String pattern, String segment) {
    int p = 0, s = 0;
    int starIdx = -1, matchIdx = 0;

    while (s < segment.length()) {
      // Match one character or '?'
      if (p < pattern.length() && (pattern.charAt(p) == '?' || pattern.charAt(p) == segment.charAt(s))) {
        p++;
        s++;
      }
      // Handle '*' wildcard
      else if (p < pattern.length() && pattern.charAt(p) == '*') {
        starIdx = p++;
        matchIdx = s;
      }
      // If mismatch but a '*' exists, backtrack
      else if (starIdx != -1) {
        p = starIdx + 1;
        matchIdx++;
        s = matchIdx;
      }
      // No match and no '*', fail
      else {
        return false;
      }
    }

    // Skip trailing '*' at the end of pattern
    while (p < pattern.length() && pattern.charAt(p) == '*') {
      p++;
    }

    // Success if full pattern consumed
    return p == pattern.length();
  }

  private static String[] tokenize(String path) {
    List<String> tokens = new ArrayList<>();
    var start = 0;
    for (var i = 0; i < path.length(); i++) {
      if (path.charAt(i) == '/') {
        if (i > start) {
          tokens.add(path.substring(start, i));
        }
        start = i + 1;
      }
    }
    if (start < path.length()) {
      tokens.add(path.substring(start));
    }
    return tokens.toArray(new String[0]);
  }

  private static String normalize(String path, boolean stripTrailing) {
    var normalized = path.replace('\\', '/');
    if (stripTrailing && normalized.length() > 1 && normalized.endsWith("/")) {
      normalized = normalized.substring(0, normalized.length() - 1);
    }
    return normalized;
  }
}
