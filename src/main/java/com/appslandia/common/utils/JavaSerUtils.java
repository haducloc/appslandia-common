// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Loc Ha
 *
 */
public class JavaSerUtils {

  private static final int DEFAULT_BUFFER_SIZE = 8192;

  public static byte[] serialize(Object obj) throws IOException {
    var out = new ByteArrayOutputStream(DEFAULT_BUFFER_SIZE);
    var oos = new ObjectOutputStream(out);
    oos.writeObject(obj);
    oos.flush();
    return out.toByteArray();
  }

  public static <T> T deserialize(byte[] ser, Class<T> targetType)
      throws IOException, ClassCastException, ClassNotFoundException {
    var ois = new ObjectInputStream(new ByteArrayInputStream(ser));
    return targetType.cast(ois.readObject());
  }
}
