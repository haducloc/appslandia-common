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
public class CharArrayOutputTest {

  @Test
  public void test_append() {
    try (var out = new CharArrayOutput()) {
      out.append('a');
      out.append("bcd");
      out.append("e_fgh", 1, 4);

      out.flush();

      Assertions.assertEquals("abcd_fg", out.toString());
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_write() {
    try (var out = new CharArrayOutput()) {
      out.write('a');
      out.write("bcd");
      out.write("ef".toCharArray());
      out.write("g_hk", 1, 3);
      out.write("x_yzt".toCharArray(), 1, 3);

      out.flush();

      Assertions.assertEquals("abcdef_hk_yz", out.toString());
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_writeNull() {
    try (var out = new CharArrayOutput()) {
      out.append("a");
      out.append("_");
      out.append(null);

      out.flush();

      Assertions.assertEquals("a_null", out.toString());
    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }
}
