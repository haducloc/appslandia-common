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

package com.appslandia.common.jose;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.crypto.MacSigner;

/**
 *
 * @author Loc Ha
 *
 */
public class JwtSignerTest {

  @Test
  public void test() {
    var signer = new JwtSigner().setJsonProcessor(JoseGson.newJsonProcessor());
    signer.setAlg("HS256").setSigner(new MacSigner().setAlgorithm("HmacSHA256").setSecret("secret".getBytes()));
    signer.setIss("Issuer1");

    var header = signer.newHeader();
    var payload = signer.newPayload().setExp(1, TimeUnit.DAYS).setIatNow();

    try {
      var jwt = signer.sign(new JwtToken(header, payload));
      Assertions.assertNotNull(jwt);

      var token = signer.parse(jwt);
      signer.verify(token);

      Assertions.assertNotNull(token);
      Assertions.assertNotNull(token.getHeader());
      Assertions.assertNotNull(token.getPayload());

      Assertions.assertEquals("JWT", token.getHeader().getTyp());
      Assertions.assertEquals("HS256", token.getHeader().getAlg());
      Assertions.assertEquals("Issuer1", token.getPayload().get("iss"));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_none() {
    var signer = new JwtSigner().setJsonProcessor(JoseGson.newJsonProcessor());
    signer.setIss("Issuer1");

    var header = signer.newHeader();
    var payload = signer.newPayload().setExp(1, TimeUnit.DAYS).setIatNow();

    try {
      var jwt = signer.sign(new JwtToken(header, payload));
      Assertions.assertNotNull(jwt);

      var token = signer.parse(jwt);
      signer.verify(token);

      Assertions.assertNotNull(token);
      Assertions.assertNotNull(token.getHeader());
      Assertions.assertNotNull(token.getPayload());

      Assertions.assertEquals("JWT", token.getHeader().getTyp());
      Assertions.assertEquals("Issuer1", token.getPayload().get("iss"));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }
}
