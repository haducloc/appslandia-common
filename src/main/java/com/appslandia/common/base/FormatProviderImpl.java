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

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class FormatProviderImpl implements FormatProvider {

  protected final Language language;

  protected NumberFormat numberParser;
  protected ProviderMap<NumberFormatKey, NumberFormat> numberFormats;
  protected ProviderMap<NumberFormatKey, NumberFormat> percentFormats;
  protected ProviderMap<NumberFormatKey, NumberFormat> currencyFormats;

  protected ProviderMap<NumberFormatKey, DecimalFormat> decimalFormats;

  /**
   * Constructs a FormatProviderImpl using the default language provided by
   * {@link com.appslandia.common.base.Language#getDefault()} as the underlying
   * language.
   */
  public FormatProviderImpl() {
    this(Language.getDefault());
  }

  public FormatProviderImpl(Language language) {
    this.language = language;
  }

  @Override
  public Language getLanguage() {
    return this.language;
  }

  @Override
  public NumberFormat getNumberParser() {
    if (this.numberParser == null) {
      this.numberParser = NumberFormat.getNumberInstance(this.language.getLocale());

      if (this.numberParser instanceof DecimalFormat) {
        ((DecimalFormat) this.numberParser).setParseBigDecimal(true);
      }
      this.numberParser.setGroupingUsed(false);
    }
    return this.numberParser;
  }

  @Override
  public NumberFormat getNumberFormat(int fractionDigits, RoundingMode roundingMode, boolean grouping) {
    return this.getNumberFormats().get(new NumberFormatKey(fractionDigits, roundingMode, grouping));
  }

  protected ProviderMap<NumberFormatKey, NumberFormat> getNumberFormats() {
    if (this.numberFormats != null) {
      return this.numberFormats;
    }
    return this.numberFormats = new ProviderMap<NumberFormatKey, NumberFormat>((key) -> {
      NumberFormat impl = NumberFormat.getNumberInstance(this.language.getLocale());

      impl.setMaximumFractionDigits(key.fractionDigits);
      impl.setMinimumFractionDigits(key.fractionDigits);

      impl.setRoundingMode(key.roundingMode);
      impl.setGroupingUsed(key.grouping);

      return impl;
    });
  }

  @Override
  public NumberFormat getPercentFormat(int fractionDigits, RoundingMode roundingMode, boolean grouping) {
    return this.getPercentFormats().get(new NumberFormatKey(fractionDigits, roundingMode, grouping));
  }

  protected ProviderMap<NumberFormatKey, NumberFormat> getPercentFormats() {
    if (this.percentFormats != null) {
      return this.percentFormats;
    }
    return this.percentFormats = new ProviderMap<NumberFormatKey, NumberFormat>((key) -> {
      NumberFormat impl = NumberFormat.getPercentInstance(this.language.getLocale());

      impl.setMaximumFractionDigits(key.fractionDigits);
      impl.setMinimumFractionDigits(key.fractionDigits);

      impl.setRoundingMode(key.roundingMode);
      impl.setGroupingUsed(key.grouping);

      return impl;
    });
  }

  @Override
  public NumberFormat getCurrencyFormat(int fractionDigits, RoundingMode roundingMode, boolean grouping) {
    return this.getCurrencyFormats().get(new NumberFormatKey(fractionDigits, roundingMode, grouping));
  }

  protected ProviderMap<NumberFormatKey, NumberFormat> getCurrencyFormats() {
    if (this.currencyFormats != null) {
      return this.currencyFormats;
    }
    return this.currencyFormats = new ProviderMap<NumberFormatKey, NumberFormat>((key) -> {
      NumberFormat impl = NumberFormat.getCurrencyInstance(this.language.getLocale());

      impl.setMaximumFractionDigits(key.fractionDigits);
      impl.setMinimumFractionDigits(key.fractionDigits);

      impl.setRoundingMode(key.roundingMode);
      impl.setGroupingUsed(key.grouping);

      return impl;
    });
  }

  @Override
  public DecimalFormat getDecimalFormat(int fractionDigits, RoundingMode roundingMode) {
    return this.getDecimalFormats().get(new NumberFormatKey(fractionDigits, roundingMode, false));
  }

  protected ProviderMap<NumberFormatKey, DecimalFormat> getDecimalFormats() {
    if (this.decimalFormats != null) {
      return this.decimalFormats;
    }
    return this.decimalFormats = new ProviderMap<NumberFormatKey, DecimalFormat>((key) -> {
      DecimalFormat impl = new DecimalFormat("0." + "0".repeat(key.fractionDigits));
      impl.setGroupingUsed(false);
      impl.setRoundingMode(key.roundingMode);
      return impl;
    });
  }

  static final class NumberFormatKey {
    final int fractionDigits;
    final RoundingMode roundingMode;
    final boolean grouping;

    public NumberFormatKey(int fractionDigits, RoundingMode roundingMode, boolean grouping) {
      this.fractionDigits = fractionDigits;
      this.roundingMode = roundingMode;
      this.grouping = grouping;
    }

    @Override
    public int hashCode() {
      int hash = 1, p = 31;
      hash = p * hash + Integer.hashCode(this.fractionDigits);
      hash = p * hash + Objects.hashCode(this.roundingMode);
      hash = p * hash + Boolean.hashCode(this.grouping);
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      NumberFormatKey that = (NumberFormatKey) obj;
      return (this.fractionDigits == that.fractionDigits) && (this.roundingMode == that.roundingMode)
          && (this.grouping == that.grouping);
    }
  }
}
