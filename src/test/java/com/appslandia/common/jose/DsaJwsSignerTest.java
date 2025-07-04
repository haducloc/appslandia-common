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

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.ECGenParameterSpec;
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
public class DsaJwsSignerTest {

  public static class JwsPayload {
    String iss;
    Long exp;

    public JwsPayload setExp(long expiresIn, TimeUnit unit) {
      var timeInMs = System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(expiresIn, unit);
      this.exp = JoseUtils.toNumericDate(timeInMs);
      return this;
    }

    public JwsPayload setIss(String iss) {
      this.iss = iss;
      return this;
    }
  }

  private static KeyPair generateECKeyPair() throws Exception {
    var keyPairGenerator = KeyPairGenerator.getInstance("EC");
    var ecGenParameterSpec = new ECGenParameterSpec("secp256r1");
    keyPairGenerator.initialize(ecGenParameterSpec);
    return keyPairGenerator.generateKeyPair();
  }

  @Test
  public void test_EC() {
    try {
      var keyPair = generateECKeyPair();

      // signer
      var signer = DsaJwsSigner.ES256(JwsPayload.class).setJsonProcessor(JoseGson.newJsonProcessor())
          .setPrivateKey(keyPair.getPrivate()).setPublicKey(keyPair.getPublic()).build();

      var header = signer.newHeader();
      var payload = new JwsPayload().setIss("Issuer1").setExp(1, TimeUnit.HOURS);

      var token = signer.sign(new JwsToken<>(header, payload));
      Assertions.assertNotNull(token);

      // AUTH0
      var algorithm = Algorithm.ECDSA256((ECPublicKey) keyPair.getPublic(), (ECPrivateKey) keyPair.getPrivate());
      var verifier = JWT.require(algorithm).withIssuer("Issuer1").build();

      var decodedJWT = verifier.verify(token);

      Assertions.assertEquals("JWT", decodedJWT.getType());
      Assertions.assertEquals("ES256", decodedJWT.getAlgorithm());
      Assertions.assertEquals("Issuer1", decodedJWT.getIssuer());

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_EC_verify() {
    try {
      var keyPair = generateECKeyPair();

      // AUTH0
      var algorithm = Algorithm.ECDSA256((ECPublicKey) keyPair.getPublic(), (ECPrivateKey) keyPair.getPrivate());
      var auth0Jwt = JWT.create().withIssuer("Issuer1").sign(algorithm);

      // signer
      var signer = DsaJwsSigner.ES256(JwsPayload.class).setJsonProcessor(JoseGson.newJsonProcessor())
          .setPrivateKey(keyPair.getPrivate()).setPublicKey(keyPair.getPublic()).build();

      var token = signer.parse(auth0Jwt);
      signer.verify(token);

      Assertions.assertNotNull(token);
      Assertions.assertNotNull(token.getHeader());
      Assertions.assertNotNull(token.getPayload());

      Assertions.assertEquals("JWT", token.getHeader().getTyp());
      Assertions.assertEquals("ES256", token.getHeader().getAlg());
      Assertions.assertEquals("Issuer1", token.getPayload().iss);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  private static KeyPair generateRSKeyPair() throws Exception {
    var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);
    return keyPairGenerator.generateKeyPair();
  }

  @Test
  public void test_RS() {
    try {
      var keyPair = generateRSKeyPair();

      // signer
      var signer = DsaJwsSigner.RS256(JwsPayload.class).setJsonProcessor(JoseGson.newJsonProcessor())
          .setPrivateKey(keyPair.getPrivate()).setPublicKey(keyPair.getPublic()).build();

      var header = signer.newHeader();
      var payload = new JwsPayload().setIss("Issuer1").setExp(1, TimeUnit.HOURS);

      var token = signer.sign(new JwsToken<>(header, payload));
      Assertions.assertNotNull(token);

      // AUTH0
      var algorithm = Algorithm.RSA256((RSAPublicKey) keyPair.getPublic(), (RSAPrivateKey) keyPair.getPrivate());
      var verifier = JWT.require(algorithm).withIssuer("Issuer1").build();

      var decodedJWT = verifier.verify(token);

      Assertions.assertEquals("JWT", decodedJWT.getType());
      Assertions.assertEquals("RS256", decodedJWT.getAlgorithm());
      Assertions.assertEquals("Issuer1", decodedJWT.getIssuer());

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_RS_verify() {
    try {
      var keyPair = generateRSKeyPair();

      // AUTH0
      var algorithm = Algorithm.RSA256((RSAPublicKey) keyPair.getPublic(), (RSAPrivateKey) keyPair.getPrivate());
      var auth0Jwt = JWT.create().withIssuer("Issuer1").sign(algorithm);

      // signer
      var signer = DsaJwsSigner.RS256(JwsPayload.class).setJsonProcessor(JoseGson.newJsonProcessor())
          .setPrivateKey(keyPair.getPrivate()).setPublicKey(keyPair.getPublic()).build();

      var token = signer.parse(auth0Jwt);
      signer.verify(token);

      Assertions.assertNotNull(token);
      Assertions.assertNotNull(token.getHeader());
      Assertions.assertNotNull(token.getPayload());

      Assertions.assertEquals("JWT", token.getHeader().getTyp());
      Assertions.assertEquals("RS256", token.getHeader().getAlg());
      Assertions.assertEquals("Issuer1", token.getPayload().iss);

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }
}
