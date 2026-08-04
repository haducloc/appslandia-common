// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
  public void test_ExactMatch() {
    Assertions.assertTrue(matcher.match("/foo/bar", "/foo/bar"));
    Assertions.assertFalse(matcher.match("/foo/bar", "/foo/baz"));
  }

  @Test
  public void test_SingleStar() {
    Assertions.assertTrue(matcher.match("/foo/*", "/foo/bar"));
    Assertions.assertFalse(matcher.match("/foo/*", "/foo/bar/baz"));
    Assertions.assertTrue(matcher.match("/a*b", "/ab"));
    Assertions.assertTrue(matcher.match("/a*b", "/axb"));
    Assertions.assertFalse(matcher.match("/a*b", "/axxb/c"));
  }

  @Test
  public void test_DoubleStar() {
    Assertions.assertTrue(matcher.match("/foo/**", "/foo/bar/baz"));
    Assertions.assertTrue(matcher.match("/**", "/anything/here"));
    Assertions.assertTrue(matcher.match("/**", ""));
    Assertions.assertTrue(matcher.match("/**", "/"));
    Assertions.assertTrue(matcher.match("/**/test", "/a/b/c/test"));
    Assertions.assertFalse(matcher.match("/a/**/b", "/a/x/y/z"));
  }

  @Test
  public void test_QuestionMark() {
    Assertions.assertTrue(matcher.match("/data/?/file.txt", "/data/a/file.txt"));
    Assertions.assertFalse(matcher.match("/data/?/file.txt", "/data/ab/file.txt"));
    Assertions.assertTrue(matcher.match("/file-?.txt", "/file-a.txt"));
    Assertions.assertFalse(matcher.match("/file-?.txt", "/file-aa.txt"));
  }

  @Test
  public void test_TrailingSlashIgnored() {
    Assertions.assertTrue(matcher.match("/foo/bar", "/foo/bar/"));
    Assertions.assertTrue(matcher.match("/foo/bar/", "/foo/bar"));
    Assertions.assertTrue(matcher.match("/foo/bar/", "/foo/bar/"));
  }

  @Test
  public void test_CaseInsensitive() {
    Assertions.assertTrue(insensitive.match("/FOO/BAR", "/foo/bar"));
    Assertions.assertTrue(insensitive.match("/foo/*.TXT", "/FOO/test.txt"));
    Assertions.assertTrue(insensitive.match("/A/**/Z", "/a/x/y/z"));
  }

  @Test
  public void test_EmptyPathAndPattern() {
    Assertions.assertTrue(matcher.match("", ""));
    Assertions.assertTrue(matcher.match("/**", ""));
    Assertions.assertFalse(matcher.match("/foo", ""));
    Assertions.assertTrue(matcher.match("/**", "/"));
  }

  @Test
  public void test_ConsecutiveDoubleStars() {
    Assertions.assertTrue(matcher.match("/**/**/file.txt", "/a/b/file.txt"));
    Assertions.assertTrue(matcher.match("/**/file.txt", "/file.txt"));
    Assertions.assertTrue(matcher.match("/**/**", "/a/b/c"));
  }

  @Test
  public void test_WindowsPathNormalization() {
    Assertions.assertTrue(matcher.match("C:\\foo\\**\\bar", "C:/foo/baz/bar"));
    Assertions.assertTrue(matcher.match("C:\\**\\file.txt", "C:/x/y/z/file.txt"));
  }

  @Test
  public void test_DoubleStarBetweenFixedPrefixAndSuffix() {
    Assertions.assertTrue(matcher.match("/a/**/b", "/a/x/b"));
    Assertions.assertTrue(matcher.match("/a/**/b", "/a/x/y/b"));
    Assertions.assertFalse(matcher.match("/a/**/b", "/a"));
  }

  @Test
  public void test_MixedWildcards() {
    Assertions.assertTrue(matcher.match("/a*b?c/**/x", "/axbyc/z/x"));
    Assertions.assertFalse(matcher.match("/a*b?c/**/x", "/azc/z/y"));
  }

  @Test
  public void test_RootAndSlash() {
    Assertions.assertTrue(matcher.match("/**", "/"));
    Assertions.assertFalse(matcher.match("/foo", "/"));
  }

  @Test
  public void test_GlobalDoubleStar() {
    Assertions.assertTrue(matcher.match("**", ""));
    Assertions.assertTrue(matcher.match("**", "/a"));
    Assertions.assertTrue(matcher.match("**", "/a/b/c"));
  }

  @Test
  public void test_LeadingDoubleStar() {
    Assertions.assertTrue(matcher.match("**/foo.txt", "foo.txt"));
    Assertions.assertTrue(matcher.match("**/foo.txt", "a/b/foo.txt"));
    Assertions.assertFalse(matcher.match("**/foo.txt", "bar.txt"));
  }

  @Test
  public void test_TrailingSingleStar() {
    Assertions.assertFalse(matcher.match("/foo/*", "/foo"));
    Assertions.assertTrue(matcher.match("/foo/*", "/foo/bar"));
  }

  @Test
  public void test_EmptyPatternBehavior() {
    Assertions.assertTrue(matcher.match("", ""));
    Assertions.assertTrue(matcher.match("", "/"));
    Assertions.assertFalse(matcher.match("", "a"));
  }

  @Test
  public void test_RootPatternBehavior() {
    Assertions.assertTrue(matcher.match("/", "/"));
    Assertions.assertTrue(matcher.match("/", ""));
    Assertions.assertFalse(matcher.match("/", "/a"));
  }

  @Test
  public void test_ConsecutiveSlashes() {
    Assertions.assertTrue(matcher.match("/foo/bar", "/foo//bar"));
    Assertions.assertTrue(matcher.match("/foo/*", "/foo//bar"));
  }

  @Test
  public void test_MiddleDoubleStar() {
    Assertions.assertTrue(matcher.match("/a/**/c", "/a/b/c"));
    Assertions.assertTrue(matcher.match("/a/**/c", "/a/b/x/y/c"));
    Assertions.assertTrue(matcher.match("/a/**/c", "/a/c"));
    Assertions.assertFalse(matcher.match("/a/**/c", "/b/c"));
  }

  @Test
  public void test_SimpleFileExtensionMatching() {
    Assertions.assertTrue(matcher.match("/foo/bar.txt", "/foo/bar.txt"));
    Assertions.assertTrue(matcher.match("/foo/*.txt", "/foo/bar.txt"));
    Assertions.assertFalse(matcher.match("/foo/*.txt", "/foo/bar.xml"));
  }

  @Test
  public void test_PatternLongerThanPath() {
    Assertions.assertFalse(matcher.match("/a/b/c", "/a/b"));
  }

  @Test
  public void test_EmptySegmentInPattern() {
    Assertions.assertTrue(matcher.match("/foo//bar", "/foo/bar"));
    Assertions.assertTrue(matcher.match("/foo//*/bar", "/foo/x/bar"));
  }

  @Test
  public void test_TrailingDoubleStar() {
    Assertions.assertTrue(matcher.match("/a/**", "/a"));
    Assertions.assertTrue(matcher.match("/a/**", "/a/"));
    Assertions.assertTrue(matcher.match("/a/**", "/a/b"));
    Assertions.assertTrue(matcher.match("/a/**", "/a/b/c"));
  }

  @Test
  public void test_DoubleStarMatchesZeroSegmentsInMiddle() {
    Assertions.assertTrue(matcher.match("/a/**/b", "/a/b"));
    Assertions.assertTrue(matcher.match("/a/**/b/c", "/a/b/c"));
  }

  @Test
  public void test_SingleStarDoesNotCrossSlash() {
    Assertions.assertFalse(matcher.match("/foo*bar", "/foo/x/bar"));
    Assertions.assertFalse(matcher.match("/foo/*bar", "/foo/x/bar"));
  }

  @Test
  public void test_QuestionMarkDoesNotCrossSlash() {
    Assertions.assertFalse(matcher.match("/foo?/bar", "/foo/x/bar"));
    Assertions.assertFalse(matcher.match("/foo/?/bar", "/foo//bar"));
  }

  @Test
  public void test_RelativePaths() {
    Assertions.assertTrue(matcher.match("foo/*", "foo/bar"));
    Assertions.assertFalse(matcher.match("foo/*", "foo/bar/baz"));
    Assertions.assertTrue(matcher.match("foo/**", "foo/bar/baz"));
  }

  @Test
  public void test_UnicodePaths() {
    Assertions.assertTrue(matcher.match("/héllo/*", "/héllo/world"));
    Assertions.assertTrue(matcher.match("/你好/**", "/你好/a/b"));
    Assertions.assertFalse(matcher.match("/héllo/*.txt", "/héllo/file.xml"));
  }

  @Test
  public void test_FileExtensionPatterns() {
    Assertions.assertTrue(matcher.match("/files/*.tar.gz", "/files/app.tar.gz"));
    Assertions.assertFalse(matcher.match("/files/*.tar.gz", "/files/app.zip"));
    Assertions.assertTrue(matcher.match("/files/file-*.*", "/files/file-123.txt"));
  }

  @Test
  public void test_HiddenFiles() {
    Assertions.assertTrue(matcher.match("/foo/.*", "/foo/.gitignore"));
    Assertions.assertTrue(matcher.match("/foo/**/.env", "/foo/a/b/.env"));
    Assertions.assertFalse(matcher.match("/foo/*.txt", "/foo/.env"));
  }

  @Test
  public void test_DeepPathMatching() {
    var path = "/a/a/a/a/a/a/a/a/a/a/a/a/a/a/a/a/a/a/a/a/file.txt";

    Assertions.assertTrue(matcher.match("/**/file.txt", path));
    Assertions.assertTrue(matcher.match("/a/**/file.txt", path));
    Assertions.assertFalse(matcher.match("/b/**/file.txt", path));
  }

  @Test
  public void test_CaseSensitiveNegativeCases() {
    Assertions.assertFalse(matcher.match("/FOO/BAR", "/foo/bar"));
    Assertions.assertFalse(matcher.match("/foo/*.TXT", "/foo/test.txt"));
  }

  @Test
  public void test_CaseInsensitiveNegativeCases() {
    Assertions.assertFalse(insensitive.match("/FOO/BAR", "/foo/baz"));
    Assertions.assertFalse(insensitive.match("/A/**/Z", "/a/x/y/not-z"));
  }

  @Test
  public void test_SpaceInPath() {
    Assertions.assertTrue(matcher.match("/my files/*.txt", "/my files/read me.txt"));
    Assertions.assertFalse(matcher.match("/my files/*.txt", "/my files/read/me.txt"));
  }

  @Test
  public void test_SpecialRegexCharactersAreLiterals() {
    Assertions.assertTrue(matcher.match("/file[1].txt", "/file[1].txt"));
    Assertions.assertTrue(matcher.match("/price+$5.txt", "/price+$5.txt"));
    Assertions.assertFalse(matcher.match("/file[1].txt", "/file1.txt"));
  }
}
