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

package com.appslandia.common.base;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class StringWriterTest {

  @Test
  public void test_append() {
    try (StringWriter sw = new StringWriter()) {
      sw.append('a');
      sw.append("bcd");
      sw.append("e_fgh", 1, 4);

      Assertions.assertEquals("abcd_fg", sw.toString());

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_write() {
    try (StringWriter sw = new StringWriter()) {
      sw.write('a');
      sw.write("bcd");
      sw.write("ef".toCharArray());
      sw.write("g_hk", 1, 3);
      sw.write("x_yzt".toCharArray(), 1, 3);

      Assertions.assertEquals("abcdef_hk_yz", sw.toString());

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }

  @Test
  public void test_writeNull() {
    try (StringWriter sw = new StringWriter()) {
      sw.append("a");
      sw.append("_");
      sw.append(null);

      Assertions.assertEquals("a_null", sw.toString());

    } catch (Exception ex) {
      Assertions.fail(ex.getMessage());
    }
  }
}
