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

    this.headerPart = null;
    this.payloadPart = null;
    this.signaturePart = null;
  }

  public JwsToken(JoseHeader header, P payload, String headerPart, String payloadPart, String signaturePart) {
    this.header = Arguments.notNull(header);
    this.payload = Arguments.notNull(payload);

    this.headerPart = Arguments.notNull(headerPart);
    this.payloadPart = Arguments.notNull(payloadPart);
    this.signaturePart = Arguments.notNull(signaturePart);
  }

  public JoseHeader getHeader() {
    return this.header;
  }

  public P getPayload() {
    return this.payload;
  }

  public String getHeaderPart() {
    return this.headerPart;
  }

  public String getPayloadPart() {
    return this.payloadPart;
  }

  public String getSignaturePart() {
    return this.signaturePart;
  }
}
