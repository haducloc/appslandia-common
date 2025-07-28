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

  public static final int DEFAULT_PBE_KEY_SIZE = 32;
  public static final int DEFAULT_PBE_SALT_SIZE = 16;
  public static final int DEFAULT_PBE_ITERATIONS = 100_000;
  public static final String DEFAULT_PBE_KEY_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA256";

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
