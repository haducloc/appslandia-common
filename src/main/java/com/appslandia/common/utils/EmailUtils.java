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

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeUtility;

/**
 *
 * @author Loc Ha
 *
 */
public class EmailUtils {

  private static final String EMAIL_REGEX = "(?=.{6,255}$)[_a-z\\d+-]+(\\.[_a-z\\d-]+)*@[a-z\\d-]+(\\.[a-z\\d-]+)*(\\.[a-z]{2,})";

  private static final Pattern EMAIL_PATTERN = Pattern.compile("^" + EMAIL_REGEX + "$", Pattern.CASE_INSENSITIVE);

  private static final Pattern VALID_EMAILS_PATTERN = Pattern
      .compile("^" + EMAIL_REGEX + "(?:\\s*,\\s*" + EMAIL_REGEX + ")*$", Pattern.CASE_INSENSITIVE);

  public static boolean isValidEmail(String email) {
    if (email == null) {
      return true;
    }
    return EMAIL_PATTERN.matcher(email.trim()).matches();
  }

  public static boolean isValidEmails(String emails) {
    if (emails == null) {
      return true;
    }
    return VALID_EMAILS_PATTERN.matcher(emails).matches();
  }

  public static String toEmails(String emails) {
    if (emails == null) {
      return null;
    }

    var emailArr = SplitUtils.splitByComma(emails);
    if (emailArr.length == 0) {
      return null;
    }
    return String.join(",", emailArr);
  }

  public static InternetAddress toAddressEmail(String email, String person) throws AddressException {
    Arguments.notNull(email);

    var address = new InternetAddress(email);
    if (person != null) {
      try {
        address.setPersonal(person, StandardCharsets.UTF_8.name());
      } catch (UnsupportedEncodingException ex) {
        throw new Error(ex);
      }
    }
    return address;
  }

  public static String toPersonEmail(String email, String person) {
    Arguments.notNull(email);
    if (person == null) {
      return email;
    }
    try {
      return String.format("%s <%s>", MimeUtility.encodeWord(person, StandardCharsets.UTF_8.name(), null), email);
    } catch (UnsupportedEncodingException ex) {
      throw new Error(ex);
    }
  }
}
