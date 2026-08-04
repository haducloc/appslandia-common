// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import java.util.LinkedHashSet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class CaseInsensitiveSetTest {

  @Test
  public void test() {
    var m = new CaseInsensitiveSet(new LinkedHashSet<>());
    m.add("k1");

    Assertions.assertTrue(m.contains("K1"));
    Assertions.assertTrue(m.contains("k1"));

    m.add("K2");

    Assertions.assertTrue(m.contains("k2"));
    Assertions.assertTrue(m.contains("K2"));
  }
}
