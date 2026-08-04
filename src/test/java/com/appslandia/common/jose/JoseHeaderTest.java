// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
