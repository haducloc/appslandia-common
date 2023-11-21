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
import java.security.spec.PSSParameterSpec;
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
public class PSSParameterSpecUtil {

  // PSS (Probabilistic Padding Scheme): For RSA digital signatures
  static final Map<String, PSSParameterSpec> SPECS;

  static {
    Map<String, PSSParameterSpec> map = new HashMap<>();

    map.put("SHA224withRSA/PSS", new PSSParameterSpec("SHA-224", "MGF1", MGF1ParameterSpec.SHA224, 224 / 8, 1));
    map.put("SHA256withRSA/PSS", new PSSParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, 256 / 8, 1));
    map.put("SHA384withRSA/PSS", new PSSParameterSpec("SHA-384", "MGF1", MGF1ParameterSpec.SHA384, 384 / 8, 1));
    map.put("SHA512withRSA/PSS", new PSSParameterSpec("SHA-512", "MGF1", MGF1ParameterSpec.SHA512, 512 / 8, 1));

    map.put("SHA3-224withRSA/PSS", new PSSParameterSpec("SHA3-224", "MGF1", MGF1ParameterSpec.SHA3_224, 224 / 8, 1));
    map.put("SHA3-256withRSA/PSS", new PSSParameterSpec("SHA3-256", "MGF1", MGF1ParameterSpec.SHA3_256, 256 / 8, 1));
    map.put("SHA3-384withRSA/PSS", new PSSParameterSpec("SHA3-384", "MGF1", MGF1ParameterSpec.SHA3_384, 384 / 8, 1));
    map.put("SHA3-512withRSA/PSS", new PSSParameterSpec("SHA3-512", "MGF1", MGF1ParameterSpec.SHA3_512, 512 / 8, 1));

    SPECS = Collections.unmodifiableMap(map);
  }

  public static PSSParameterSpec getInstance(String signatureAlg) {
    Asserts.notNull(signatureAlg);

    PSSParameterSpec spec = SPECS.get(signatureAlg);
    if (spec == null) {
      throw new IllegalArgumentException(
          STR.fmt("The given signature algorithm name '{}' is unregistered.", signatureAlg));
    }
    return spec;
  }
}
