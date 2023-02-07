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

package com.appslandia.common.crypto;

import java.io.PrintWriter;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;

import com.appslandia.common.base.BaseEncoder;
import com.appslandia.common.base.StringWriter;
import com.appslandia.common.utils.Asserts;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class PKIUtils {

    public static byte[] toDerEncoded(String pem) {
	pem = removeBeginEnd(pem);
	return BaseEncoder.BASE64_MIME.decode(pem);
    }

    public static String removeBeginEnd(String pem) {
	// Remove -----BEGIN .+ -----
	int idx = pem.indexOf("-----BEGIN ");
	boolean valid = true;

	if (idx < 0)
	    valid = false;

	if (valid) {
	    idx = pem.indexOf("-----", idx + 11);
	    if (idx < 0)
		valid = false;
	    else
		pem = pem.substring(idx + 5);
	}

	// Remove -----END .+ -----
	if (valid) {
	    idx = pem.lastIndexOf("-----");
	    if (idx < 0)
		valid = false;
	}

	if (valid) {
	    idx = pem.lastIndexOf("-----END ", idx - 9);
	    if (idx < 0)
		valid = false;
	}
	Asserts.isTrue(valid, "The pem is invalid.");

	return pem.substring(0, idx).trim();
    }

    // PEM: Privacy-enhanced Electronic Mail
    public static String toPemEncoded(byte[] der, String label) {
	StringWriter pem = new StringWriter(der.length * 4 / 3 + 128);
	PrintWriter pw = new PrintWriter(pem);
	pw.append("-----BEGIN ").append(label).println("-----");
	pw.write(BaseEncoder.BASE64_MIME.encode(der));
	pw.println();
	pw.append("-----END ").append(label).print("-----");
	pw.close();
	return pem.toString();
    }

    public static String toPemLabel(PublicKey key) {
	return key.getAlgorithm() + " PUBLIC KEY";
    }

    public static String toPemLabel(PrivateKey key) {
	return key.getAlgorithm() + " PRIVATE KEY";
    }

    public static String toPemEncoded(PublicKey key) {
	return toPemEncoded(key.getEncoded(), toPemLabel(key));
    }

    public static String toPemEncoded(PrivateKey key) {
	return toPemEncoded(key.getEncoded(), toPemLabel(key));
    }

    public static String toPemLabel(Certificate cert) {
	return cert.getType() + " CERTIFICATE";
    }

    public static String toPemEncoded(Certificate cert) throws CertificateEncodingException {
	return toPemEncoded(cert.getEncoded(), toPemLabel(cert));
    }
}
