// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.utils.ArrayUtils;
import com.appslandia.common.utils.IOUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class BOMInputStreamTest {

  @Test
  public void test() {
    var d = "data".getBytes(StandardCharsets.ISO_8859_1);

    try (var bis = new BOMInputStream(new ByteArrayInputStream(d))) {
      var dr = IOUtils.toByteArray(bis);

      Assertions.assertArrayEquals(d, dr);

    } catch (IOException ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_utf8() {
    var d = "data".getBytes(StandardCharsets.UTF_8);
    var bd = ArrayUtils.append(BOM.UTF_8.getBytes(), d);

    try (var bis = new BOMInputStream(new ByteArrayInputStream(bd))) {
      var dr = IOUtils.toByteArray(bis);

      Assertions.assertArrayEquals(d, dr);

    } catch (IOException ex) {
      Assertions.fail(ex.getMessage());
    }
  }
}
