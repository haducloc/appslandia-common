// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.crypto;

import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.STR;

/**
 *
 * @author Loc Ha
 *
 */
public class CipherOps {

  final String algorithm;
  final String mode;
  final String padding;

  public CipherOps(String transformation) {
    Arguments.notNull(transformation);

    var cipherOps = transformation.split("/");
    Arguments.isTrue(cipherOps.length >= 1 && cipherOps.length <= 3, "transformation is invalid.");

    algorithm = cipherOps[0];
    mode = (cipherOps.length >= 2) ? cipherOps[1] : null;
    padding = (cipherOps.length == 3) ? cipherOps[2] : null;
  }

  public boolean isPadding(String padding) {
    if (this.padding == null) {
      return padding == null || "NoPadding".equalsIgnoreCase(padding);
    }
    return this.padding.equalsIgnoreCase(padding);
  }

  public String getAlgorithm() {
    return algorithm;
  }

  public String getMode() {
    return mode;
  }

  public String getPadding() {
    return padding;
  }

  @Override
  public String toString() {
    if (padding != null) {
      return STR.fmt("{}/{}/{}", algorithm, mode, padding);
    }
    if (mode != null) {
      return STR.fmt("{}/{}", algorithm, mode);
    }
    return algorithm;
  }
}
