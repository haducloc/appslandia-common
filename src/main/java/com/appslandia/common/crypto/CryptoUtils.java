// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.crypto;

import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;

import com.appslandia.common.utils.RandomUtils;
import com.appslandia.common.utils.SecureRand;

/**
 *
 * @author Loc Ha
 *
 */
public class CryptoUtils {

  public static void clear(char[] chars) {
    if (chars != null) {
      Arrays.fill(chars, (char) 0);
    }
  }

  public static void clear(byte[] bytes) {
    if (bytes != null) {
      Arrays.fill(bytes, (byte) 0);
    }
  }

  public static char[] toCharArray(byte[] src) {
    var arr = new char[src.length / 2];
    for (var i = 0; i < arr.length; i++) {
      arr[i] = (char) (((0xff & (src[i * 2])) << 8) | (0xff & src[i * 2 + 1]));
    }
    return arr;
  }

  public static byte[] toByteArray(char[] src) {
    var arr = new byte[src.length * 2];
    for (var i = 0; i < src.length; i++) {
      var chr = src[i];
      arr[i * 2] = (byte) (0xff & (chr >> 8));
      arr[i * 2 + 1] = (byte) (0xff & (chr));
    }
    return arr;
  }

  public static byte[] stripLeadingZeros(byte[] bytes) {
    for (var i = 0; i < bytes.length; i++) {
      if (bytes[i] != 0) {
        var stripped = new byte[bytes.length - i];
        System.arraycopy(bytes, i, stripped, 0, stripped.length);
        return stripped;
      }
    }
    return bytes;
  }

  public static void destroy(Destroyable obj) {
    if (obj != null && !obj.isDestroyed()) {
      try {
        obj.destroy();
      } catch (DestroyFailedException ignored) {
      }
    }
  }

  public static SecretKey copy(SecretKey key) {
    var kByte = key.getEncoded();
    SecretKey cKey = new DSecretKeySpec(kByte, key.getAlgorithm());
    clear(kByte);
    return cKey;
  }

  public static byte[] randomBytes(int size) {
    return RandomUtils.nextBytes(size, SecureRand.getInstance());
  }

  private static final String ENC_BEGIN = "ENC(";
  private static final String ENC_END = ")";

  public static String markEncValue(String value) {
    return ENC_BEGIN + value + ENC_END;
  }

  public static boolean isEncValue(String value) {
    return value.startsWith(ENC_BEGIN) && value.endsWith(ENC_END);
  }

  public static String parseEncValue(String value) {
    return value.substring(ENC_BEGIN.length(), value.length() - ENC_END.length());
  }
}
