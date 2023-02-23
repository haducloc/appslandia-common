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

package com.appslandia.common.jwt;

import java.io.Serializable;

import com.appslandia.common.utils.Asserts;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JwtToken implements Serializable {
    private static final long serialVersionUID = 1L;

    final JwtHeader header;
    final JwtPayload payload;

    final String headerPart;
    final String payloadPart;
    final String signaturePart;

    public JwtToken(JwtHeader header, JwtPayload payload) {
	this(header, payload, null, null, null);
    }

    public JwtToken(JwtHeader header, JwtPayload payload, String headerPart, String payloadPart, String signaturePart) {
	this.header = Asserts.notNull(header);
	this.payload = Asserts.notNull(payload);

	this.headerPart = headerPart;
	this.payloadPart = payloadPart;
	this.signaturePart = signaturePart;
    }

    public JwtHeader getHeader() {
	return this.header;
    }

    public JwtPayload getPayload() {
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
