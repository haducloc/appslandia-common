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

import java.util.regex.Pattern;

/**
 *
 * @author Loc Ha
 *
 */
public class UserUtils {

  // - start with a letter
  // - contain only letters, digits, dots, underscores, colons, or dashes
  // - do not contain consecutive symbols like "..", "__", etc.

  private static final String ROLE_PATTERN_STRING = "[a-z](?:[a-z\\d]|[._:-](?![._:-]))*";

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

  static final Pattern USERNAME_PATTERN = Pattern.compile("^(?=.{6,128}$)(?!.*\\.\\.)(?!.*\\.$)[a-z][a-z\\d.]+$",
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

  // contains a-z and 0-9
  // start with a-z, followed by zero or more digits
  // length 3-45

  static final Pattern NICKNAME_PATTERN = Pattern.compile("^(?=.{3,45}$)[a-z]{1,}[\\d]*$", Pattern.CASE_INSENSITIVE);

  public static boolean isValidNickname(String nickname) {
    if (nickname == null) {
      return true;
    }
    return NICKNAME_PATTERN.matcher(nickname).matches();
  }
}
