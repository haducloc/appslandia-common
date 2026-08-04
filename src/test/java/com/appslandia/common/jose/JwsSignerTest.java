// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jose;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.crypto.MacSigner;
import com.appslandia.common.utils.MathUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class JwsSignerTest {

  @Test
  public void test_bytes() {
    var signer = new JwsSigner<>(byte[].class).setJsonProcessor(JoseGson.newJsonProcessor());
    signer.setAlg("HS256").setSigner(new MacSigner().setAlgorithm("HmacSHA256").setSecret("secret".getBytes()));

    var header = signer.newHeader();
    var payload = MathUtils.toByteArray(1, 100);

    try {
      var jwt = signer.sign(new JwsToken<>(header, payload));
      Assertions.assertNotNull(jwt);

      var token = signer.parse(jwt);
      signer.verify(token);

      Assertions.assertNotNull(token);
      Assertions.assertNotNull(token.getHeader());
      Assertions.assertNotNull(token.getPayload());

      Assertions.assertEquals("JWT", token.getHeader().getTyp());
      Assertions.assertEquals("HS256", token.getHeader().getAlg());

      Assertions.assertTrue(Arrays.equals(MathUtils.toByteArray(1, 100), token.getPayload()));

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }
}
