// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * @author Loc Ha
 *
 */
public class EmailUtils {

  // @formatter:off
  /**
   * Practical email validation rules:
   *
   * <b>General</b>
   * - Total length: 1-255 characters
   * - Case-insensitive
   * - No consecutive dots
   *
   * <b>Local-part</b>
   * - Allows letters, digits, '_', '%', '+', '-'
   * - Dots allowed only between tokens
   * - Cannot start/end with dot
   *
   * <b>Domain</b>
   * - Labels separated by dots
   * - Labels may contain '-'
   * - Labels cannot start/end with '-'
   *
   * <b>TLD</b>
   * - Letters only
   * - Length 2-63
   */
  // @formatter:on
  private static final String EMAIL_REGEX = "(?=.{1,255}$)(?!.*\\.\\.)([a-z\\d_%+-]+(?:\\.[a-z\\d_%+-]+)*)@(?:[a-z\\d](?:[a-z\\d-]{0,61}[a-z\\d])?\\.)+[a-z]{2,63}";

  private static final Pattern EMAIL_PATTERN = Pattern.compile("^" + EMAIL_REGEX + "$", Pattern.CASE_INSENSITIVE);

  private static final Pattern VALID_EMAILS_PATTERN = Pattern
      .compile("^" + EMAIL_REGEX + "(?:\\s*,\\s*" + EMAIL_REGEX + ")*$", Pattern.CASE_INSENSITIVE);

  public static boolean isValidEmail(String email) {
    if (email == null) {
      return true;
    }
    return EMAIL_PATTERN.matcher(email.strip()).matches();
  }

  public static boolean isValidEmails(String emails) {
    if (emails == null) {
      return true;
    }
    return VALID_EMAILS_PATTERN.matcher(emails.strip()).matches();
  }

  public static String toEmails(String emails) {
    if (emails == null) {
      return null;
    }

    var emailArr = SplitUtils.splitByComma(emails);
    if (emailArr.length == 0) {
      return null;
    }

    var result = Arrays.stream(emailArr).filter(Objects::nonNull).map(String::strip).filter(s -> !s.isEmpty())
        .collect(Collectors.joining(","));

    return result.isEmpty() ? null : result;
  }
}
