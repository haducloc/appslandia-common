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

package com.appslandia.common.utils;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class DateUtilsTest {

  @Test
  public void test_translateToMs() {
    var ms = DateUtils.translateToMs("1d 4h 8m");
    Assertions.assertEquals(TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
        + TimeUnit.MILLISECONDS.convert(4, TimeUnit.HOURS) + TimeUnit.MILLISECONDS.convert(8, TimeUnit.MINUTES), ms);

    ms = DateUtils.translateToMs("1D 4H 8M");
    Assertions.assertEquals(TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
        + TimeUnit.MILLISECONDS.convert(4, TimeUnit.HOURS) + TimeUnit.MILLISECONDS.convert(8, TimeUnit.MINUTES), ms);
  }

  @Test
  public void test_translateToMs_failed() {
    try {
      DateUtils.translateToMs("1d 4hr 8min");
      Assertions.fail();
    } catch (Exception ex) {
    }
  }
}
