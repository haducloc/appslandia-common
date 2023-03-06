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

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.crypto.MacDigester;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JwtSignerTest {

    @Test
    public void test() {
	JwtSigner jwtSigner = new JwtSigner().setJsonProcessor(JwtGson.newJsonProcessor());
	jwtSigner.setAlg("HS256").setSigner(new MacDigester().setAlgorithm("HmacSHA256").setSecret("secret".getBytes()));
	jwtSigner.setIssuer("Issuer1");

	JwtHeader header = jwtSigner.newHeader();
	JwtPayload payload = jwtSigner.newPayload().setExpiresIn(1, TimeUnit.DAYS).setIssuedAtNow();

	try {
	    String jwt = jwtSigner.toJwt(new JwtToken(header, payload));
	    Assertions.assertNotNull(jwt);

	    JwtToken token = jwtSigner.parseJwt(jwt);
	    jwtSigner.verifyJwt(token);

	    Assertions.assertNotNull(token);
	    Assertions.assertNotNull(token.getHeader());
	    Assertions.assertNotNull(token.getPayload());

	    Assertions.assertEquals("JWT", token.getHeader().getType());
	    Assertions.assertEquals("HS256", token.getHeader().getAlgorithm());
	    Assertions.assertEquals("Issuer1", token.getPayload().get("iss"));

	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
    }

    @Test
    public void test_none() {
	JwtSigner jwtSigner = new JwtSigner().setJsonProcessor(JwtGson.newJsonProcessor());
	jwtSigner.setIssuer("Issuer1");

	JwtHeader header = jwtSigner.newHeader();
	JwtPayload payload = jwtSigner.newPayload().setExpiresIn(1, TimeUnit.DAYS).setIssuedAtNow();

	try {
	    String jwt = jwtSigner.toJwt(new JwtToken(header, payload));
	    Assertions.assertNotNull(jwt);

	    JwtToken token = jwtSigner.parseJwt(jwt);
	    jwtSigner.verifyJwt(token);

	    Assertions.assertNotNull(token);
	    Assertions.assertNotNull(token.getHeader());
	    Assertions.assertNotNull(token.getPayload());

	    Assertions.assertEquals("JWT", token.getHeader().getType());
	    Assertions.assertEquals("Issuer1", token.getPayload().get("iss"));

	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
    }
}
