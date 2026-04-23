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

package com.appslandia.common.crypto;

import java.io.PrintWriter;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;

import com.appslandia.common.base.BaseEncoder;
import com.appslandia.common.base.StringOutput;
import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class PKIUtils {

  static final String BEGIN_MARKER = "-----BEGIN ";
  static final String END_MARKER = "-----END ";

  public static byte[] toDerEncoded(String pem) {
    pem = removeBeginEnd(pem);
    return BaseEncoder.BASE64_MIME.decode(pem);
  }

  public static String extracData(String pem, String entityName) {
    var beginMarker = "-----BEGIN " + entityName + "-----";
    var endMarker = "-----END " + entityName + "-----";

    var idx1 = pem.indexOf(beginMarker);
    if (idx1 < 0) {
      return null;
    }
    var idx2 = pem.indexOf(endMarker, idx1 + beginMarker.length());
    if (idx2 < 0) {
      return null;
    }
    return pem.substring(idx1 + beginMarker.length(), idx2).strip();
  }

  public static String removeBeginEnd(String pem) {
    // Remove -----BEGIN .+ -----
    var idx = pem.indexOf("-----BEGIN ");
    var valid = true;

    if (idx < 0) {
      valid = false;
    }
    if (valid) {
      idx = pem.indexOf("-----", idx + 11);
      if (idx < 0) {
        valid = false;
      } else {
        pem = pem.substring(idx + 5);
      }
    }

    // Remove -----END .+ -----
    if (valid) {
      idx = pem.lastIndexOf("-----");
      if (idx < 0) {
        valid = false;
      }
    }
    if (valid) {
      idx = pem.lastIndexOf("-----END ", idx - 9);
      if (idx < 0) {
        valid = false;
      }
    }
    Arguments.isTrue(valid, "The pem is invalid.");

    return pem.substring(0, idx).strip();
  }

  // PEM: Privacy-enhanced Electronic Mail
  public static String toPemEncoded(byte[] der, String label) {
    var pem = new StringOutput(der.length * 4 / 3 + 128);
    var pw = new PrintWriter(pem);
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
    var kBytes = key.getEncoded();
    Arguments.notNull(kBytes);
    return toPemEncoded(kBytes, toPemLabel(key));
  }

  public static String toPemEncoded(PrivateKey key) {
    var kBytes = key.getEncoded();
    Arguments.notNull(kBytes);
    try {
      return toPemEncoded(kBytes, toPemLabel(key));
    } finally {
      CryptoUtils.clear(kBytes);
    }
  }

  public static String toPemLabel(Certificate cert) {
    return cert.getType() + " CERTIFICATE";
  }

  public static String toPemEncoded(Certificate cert) throws CertificateEncodingException {
    return toPemEncoded(cert.getEncoded(), toPemLabel(cert));
  }
}
