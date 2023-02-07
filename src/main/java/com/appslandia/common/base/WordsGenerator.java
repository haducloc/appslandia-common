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

import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Pattern;

import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.CharUtils;
import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class WordsGenerator extends InitializeObject implements TextGenerator {

    private static final char[] ALPHABET_LOWER = CharUtils.toCharRanges("a-z");
    private static final char[] ALPHABET_UPPER = CharUtils.toCharRanges("A-Z");
    private static final char[] ALPHABET_DIGITS = CharUtils.toCharRanges("0-9");

    private static final Pattern DIGITS_PATTERN = Pattern.compile("[\\d]+");
    private static final Pattern DIGITS_AZ_PATTERN = Pattern.compile("[a-zA-Z\\d]+");
    private static final Pattern DIGITS_AZ_LOWER_PATTERN = Pattern.compile("[a-z\\d]+");
    private static final Pattern DIGITS_AZ_UPPER_PATTERN = Pattern.compile("[A-Z\\d]+");

    private int length;
    private Alphabet alphabet;

    final Random random = new SecureRandom();

    public enum Alphabet {
	DIGITS, AZ, AZ_UPPER, AZ_LOWER, DIGITS_AZ_UPPER, DIGITS_AZ_LOWER, DIGITS_AZ
    }

    @Override
    protected void init() throws Exception {
	Asserts.isTrue(this.length > 0, "length is required.");
	this.alphabet = ValueUtils.valueOrAlt(this.alphabet, Alphabet.DIGITS_AZ);
    }

    @Override
    public String generate() {
	initialize();
	switch (this.alphabet) {
	case DIGITS_AZ:
	    return generate(new char[][] { ALPHABET_DIGITS, ALPHABET_UPPER, ALPHABET_LOWER });
	case DIGITS_AZ_UPPER:
	    return generate(new char[][] { ALPHABET_DIGITS, ALPHABET_UPPER });
	case DIGITS_AZ_LOWER:
	    return generate(new char[][] { ALPHABET_DIGITS, ALPHABET_LOWER });

	case AZ:
	    return generate(new char[][] { ALPHABET_UPPER, ALPHABET_LOWER });
	case AZ_UPPER:
	    return generate(new char[][] { ALPHABET_UPPER });
	case AZ_LOWER:
	    return generate(new char[][] { ALPHABET_LOWER });

	default:
	    return generate(new char[][] { ALPHABET_DIGITS });
	}
    }

    private String generate(char[][] sources) {
	char[] rdChars = CharUtils.randomChars(this.length, sources, this.random);
	return new String(rdChars, 0, this.length);
    }

    @Override
    public boolean verify(String value) {
	Asserts.notNull(value);
	if (value.length() != this.length)
	    return false;

	switch (this.alphabet) {
	case DIGITS_AZ:
	    return DIGITS_AZ_PATTERN.matcher(value).matches();
	case DIGITS_AZ_UPPER:
	    return DIGITS_AZ_UPPER_PATTERN.matcher(value).matches();
	case DIGITS_AZ_LOWER:
	    return DIGITS_AZ_LOWER_PATTERN.matcher(value).matches();
	default:
	    return DIGITS_PATTERN.matcher(value).matches();
	}
    }

    public WordsGenerator setLength(int length) {
	assertNotInitialized();
	this.length = length;
	return this;
    }

    public WordsGenerator setAlphabet(Alphabet alphabet) {
	assertNotInitialized();
	this.alphabet = alphabet;
	return this;
    }
}
