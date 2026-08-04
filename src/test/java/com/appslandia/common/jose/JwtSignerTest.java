// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
