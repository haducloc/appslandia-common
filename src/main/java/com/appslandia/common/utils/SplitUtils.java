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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SplitUtils {

    private static final Pattern NEWLINE_SEP_PATTERN = Pattern.compile("(\r?\n)+");

    public static String[] splitByLine(String str) {
	return splitByLine(str, SplitOptions.EXCLUDE_NULL);
    }

    public static String[] splitByLine(String str, SplitOptions splitOptions) {
	return split(str, NEWLINE_SEP_PATTERN, splitOptions);
    }

    public static String[] split(String str, Pattern separator) {
	return split(str, separator, SplitOptions.EXCLUDE_NULL);
    }

    public static String[] split(String str, Pattern separator, SplitOptions splitOptions) {
	if (str == null) {
	    return StringUtils.EMPTY_ARRAY;
	}
	String[] items = separator.split(str);
	List<String> list = new ArrayList<>(items.length);

	for (String item : items) {
	    item = convertItem(item, splitOptions);

	    if (item != null) {
		list.add(item);
	    } else {
		if (splitOptions != SplitOptions.EXCLUDE_NULL) {
		    list.add(null);
		}
	    }
	}
	return list.toArray(new String[list.size()]);
    }

    public static String[] splitByComma(String str) {
	return splitByComma(str, SplitOptions.EXCLUDE_NULL);
    }

    public static String[] splitByComma(String str, SplitOptions splitOptions) {
	return split(str, ',', splitOptions);
    }

    public static String[] split(String str, char separator) {
	return split(str, separator, SplitOptions.EXCLUDE_NULL);
    }

    public static String[] split(String str, char separator, SplitOptions splitOptions) {
	if (str == null) {
	    return StringUtils.EMPTY_ARRAY;
	}

	List<String> list = new ArrayList<>();
	StringBuilder currentItem = new StringBuilder();
	boolean escapeNextChar = false;

	for (int i = 0; i < str.length(); i++) {
	    char c = str.charAt(i);

	    if (escapeNextChar) {
		currentItem.append(c);
		escapeNextChar = false;

	    } else if (c == '\\') {
		escapeNextChar = true;

	    } else if (c == separator) {
		String item = convertItem(currentItem.toString(), splitOptions);

		if (item != null) {
		    list.add(item);
		} else {
		    if (splitOptions != SplitOptions.EXCLUDE_NULL) {
			list.add(null);
		    }
		}

		currentItem.setLength(0);
	    } else {
		currentItem.append(c);
	    }
	}

	// Last item
	String item = convertItem(currentItem.toString(), splitOptions);

	if (item != null) {
	    list.add(item);
	} else {
	    if (splitOptions != SplitOptions.EXCLUDE_NULL) {
		list.add(null);
	    }
	}
	return list.toArray(new String[list.size()]);
    }

    private static String convertItem(String item, SplitOptions splitOptions) {
	if (splitOptions == null || splitOptions == SplitOptions.NONE) {
	    return item;
	} else {
	    item = item.trim();
	    return !item.isEmpty() ? item : null;
	}
    }
}
