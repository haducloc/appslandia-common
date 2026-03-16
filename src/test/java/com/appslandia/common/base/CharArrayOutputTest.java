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
