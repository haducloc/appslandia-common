// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;

/**
 *
 * @author Loc Ha
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
   * {@link com.appslandia.common.base.Language#getDefault()} as the underlying language.
   */
  public FormatProviderImpl() {
    this(Language.getDefault());
  }

  public FormatProviderImpl(Language language) {
    this.language = language;
  }

  @Override
  public Language getLanguage() {
    return language;
  }

  @Override
  public NumberFormat getNumberParser() {
    if (numberParser == null) {
      numberParser = NumberFormat.getNumberInstance(language.getLocale());

      if (numberParser instanceof DecimalFormat) {
        ((DecimalFormat) numberParser).setParseBigDecimal(true);
      }
      numberParser.setGroupingUsed(false);
    }
    return numberParser;
  }

  @Override
  public NumberFormat getNumberFormat(RoundingMode roundingMode, int fractionDigits, boolean grouping) {
    return getNumberFormats().get(new NumberFormatKey(roundingMode, fractionDigits, grouping));
  }

  protected ProviderMap<NumberFormatKey, NumberFormat> getNumberFormats() {
    if (numberFormats != null) {
      return numberFormats;
    }
    return numberFormats = new ProviderMap<>((key) -> {
      var impl = NumberFormat.getNumberInstance(language.getLocale());

      impl.setMaximumFractionDigits(key.fractionDigits);
      impl.setMinimumFractionDigits(key.fractionDigits);

      if (key.roundingMode != null) {
        impl.setRoundingMode(key.roundingMode);
      }
      impl.setGroupingUsed(key.grouping);

      return impl;
    });
  }

  @Override
  public NumberFormat getPercentFormat(RoundingMode roundingMode, int fractionDigits, boolean grouping) {
    return getPercentFormats().get(new NumberFormatKey(roundingMode, fractionDigits, grouping));
  }

  protected ProviderMap<NumberFormatKey, NumberFormat> getPercentFormats() {
    if (percentFormats != null) {
      return percentFormats;
    }
    return percentFormats = new ProviderMap<>((key) -> {
      var impl = NumberFormat.getPercentInstance(language.getLocale());

      impl.setMaximumFractionDigits(key.fractionDigits);
      impl.setMinimumFractionDigits(key.fractionDigits);

      if (key.roundingMode != null) {
        impl.setRoundingMode(key.roundingMode);
      }
      impl.setGroupingUsed(key.grouping);

      return impl;
    });
  }

  @Override
  public NumberFormat getCurrencyFormat(RoundingMode roundingMode, int fractionDigits, boolean grouping) {
    return getCurrencyFormats().get(new NumberFormatKey(roundingMode, fractionDigits, grouping));
  }

  protected ProviderMap<NumberFormatKey, NumberFormat> getCurrencyFormats() {
    if (currencyFormats != null) {
      return currencyFormats;
    }
    return currencyFormats = new ProviderMap<>((key) -> {
      var impl = NumberFormat.getCurrencyInstance(language.getLocale());

      impl.setMaximumFractionDigits(key.fractionDigits);
      impl.setMinimumFractionDigits(key.fractionDigits);

      if (key.roundingMode != null) {
        impl.setRoundingMode(key.roundingMode);
      }
      impl.setGroupingUsed(key.grouping);

      return impl;
    });
  }

  @Override
  public DecimalFormat getDecimalFormat(RoundingMode roundingMode, int fractionDigits) {
    return getDecimalFormats().get(new NumberFormatKey(roundingMode, fractionDigits, false));
  }

  protected ProviderMap<NumberFormatKey, DecimalFormat> getDecimalFormats() {
    if (decimalFormats != null) {
      return decimalFormats;
    }
    return decimalFormats = new ProviderMap<>((key) -> {
      var impl = new DecimalFormat("0." + "0".repeat(key.fractionDigits));
      if (key.roundingMode != null) {
        impl.setRoundingMode(key.roundingMode);
      }
      impl.setGroupingUsed(false);
      return impl;
    });
  }

  static final class NumberFormatKey {
    final RoundingMode roundingMode;
    final int fractionDigits;
    final boolean grouping;

    public NumberFormatKey(RoundingMode roundingMode, int fractionDigits, boolean grouping) {
      this.roundingMode = roundingMode;
      this.fractionDigits = fractionDigits;
      this.grouping = grouping;
    }

    @Override
    public int hashCode() {
      int hash = 1, p = 31;
      hash = p * hash + Objects.hashCode(roundingMode);
      hash = p * hash + Integer.hashCode(fractionDigits);
      hash = p * hash + Boolean.hashCode(grouping);
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      var that = (NumberFormatKey) obj;
      return (roundingMode == that.roundingMode) && (fractionDigits == that.fractionDigits)
          && (grouping == that.grouping);
    }
  }
}
