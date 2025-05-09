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
    return this.accesses;
  }

  public long getWindowMs() {
    return this.windowsMs;
  }

  public double getRatePerMs() {
    return (1.0d * this.accesses) / this.windowsMs;
  }

  @Override
  public String toString() {
    return STR.fmt("RateLimit: accesses={}, windowsMs={}", this.accesses, this.windowsMs);
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
