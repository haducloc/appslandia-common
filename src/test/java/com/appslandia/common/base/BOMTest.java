// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class BOMTest {

  @Test
  public void test_parse_UTF8() {
    var bom = new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 0 };
    var b = BOM.parse(bom, 3);
    Assertions.assertNotNull(b);
    Assertions.assertEquals(BOM.UTF_8, b);
  }

  @Test
  public void test_parse_UTF_16BE() {
    var bom = new byte[] { (byte) 0xFE, (byte) 0xFF, 0, 0 };
    var b = BOM.parse(bom, 2);
    Assertions.assertNotNull(b);
    Assertions.assertEquals(BOM.UTF_16BE, b);
  }

  @Test
  public void test_parse_UTF_16LE() {
    var bom = new byte[] { (byte) 0xFF, (byte) 0xFE, 0, 0 };
    var b = BOM.parse(bom, 2);
    Assertions.assertNotNull(b);
    Assertions.assertEquals(BOM.UTF_16LE, b);
  }

  @Test
  public void test_parse_UTF_32BE() {
    var bom = new byte[] { 0, 0, (byte) 0xFE, (byte) 0xFF };
    var b = BOM.parse(bom, 4);
    Assertions.assertNotNull(b);
    Assertions.assertEquals(BOM.UTF_32BE, b);
  }

  @Test
  public void test_parse_UTF_32LE() {
    var bom = new byte[] { (byte) 0xFF, (byte) 0xFE, 0, 0 };
    var b = BOM.parse(bom, 4);
    Assertions.assertNotNull(b);
    Assertions.assertEquals(BOM.UTF_32LE, b);
  }

  @Test
  public void test_others() {
    var bom = new byte[] { (byte) 0xFF, (byte) 0xFE, 0, 0 };
    var b = BOM.parse(bom, 2);
    Assertions.assertNotNull(b);
    Assertions.assertEquals(BOM.UTF_16LE, b);

    b = BOM.parse(bom, 4);
    Assertions.assertNotNull(b);
    Assertions.assertEquals(BOM.UTF_32LE, b);
  }
}
