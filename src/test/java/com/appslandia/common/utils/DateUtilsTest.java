// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
