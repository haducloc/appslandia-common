// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class BytesSizeUtilsTest {

  @Test
  public void test_translateToBytes() {
    var size = BytesSizeUtils.translateToBytes("1GiB 2MiB 3KiB 4B");
    Assertions.assertEquals(1073741824 + 2 * 1_048_576 + 3 * 1024 + 4, size);

    size = BytesSizeUtils.translateToBytes("1.5GiB 4B");
    Assertions.assertEquals((long) (1.5 * 1073741824) + 4, size);

    size = BytesSizeUtils.translateToBytes("1gib 2mib");
    Assertions.assertEquals(1073741824 + 2 * 1_048_576, size);
  }

  @Test
  public void test_translateToBytes_Base10() {
    var size = BytesSizeUtils.translateToBytes("1GB 2MB 3KB 4B");
    Assertions.assertEquals(1000_000_000 + 2 * 1000_000 + 3 * 1000 + 4, size);

    size = BytesSizeUtils.translateToBytes("1.5GB 4B");
    Assertions.assertEquals((long) (1.5 * 1000_000_000L) + 4, size);

    size = BytesSizeUtils.translateToBytes("1gb 2mb");
    Assertions.assertEquals(1000_000_000 + 2 * 1000_000, size);
  }

  @Test
  public void test_translateToBytes_invalid() {
    try {
      BytesSizeUtils.translateToBytes("1GB 2M");
      Assertions.fail();
    } catch (Exception ex) {
    }
    try {
      BytesSizeUtils.translateToBytes("1GB+2M");
      Assertions.fail();
    } catch (Exception ex) {
    }
    try {
      BytesSizeUtils.translateToBytes("1 GB 2M");
      Assertions.fail();
    } catch (Exception ex) {
    }
  }
}
