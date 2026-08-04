// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.mail;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import com.appslandia.common.utils.Arguments;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeUtility;

/**
 *
 * @author Loc Ha
 *
 */
public class MailUtils {

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
}
