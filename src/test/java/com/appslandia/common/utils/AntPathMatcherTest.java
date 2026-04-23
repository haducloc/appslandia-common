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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class AntPathMatcherTest {

  private final AntPathMatcher matcher = new AntPathMatcher(true, true);
  private final AntPathMatcher insensitive = new AntPathMatcher(false, true);

  @Test
  void test_exact_match() {
    Assertions.assertTrue(matcher.match("/foo/bar", "/foo/bar"));
    Assertions.assertFalse(matcher.match("/foo/bar", "/foo/baz"));
  }

  @Test
  void test_single_star() {
    Assertions.assertTrue(matcher.match("/foo/*", "/foo/bar"));
    Assertions.assertFalse(matcher.match("/foo/*", "/foo/bar/baz"));
    Assertions.assertTrue(matcher.match("/a*b", "/ab"));
    Assertions.assertTrue(matcher.match("/a*b", "/axb"));
    Assertions.assertFalse(matcher.match("/a*b", "/axxb/c"));
  }

  @Test
  void test_double_star() {
    Assertions.assertTrue(matcher.match("/foo/**", "/foo/bar/baz"));
    Assertions.assertTrue(matcher.match("/**", "/anything/here"));
    Assertions.assertTrue(matcher.match("/**", ""));
    Assertions.assertTrue(matcher.match("/**", "/"));
    Assertions.assertTrue(matcher.match("/**/test", "/a/b/c/test"));
    Assertions.assertFalse(matcher.match("/a/**/b", "/a/x/y/z"));
  }

  @Test
  void test_question_mark() {
    Assertions.assertTrue(matcher.match("/data/?/file.txt", "/data/a/file.txt"));
    Assertions.assertFalse(matcher.match("/data/?/file.txt", "/data/ab/file.txt"));
    Assertions.assertTrue(matcher.match("/file-?.txt", "/file-a.txt"));
    Assertions.assertFalse(matcher.match("/file-?.txt", "/file-aa.txt"));
  }

  @Test
  void test_trailing_slash_ignored() {
    Assertions.assertTrue(matcher.match("/foo/bar", "/foo/bar/"));
    Assertions.assertTrue(matcher.match("/foo/bar/", "/foo/bar"));
    Assertions.assertTrue(matcher.match("/foo/bar/", "/foo/bar/"));
  }

  @Test
  void test_case_insensitive() {
    Assertions.assertTrue(insensitive.match("/FOO/BAR", "/foo/bar"));
    Assertions.assertTrue(insensitive.match("/foo/*.TXT", "/FOO/test.txt"));
    Assertions.assertTrue(insensitive.match("/A/**/Z", "/a/x/y/z"));
  }

  @Test
  void test_empty_path_and_pattern() {
    Assertions.assertTrue(matcher.match("", ""));
    Assertions.assertTrue(matcher.match("/**", ""));
    Assertions.assertFalse(matcher.match("/foo", ""));
    Assertions.assertTrue(matcher.match("/**", "/"));
  }

  @Test
  void test_consecutive_double_stars() {
    Assertions.assertTrue(matcher.match("/**/**/file.txt", "/a/b/file.txt"));
    Assertions.assertTrue(matcher.match("/**/file.txt", "/file.txt"));
    Assertions.assertTrue(matcher.match("/**/**", "/a/b/c"));
  }

  @Test
  void test_windows_path_normalization() {
    Assertions.assertTrue(matcher.match("C:\\foo\\**\\bar", "C:/foo/baz/bar"));
    Assertions.assertTrue(matcher.match("C:\\**\\file.txt", "C:/x/y/z/file.txt"));
  }

  @Test
  void test_suffix_and_prefix_matching() {
    Assertions.assertTrue(matcher.match("/a/**/b", "/a/x/b"));
    Assertions.assertTrue(matcher.match("/a/**/b", "/a/x/y/b"));
    Assertions.assertFalse(matcher.match("/a/**/b", "/a"));
  }

  @Test
  void test_mixed_wildcards() {
    Assertions.assertTrue(matcher.match("/a*b?c/**/x", "/axbyc/z/x"));
    Assertions.assertFalse(matcher.match("/a*b?c/**/x", "/azc/z/y"));
  }

  @Test
  void test_root_and_slash() {
    Assertions.assertTrue(matcher.match("/**", "/"));
    Assertions.assertFalse(matcher.match("/foo", "/"));
  }

  @Test
  void test_global_double_star() {
    Assertions.assertTrue(matcher.match("**", ""));
    Assertions.assertTrue(matcher.match("**", "/a"));
    Assertions.assertTrue(matcher.match("**", "/a/b/c"));
  }

  @Test
  void test_leading_double_star() {
    Assertions.assertTrue(matcher.match("**/foo.txt", "foo.txt"));
    Assertions.assertTrue(matcher.match("**/foo.txt", "a/b/foo.txt"));
    Assertions.assertFalse(matcher.match("**/foo.txt", "bar.txt"));
  }

  @Test
  void test_trailing_single_star() {
    Assertions.assertFalse(matcher.match("/foo/*", "/foo"));
    Assertions.assertTrue(matcher.match("/foo/*", "/foo/bar"));
  }

  @Test
  void test_empty_pattern_behavior() {
    Assertions.assertTrue(matcher.match("", ""));
    Assertions.assertTrue(matcher.match("", "/"));
    Assertions.assertFalse(matcher.match("", "a"));
  }

  @Test
  void test_root_pattern_behavior() {
    Assertions.assertTrue(matcher.match("/", "/"));
    Assertions.assertTrue(matcher.match("/", ""));
    Assertions.assertFalse(matcher.match("/", "/a"));
  }

  @Test
  void test_consecutive_slashes() {
    Assertions.assertTrue(matcher.match("/foo/bar", "/foo//bar"));
    Assertions.assertTrue(matcher.match("/foo/*", "/foo//bar"));
  }

  @Test
  void test_middle_double_star() {
    Assertions.assertTrue(matcher.match("/a/**/c", "/a/b/c"));
    Assertions.assertTrue(matcher.match("/a/**/c", "/a/b/x/y/c"));
    Assertions.assertTrue(matcher.match("/a/**/c", "/a/c"));
    Assertions.assertFalse(matcher.match("/a/**/c", "/b/c"));
  }

  @Test
  void test_dot_in_path() {
    Assertions.assertTrue(matcher.match("/foo/bar.txt", "/foo/bar.txt"));
    Assertions.assertTrue(matcher.match("/foo/*.txt", "/foo/bar.txt"));
    Assertions.assertFalse(matcher.match("/foo/*.txt", "/foo/bar.xml"));
  }

  @Test
  void test_pattern_longer_than_path() {
    Assertions.assertFalse(matcher.match("/a/b/c", "/a/b"));
  }

  @Test
  void test_empty_segment_in_pattern() {
    Assertions.assertTrue(matcher.match("/foo//bar", "/foo/bar"));
    Assertions.assertTrue(matcher.match("/foo//*/bar", "/foo/x/bar"));
  }

  @Test
  void test_trailing_double_star() {
    Assertions.assertTrue(matcher.match("/a/**", "/a"));
    Assertions.assertTrue(matcher.match("/a/**", "/a/"));
    Assertions.assertTrue(matcher.match("/a/**", "/a/b"));
    Assertions.assertTrue(matcher.match("/a/**", "/a/b/c"));
  }
}
