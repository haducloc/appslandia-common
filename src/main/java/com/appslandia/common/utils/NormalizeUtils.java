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

package com.appslandia.common.utils;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class NormalizeUtils {

    public interface DecomposedCharacterConverter {
	char convert(char decomposedCharacter);
    }

    private static DecomposedCharacterConverter decomposedCharacterConverter;

    public static void setDecomposedCharacterConverter(DecomposedCharacterConverter converter) {
	Asserts.isNull(decomposedCharacterConverter);
	decomposedCharacterConverter = converter;
    }

    private static final Pattern STRIP_ACCENTS_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    public static String unaccent(String str) {
	if (str == null) {
	    return null;
	}
	StringBuilder decomposed = new StringBuilder(Normalizer.normalize(str, Normalizer.Form.NFD));
	if (decomposedCharacterConverter != null) {
	    for (int i = 0; i < decomposed.length(); i++) {
		char converted = decomposedCharacterConverter.convert(decomposed.charAt(i));

		if (converted != decomposed.charAt(i)) {
		    decomposed.deleteCharAt(i);
		    decomposed.insert(i, converted);
		}
	    }
	}
	return STRIP_ACCENTS_PATTERN.matcher(decomposed).replaceAll(StringUtils.EMPTY_STRING);
    }

    private static final Pattern[] WTSP_PUNCT_HYPHEN_PATTERNS = PatternUtils.compile("\\s+|\\p{Punct}+", "-{2,}");

    public static String normalizeLabel(String str) {
	str = normalize(str, WTSP_PUNCT_HYPHEN_PATTERNS, "-");
	str = StringUtils.trimToNull(str, '-');
	return StringUtils.toLowerCase(str, Locale.ROOT);
    }

    private static final Pattern[] CRLF3_PATTERNS = PatternUtils.compile("(\r?\n){3,}");

    public static String normalizeText(String text) {
	if (text == null) {
	    return null;
	}
	return normalize(text, CRLF3_PATTERNS, StringUtils.DOUBLE_LINE_SEP);
    }

    private static final Pattern[] WTSP_PATTERNS = PatternUtils.compile("\\s+");

    public static String normalizeString(String simpleStr) {
	if (simpleStr == null) {
	    return null;
	}
	return normalize(simpleStr, WTSP_PATTERNS, " ");
    }

    public static String removeSp(String str) {
	if (str == null) {
	    return null;
	}
	return normalize(str, WTSP_PATTERNS, "");
    }

    private static final Pattern[] WTSP_PUNCT_PATTERNS = PatternUtils.compile("\\s+|\\p{Punct}+");

    public static String removeSpPunct(String str) {
	if (str == null) {
	    return null;
	}
	return normalize(str, WTSP_PUNCT_PATTERNS, "");
    }

    private static final Pattern[] NON_DIGITS_PATTERNS = PatternUtils.compile("[^\\d]+");

    public static String digitOnly(String str) {
	if (str == null) {
	    return null;
	}
	return normalize(str, NON_DIGITS_PATTERNS, "");
    }

    public static String normalize(String str, Pattern[] matchers, String replacement) {
	if (str == null) {
	    return null;
	}
	for (Pattern p : matchers) {
	    str = p.matcher(str).replaceAll(Matcher.quoteReplacement(replacement));
	}
	return StringUtils.trimToNull(str);
    }

    public static String stringAsID(String str) {
	if (str == null) {
	    return "null";
	}
	str = removeSpPunct(str);
	return (str != null) ? StringUtils.toLowerCase(str, Locale.ROOT) : "null";
    }

    public static String valuesAsID(Object... values) {
	Asserts.hasElements(values);

	return Arrays.stream(values).map(v -> {
	    return stringAsID(String.valueOf(v));

	}).collect(Collectors.joining("-"));
    }
}
