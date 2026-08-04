// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jose;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

/**
 *
 * @author Loc Ha
 *
 */
public class HsJwsSignerTest {

  public static class JwsPayload {
    String iss;
    Long exp;

    public JwsPayload setExp(long expiresIn, TimeUnit unit) {
      var timeInMs = System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(expiresIn, unit);
      exp = JoseUtils.toNumericDate(timeInMs);
      return this;
    }

    public JwsPayload setIss(String iss) {
      this.iss = iss;
      return this;
    }
  }

  @Test
  public void test_hs() {
    try {
      // signer
      var signer = HsJwsSigner.HS256(JwsPayload.class).setJsonProcessor(JoseGson.newJsonProcessor())
          .setSecret("secret".getBytes()).build();

      var header = signer.newHeader();
      var payload = new JwsPayload().setIss("Issuer1").setExp(1, TimeUnit.HOURS);

      var token = signer.sign(new JwsToken<>(header, payload));
      Assertions.assertNotNull(token);

      // AUTH0
      var algorithm = Algorithm.HMAC256("secret".getBytes());
      var verifier = JWT.require(algorithm).withIssuer("Issuer1").build();

      var decodedJWT = verifier.verify(token);

      Assertions.assertEquals("JWT", decodedJWT.getType());
      Assertions.assertEquals("HS256", decodedJWT.getAlgorithm());
      Assertions.assertEquals("Issuer1", decodedJWT.getIssuer());

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_hs_verify() {
    try {
      // AUTH0
      var algorithm = Algorithm.HMAC256("secret".getBytes());
      var auth0Jwt = JWT.create().withIssuer("Issuer1")
          .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)))
          .sign(algorithm);

      // signer
      var signer = HsJwsSigner.HS256(JwsPayload.class).setJsonProcessor(JoseGson.newJsonProcessor())
          .setSecret("secret".getBytes()).build();

      var token = signer.parse(auth0Jwt);
      signer.verify(token);

      Assertions.assertNotNull(token);
      Assertions.assertNotNull(token.getHeader());
      Assertions.assertNotNull(token.getPayload());

      Assertions.assertEquals("JWT", token.getHeader().getTyp());
      Assertions.assertEquals("HS256", token.getHeader().getAlg());
      Assertions.assertEquals("Issuer1", token.getPayload().iss);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }
}
