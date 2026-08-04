// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jose;

import java.math.BigInteger;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.EllipticCurve;

import com.appslandia.common.crypto.AlgorithmParametersUtil;
import com.appslandia.common.crypto.CryptoException;
import com.appslandia.common.crypto.CryptoUtils;
import com.appslandia.common.crypto.KeyFactoryUtil;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.STR;

/**
 *
 * @author Loc Ha
 *
 */
public class EcPublicKeyJwkConverter extends JwkConverter<ECPublicKey> {

  private String ecKeyFactoryProvider;
  private String ecAlgParamProvider;

  private KeyFactoryUtil keyFactoryUtil;

  public EcPublicKeyJwkConverter() {
    super("EC");
  }

  @Override
  protected void init() throws Exception {
    keyFactoryUtil = new KeyFactoryUtil("EC", ecKeyFactoryProvider);
  }

  @Override
  public JsonWebKey toJsonWebKey(ECPublicKey key) {
    initialize();
    Arguments.isTrue("EC".equals(key.getAlgorithm()));

    // JsonWebKey
    var jwk = new JsonWebKey();
    jwk.setKty(kty);

    // curve
    var curve = getCurveName(key.getParams().getCurve());
    jwk.put("crv", curve);

    // ecPoint
    var ecPoint = key.getW();

    var xBytes = CryptoUtils.stripLeadingZeros(ecPoint.getAffineX().toByteArray());
    var yBytes = CryptoUtils.stripLeadingZeros(ecPoint.getAffineY().toByteArray());

    jwk.put("x", JoseUtils.getJoseBase64().encode(xBytes));
    jwk.put("y", JoseUtils.getJoseBase64().encode(yBytes));
    return jwk;
  }

  @Override
  public ECPublicKey fromJsonWebKey(JsonWebKey jwk) throws CryptoException {
    initialize();
    Arguments.isTrue(kty.equals(kty), "kty doesn't match.");

    var curve = Arguments.notNull((String) jwk.get("crv"), "crv is required.");
    var stdName = getStdName(curve);

    var x = Arguments.notNull((String) jwk.get("x"), "x is required.");
    var y = Arguments.notNull((String) jwk.get("y"), "y is required.");

    // ecPoint
    var xBytes = JoseUtils.getJoseBase64().decode(x);
    var yBytes = JoseUtils.getJoseBase64().decode(y);
    var ecPoint = new ECPoint(new BigInteger(1, xBytes), new BigInteger(1, yBytes));

    // ECParameterSpec
    var specUtil = new AlgorithmParametersUtil("EC", ecAlgParamProvider);
    specUtil.setAlgParamSpec(new ECGenParameterSpec(stdName));

    var ecParamSpec = specUtil.getParameterSpec(ECParameterSpec.class);
    var pubKeySpec = new ECPublicKeySpec(ecPoint, ecParamSpec);

    var pk = keyFactoryUtil.toPublicKey(pubKeySpec);
    return (ECPublicKey) pk;
  }

  public EcPublicKeyJwkConverter setEcKeyFactoryProvider(String ecKeyFactoryProvider) {
    assertNotInitialized();
    this.ecKeyFactoryProvider = ecKeyFactoryProvider;
    return this;
  }

  public EcPublicKeyJwkConverter setEcAlgParamProvider(String ecAlgParamProvider) {
    assertNotInitialized();
    this.ecAlgParamProvider = ecAlgParamProvider;
    return this;
  }

  protected String getCurveName(EllipticCurve curve) {
    switch (curve.getField().getFieldSize()) {
    case 256:
      return "P-256";
    case 384:
      return "P-384";
    case 521:
      return "P-521";
    default:
      break;
    }
    throw new IllegalArgumentException(STR.fmt("Unsupported curve: {}", curve));
  }

  protected String getStdName(String curve) {
    switch (curve) {
    case "P-256":
      return "secp256r1";
    case "P-384":
      return "secp384r1";
    case "P-521":
      return "secp521r1";
    default:
      break;
    }
    throw new IllegalArgumentException(STR.fmt("Unsupported curve: {}", curve));
  }
}
