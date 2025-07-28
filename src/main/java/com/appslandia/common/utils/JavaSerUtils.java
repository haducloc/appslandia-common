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
