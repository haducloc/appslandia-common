// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.crypto.PasswordUtil;
import com.appslandia.common.utils.URLEncoding;

/**
 *
 * @author Loc Ha
 *
 */
public class URLEncodingTest {

  @Test
  public void test_encodeParam() {
    var s = new String(PasswordUtil.generatePassword(512, 1024));

    var enc = URLEncoding.encodeParam(s);
    var dec = URLEncoding.decodeParam(enc);

    Assertions.assertEquals(s, dec);
  }

  @Test
  public void test_encodeParam_spaceToPlus() {
    var s = " ";

    var enc = URLEncoding.encodeParam(s);
    var dec = URLEncoding.decodeParam(enc);

    Assertions.assertEquals("+", enc);
    Assertions.assertEquals(" ", dec);
  }

  @Test
  public void test_encodeParam_escSpace() {
    var s = " ";

    var enc = URLEncoding.encodeParam(s, false);
    var dec = URLEncoding.decodeParam(enc);

    Assertions.assertEquals("%20", enc);
    Assertions.assertEquals(" ", dec);
  }

  @Test
  public void test_encodePath() {
    var s = new String(PasswordUtil.generatePassword(512, 1024));

    var enc = URLEncoding.encodePath(s);
    var dec = URLEncoding.decodePath(enc);

    Assertions.assertEquals(s, dec);
  }
}
