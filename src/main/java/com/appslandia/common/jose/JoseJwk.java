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

package com.appslandia.common.jose;

import java.util.Map;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JoseJwk extends JoseMapObject {
    private static final long serialVersionUID = 1L;

    // Specifies the type of the key, such as "RSA", "EC", or "oct".
    public static final String KTY = "kty";

    // Specifies the intended use of the key, such as "sig" (signature) or "enc" (encryption).
    public static final String USE = "use";

    // Specifies the cryptographic algorithm used with the key, such as "RS256" or "ES256".
    public static final String ALG = "alg";

    // A unique identifier for the key.
    public static final String KID = "kid";

    // The modulus and exponent components of an RSA public key.
    public static final String N = "n";
    public static final String E = "e";

    // The private exponent component of an RSA key.
    public static final String D = "d";

    // The x-coordinate and y-coordinate components of an EC public key.
    public static final String X = "x";
    public static final String Y = "y";

    // The value of a symmetric key, represented as a sequence of octets
    public static final String K = "k";

    public JoseJwk() {
    }

    public JoseJwk(Map<String, Object> map) {
	super(map);
    }
}
