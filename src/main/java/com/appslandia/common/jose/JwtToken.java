// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jose;

/**
 *
 * @author Loc Ha
 *
 */
public class JwtToken extends JwsToken<JwtPayload> {
  private static final long serialVersionUID = 1L;

  public JwtToken(JoseHeader header, JwtPayload payload) {
    super(header, payload);
  }

  public JwtToken(JoseHeader header, JwtPayload payload, String headerPart, String payloadPart, String signaturePart) {
    super(header, payload, headerPart, payloadPart, signaturePart);
  }
}
