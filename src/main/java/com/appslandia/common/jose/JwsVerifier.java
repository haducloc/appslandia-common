// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jose;

/**
 *
 * @author Loc Ha
 *
 */
@FunctionalInterface
public interface JwsVerifier<P> {

  void verify(JwsToken<P> token) throws JoseVerificationException;
}
