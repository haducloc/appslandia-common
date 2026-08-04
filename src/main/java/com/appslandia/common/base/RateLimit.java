// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import java.io.Serializable;
import java.util.regex.Pattern;

import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.DateUtils;
import com.appslandia.common.utils.STR;

/**
 *
 *
 * @author Loc Ha
 *
 */
public class RateLimit implements Serializable {
  private static final long serialVersionUID = 1L;

  final int accesses;
  final long windowsMs;

  public RateLimit(int accesses, long windowsMs) {
    this.accesses = accesses;
    this.windowsMs = windowsMs;
  }

  public int getAccesses() {
    return accesses;
  }

  public long getWindowMs() {
    return windowsMs;
  }

  public double getRatePerMs() {
    return (1.0d * accesses) / windowsMs;
  }

  @Override
  public String toString() {
    return STR.fmt("RateLimit: accesses={}, windowsMs={}", accesses, windowsMs);
  }

  static final Pattern RATE_LIMIT_PATTERN = Pattern.compile("\\d+\\s*/\\s*\\d+(w|d|h|m|s|ms)",
      Pattern.CASE_INSENSITIVE);

  public static RateLimit parse(String rateLimit) {
    Arguments.notNull(rateLimit);

    if (!RATE_LIMIT_PATTERN.matcher(rateLimit).matches()) {
      throw new IllegalArgumentException(STR.fmt("rateLimit '{}' is invalid.", rateLimit));
    }

    var idx = rateLimit.indexOf('/');
    var accesses = Integer.parseInt(rateLimit.substring(0, idx).strip());
    var windowsMs = DateUtils.translateToMs(rateLimit.substring(idx + 1).strip());

    return new RateLimit(accesses, windowsMs);
  }
}
