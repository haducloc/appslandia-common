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

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class Arguments {

    public static void isTrue(boolean expression) {
	if (!expression) {
	    throw new IllegalArgumentException("The expression must be true.");
	}
    }

    public static void isTrue(boolean expression, String errorMessage) {
	if (!expression) {
	    throw new IllegalArgumentException(errorMessage);
	}
    }

    public static void isTrue(boolean expression, Supplier<String> errorMessage) {
	if (!expression) {
	    throw new IllegalArgumentException(errorMessage.get());
	}
    }

    public static <T> T notNull(T obj) {
	if (obj == null) {
	    throw new IllegalArgumentException("The obj must be not null.");
	}
	return obj;
    }

    public static <T> T notNull(T obj, String errorMessage) {
	if (obj == null) {
	    throw new IllegalArgumentException(errorMessage);
	}
	return obj;
    }

    public static <T> T notNull(T obj, Supplier<String> errorMessage) {
	if (obj == null) {
	    throw new IllegalArgumentException(errorMessage.get());
	}
	return obj;
    }

    public static void isNull(Object obj) {
	if (obj != null) {
	    throw new IllegalArgumentException("The obj must be null.");
	}
    }

    public static void isNull(Object obj, String errorMessage) {
	if (obj != null) {
	    throw new IllegalArgumentException(errorMessage);
	}
    }

    public static void isNull(Object obj, Supplier<String> errorMessage) {
	if (obj != null) {
	    throw new IllegalArgumentException(errorMessage.get());
	}
    }

    public static <T> T[] hasElements(T[] array) {
	if ((array == null) || (array.length == 0)) {
	    throw new IllegalArgumentException("The array must have elements.");
	}
	return array;
    }

    public static <T> T[] hasElements(T[] array, String errorMessage) {
	if ((array == null) || (array.length == 0)) {
	    throw new IllegalArgumentException(errorMessage);
	}
	return array;
    }

    public static <T> T[] hasElements(T[] elements, Supplier<String> errorMessage) {
	if ((elements == null) || (elements.length == 0)) {
	    throw new IllegalArgumentException(errorMessage.get());
	}
	return elements;
    }

    public static <T extends Collection<?>> T hasElements(T collection) {
	if ((collection == null) || (collection.size() == 0)) {
	    throw new IllegalArgumentException("The collection must have elements.");
	}
	return collection;
    }

    public static <T extends Collection<?>> T hasElements(T collection, String errorMessage) {
	if ((collection == null) || (collection.size() == 0)) {
	    throw new IllegalArgumentException(errorMessage);
	}
	return collection;
    }

    public static <T extends Collection<?>> T hasElements(T collection, Supplier<String> errorMessage) {
	if ((collection == null) || (collection.size() == 0)) {
	    throw new IllegalArgumentException(errorMessage.get());
	}
	return collection;
    }

    public static <M extends Map<?, ?>> M hasEntries(M map) {
	if ((map == null) || (map.size() == 0)) {
	    throw new IllegalArgumentException("The map must have entries.");
	}
	return map;
    }

    public static <M extends Map<?, ?>> M hasEntries(M map, String errorMessage) {
	if ((map == null) || (map.size() == 0)) {
	    throw new IllegalArgumentException(errorMessage);
	}
	return map;
    }

    public static <M extends Map<?, ?>> M hasEntries(M map, Supplier<String> errorMessage) {
	if ((map == null) || (map.size() == 0)) {
	    throw new IllegalArgumentException(errorMessage.get());
	}
	return map;
    }
}
