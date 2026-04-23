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

package com.appslandia.common.jose;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.utils.DateUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class JoseHeaderTest {

  @Test
  public void test_numericDate() {
    var header = new JoseHeader();

    var d = new Date();
    header.setNumericDate("nd", d);

    var d1 = header.getNumericDate("nd");
    Assertions.assertEquals((d.getTime() / 1000) * 1000, d1.getTime());
  }

  @Test
  public void test_LocalDate() {
    var header = new JoseHeader();
    header.set("key", "2024-06-13");

    var val = header.getLocalDate("key");
    Assertions.assertEquals(DateUtils.parseLocalDate("2024-06-13"), val);
  }

  @Test
  public void test_LocalTime() {
    var header = new JoseHeader();
    header.set("key", "12:34:56");

    var val = header.getLocalTime("key");
    Assertions.assertEquals(DateUtils.parseLocalTime("12:34:56"), val);
  }

  @Test
  public void test_LocalDateTime() {
    var header = new JoseHeader();
    header.set("key", "2024-06-13T12:34:56");

    var val = header.getLocalDateTime("key");
    Assertions.assertEquals(DateUtils.parseLocalDateTime("2024-06-13T12:34:56"), val);
  }

  @Test
  public void test_OffsetTime() {
    var header = new JoseHeader();
    header.set("key", "12:34:56+01:00");

    var val = header.getOffsetTime("key");
    Assertions.assertEquals(DateUtils.parseOffsetTime("12:34:56+01:00"), val);
  }

  @Test
  public void test_OffsetDateTime() {
    var header = new JoseHeader();
    header.set("key", "2024-06-13T12:34:56+01:00");

    var val = header.getOffsetDateTime("key");
    Assertions.assertEquals(DateUtils.parseOffsetDateTime("2024-06-13T12:34:56+01:00"), val);
  }
}
