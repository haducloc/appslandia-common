// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import java.util.regex.Pattern;

/**
 *
 * @author Loc Ha
 *
 */
public class UserProfileUtils {

  // - start with a letter
  // - contain only letters, digits, dots, underscores, colons, or dashes
  // - do not contain consecutive symbols like "..", "__", etc.
  // - do not end with an allowed symbol

  private static final String ROLE_PATTERN_STRING = "[a-z](?:[a-z\\d]|[._:-](?=[a-z\\d]))*";

  private static final Pattern VALID_ROLES_PATTERN = Pattern
      .compile("^" + ROLE_PATTERN_STRING + "(?:\\s*,\\s*" + ROLE_PATTERN_STRING + ")*$", Pattern.CASE_INSENSITIVE);

  public static boolean isValidUserRoles(String userRoles) {
    if (userRoles == null) {
      return true;
    }
    return VALID_ROLES_PATTERN.matcher(userRoles).matches();
  }

  public static String toUserRoles(String userRoles) {
    if (userRoles == null) {
      return null;
    }

    var roles = SplitUtils.splitByComma(userRoles);
    if (roles.length == 0) {
      return null;
    }
    return String.join(",", roles);
  }

  // allow characters: a-zA-Z0-9.
  // no two consecutive dot
  // must start a-zA-Z
  // length 6-128
  // do not end with dot

  static final Pattern USERNAME_PATTERN = Pattern.compile("^(?=.{6,128}$)[a-z](?:[a-z\\d]|\\.(?=[a-z\\d]))+$",
      Pattern.CASE_INSENSITIVE);

  public static boolean isValidUsername(String userName) {
    if (userName == null) {
      return true;
    }
    return USERNAME_PATTERN.matcher(userName).matches();
  }

  static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\+?[\\d]{7,15}$", Pattern.CASE_INSENSITIVE);

  // A valid phone number must:
  // - Start optionally with a '+' (international format)
  // - Contain only digits after the optional '+'
  // - Have a total of 7 to 15 digits (excluding the '+' sign)

  public static boolean isValidPhoneNumber(String phoneNumber) {
    if (phoneNumber == null) {
      return true;
    }
    return PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();
  }

  public static String toPhoneNumber(String phoneNumber) {
    if (phoneNumber == null) {
      return null;
    }
    phoneNumber = phoneNumber.strip();

    var hasPlus = phoneNumber.startsWith("+");
    var digitOnly = NormalizeUtils.digitOnly(phoneNumber);

    if (digitOnly == null) {
      return null;
    }
    return hasPlus ? ("+" + digitOnly) : digitOnly;
  }
}
