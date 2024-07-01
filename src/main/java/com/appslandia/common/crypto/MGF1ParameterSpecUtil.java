// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

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

import java.security.spec.MGF1ParameterSpec;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.STR;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MGF1ParameterSpecUtil {

  static final Map<String, MGF1ParameterSpec> SPECS;

  static {
    Map<String, MGF1ParameterSpec> map = new HashMap<>();

    map.put("MD5", new MGF1ParameterSpec("MD5"));
    map.put("SHA-1", MGF1ParameterSpec.SHA1);

    map.put("SHA-224", MGF1ParameterSpec.SHA224);
    map.put("SHA-256", MGF1ParameterSpec.SHA256);
    map.put("SHA-384", MGF1ParameterSpec.SHA384);
    map.put("SHA-512", MGF1ParameterSpec.SHA512);

    map.put("SHA-512/224", MGF1ParameterSpec.SHA512_224);
    map.put("SHA-512/256", MGF1ParameterSpec.SHA512_256);

    map.put("SHA3-224", MGF1ParameterSpec.SHA3_224);
    map.put("SHA3-256", MGF1ParameterSpec.SHA3_256);
    map.put("SHA3-384", MGF1ParameterSpec.SHA3_384);
    map.put("SHA3-512", MGF1ParameterSpec.SHA3_512);

    SPECS = Collections.unmodifiableMap(map);
  }

  public static MGF1ParameterSpec getInstance(String hashAlg) {
    Asserts.notNull(hashAlg);

    MGF1ParameterSpec spec = SPECS.get(hashAlg);
    if (spec == null) {
      throw new IllegalArgumentException(STR.fmt("The algorithm '{}' is unregistered.", hashAlg));
    }
    return spec;
  }
}
