// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jose;

import java.io.Serializable;

import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class JwsToken<P> implements Serializable {
  private static final long serialVersionUID = 1L;

  final JoseHeader header;
  final P payload;

  final String headerPart;
  final String payloadPart;
  final String signaturePart;

  public JwsToken(JoseHeader header, P payload) {
    this.header = Arguments.notNull(header);
    this.payload = Arguments.notNull(payload);

    headerPart = null;
    payloadPart = null;
    signaturePart = null;
  }

  public JwsToken(JoseHeader header, P payload, String headerPart, String payloadPart, String signaturePart) {
    this.header = Arguments.notNull(header);
    this.payload = Arguments.notNull(payload);

    this.headerPart = Arguments.notNull(headerPart);
    this.payloadPart = Arguments.notNull(payloadPart);
    this.signaturePart = Arguments.notNull(signaturePart);
  }

  public JoseHeader getHeader() {
    return header;
  }

  public P getPayload() {
    return payload;
  }

  public String getHeaderPart() {
    return headerPart;
  }

  public String getPayloadPart() {
    return payloadPart;
  }

  public String getSignaturePart() {
    return signaturePart;
  }
}
