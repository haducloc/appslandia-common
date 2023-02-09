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

import java.io.Console;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Function;

import com.appslandia.common.base.DateFormatException;
import com.appslandia.common.base.Out;
import com.appslandia.common.base.Resources;
import com.appslandia.common.crypto.PasswordUtil;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ConsoleUtils {

    private static volatile Scanner __reader;
    private static volatile PrintWriter __writer;

    private static final Object MUTEX_READER = new Object();

    public static Scanner reader() {
	Scanner obj = __reader;
	if (obj == null) {
	    synchronized (MUTEX_READER) {
		if ((obj = __reader) == null) {
		    Console cons = System.console();

		    if (cons != null) {
			__reader = obj = new Scanner(cons.reader());
		    } else {
			__reader = obj = new Scanner(System.in);
		    }
		}
	    }
	}
	return obj;
    }

    private static final Object MUTEX_WRITER = new Object();

    public static PrintWriter writer() {
	PrintWriter obj = __writer;
	if (obj == null) {
	    synchronized (MUTEX_WRITER) {
		if ((obj = __writer) == null) {

		    Console cons = System.console();
		    if (cons != null) {
			__writer = obj = cons.writer();
		    } else {
			__writer = obj = new PrintWriter(System.out, true);
		    }
		}
	    }
	}
	return obj;
    }

    public static String readLongString(String promptText, String variableName) {
	return readLongString(promptText, variableName, true, null);
    }

    public static String readLongString(String promptText, String variableName, boolean required) {
	return readLongString(promptText, variableName, required, null);
    }

    public static String readLongString(String promptText, String variableName, boolean required, Function<String, Boolean> validator) {
	Asserts.notNull(promptText);

	writer().println(promptText);
	boolean invalidValue = false;

	while (true) {

	    if (invalidValue) {
		writer().println(getInvalidEnterAgain(variableName));
	    }

	    // String trunks
	    List<String> strings = new ArrayList<>();

	    while (reader().hasNextLine()) {
		String valueStr = reader().nextLine().trim();

		if (valueStr.isEmpty()) {
		    continue;
		}

		if (valueStr.endsWith("/")) {
		    valueStr = valueStr.substring(0, valueStr.length() - 1).trim();

		    if (!valueStr.isEmpty()) {
			strings.add(valueStr);
		    }

		    break;
		}

		strings.add(valueStr);
	    }

	    // Long String
	    String longStr = String.join(" ", strings);
	    if (longStr.isEmpty()) {

		if (!required) {
		    return null;
		} else {
		    invalidValue = true;
		    continue;
		}
	    }

	    if ((validator != null) && !validator.apply(longStr)) {
		invalidValue = true;
		continue;
	    }
	    return longStr;
	}
    }

    public static String readPassword(String promptText, String variableName) {
	return readPassword(promptText, variableName, true, (p) -> PasswordUtil.isValid(p));
    }

    public static String readPassword(String promptText, String variableName, boolean required) {
	return readPassword(promptText, variableName, required, (p) -> PasswordUtil.isValid(p));
    }

    public static String readPassword(String promptText, String variableName, boolean required, Function<String, Boolean> validator) {
	Asserts.notNull(promptText);

	Console cons = System.console();
	if (cons != null) {

	    writer().println(promptText);
	    boolean invalidValue = false;

	    while (true) {

		if (invalidValue) {
		    writer().println(getInvalidEnterAgain(variableName));
		}

		// Read Password
		char[] password = cons.readPassword();
		if (password.length == 0) {

		    if (!required) {
			return null;
		    } else {
			invalidValue = true;
			continue;
		    }
		}

		// Validate value
		String value = new String(password);
		if ((validator != null) && !validator.apply(value)) {

		    invalidValue = true;
		    continue;
		}
		return value;
	    }

	} else {
	    return readString(promptText, variableName, required, validator);
	}
    }

    public static String readPassword2(String promptText, String variableName) {
	return readPassword2(promptText, variableName, true, (p) -> PasswordUtil.isValid(p));
    }

    public static String readPassword2(String promptText, String variableName, boolean required) {
	return readPassword2(promptText, variableName, required, (p) -> PasswordUtil.isValid(p));
    }

    public static String readPassword2(String promptText, String variableName, boolean required, Function<String, Boolean> validator) {
	Asserts.notNull(promptText);
	Asserts.notNull(variableName);

	Console cons = System.console();
	if (cons != null) {

	    boolean unmatchedValues = false;
	    while (true) {

		if (unmatchedValues) {
		    writer().println(getUnmatchedValueText(variableName));
		}

		// Enter value
		String readValue = readPassword(promptText, variableName, required, validator);

		// Confirm Value
		writer().println(getConfirmValue(variableName));

		char[] confirmValue = cons.readPassword();
		String confirmPwd = (confirmValue.length > 0) ? new String(confirmValue) : null;

		// Compare values
		if (!Objects.equals(readValue, confirmPwd)) {
		    unmatchedValues = true;
		    continue;
		}
		return readValue;
	    }
	} else {
	    return readString2(promptText, variableName, required, validator);
	}
    }

    public static String readString2(String promptText, String variableName) {
	return readString2(promptText, variableName, true, null);
    }

    public static String readString2(String promptText, String variableName, boolean required) {
	return readString2(promptText, variableName, required, null);
    }

    public static String readString2(String promptText, String variableName, boolean required, Function<String, Boolean> validator) {
	Asserts.notNull(promptText);
	Asserts.notNull(variableName);

	boolean unmatchedValues = false;
	while (true) {

	    if (unmatchedValues) {
		writer().println(getUnmatchedValueText(variableName));
	    }

	    // Enter value
	    String readValue = readString(promptText, variableName, required, validator);

	    // Confirm Value
	    writer().println(getConfirmValue(variableName));

	    String confirmValue = null;
	    if (reader().hasNextLine()) {
		confirmValue = StringUtils.trimToNull(reader().nextLine());
	    }

	    // Compare values
	    if (!Objects.equals(readValue, confirmValue)) {
		unmatchedValues = true;
		continue;
	    }
	    return readValue;
	}
    }

    public static boolean readBoolean2(String promptText, String variableName) {
	return readBoolean2(promptText, variableName, true);
    }

    public static Boolean readBoolean2(String promptText, String variableName, boolean required) {
	Asserts.notNull(promptText);
	Asserts.notNull(variableName);

	boolean unmatchedValues = false;
	while (true) {

	    if (unmatchedValues) {
		writer().println(getUnmatchedValueText(variableName));
	    }

	    // Enter value
	    Boolean readValue = readBoolean(promptText, variableName, required);

	    // Confirm Value
	    writer().println(getConfirmValue(variableName));

	    String confirmValue = null;
	    if (reader().hasNextLine()) {
		confirmValue = StringUtils.trimToNull(reader().nextLine());
	    }

	    if (confirmValue == null) {
		if (readValue == null) {
		    return null;
		}

		unmatchedValues = true;
		continue;
	    }

	    Out<Boolean> parsedValid = new Out<>();
	    boolean confirmVal = ParseUtils.parseBool(confirmValue, parsedValid);

	    if (!parsedValid.value || (readValue != confirmVal)) {
		unmatchedValues = true;
		continue;
	    }
	    return readValue;
	}
    }

    public static int readInteger2(String promptText, String variableName) {
	return readInteger2(promptText, variableName, true, null);
    }

    public static Integer readInteger2(String promptText, String variableName, boolean required) {
	return readInteger2(promptText, variableName, required, null);
    }

    public static Integer readInteger2(String promptText, String variableName, boolean required, Function<Integer, Boolean> validator) {
	Asserts.notNull(promptText);
	Asserts.notNull(variableName);

	boolean unmatchedValues = false;
	while (true) {

	    if (unmatchedValues) {
		writer().println(getUnmatchedValueText(variableName));
	    }

	    // Enter value
	    Integer readValue = readInteger(promptText, variableName, required, validator);

	    // Confirm Value
	    writer().println(getConfirmValue(variableName));

	    String confirmValue = null;
	    if (reader().hasNextLine()) {
		confirmValue = StringUtils.trimToNull(reader().nextLine());
	    }

	    if (confirmValue == null) {
		if (readValue == null) {
		    return null;
		}

		unmatchedValues = true;
		continue;
	    }

	    Out<Boolean> parsedValid = new Out<>();
	    int confirmVal = ParseUtils.parseInt(confirmValue, parsedValid);

	    if (!parsedValid.value || (readValue != confirmVal)) {
		unmatchedValues = true;
		continue;
	    }
	    return readValue;
	}
    }

    public static double readDouble2(String promptText, String variableName) {
	return readDouble2(promptText, variableName, true, null);
    }

    public static Double readDouble2(String promptText, String variableName, boolean required) {
	return readDouble2(promptText, variableName, required, null);
    }

    public static Double readDouble2(String promptText, String variableName, boolean required, Function<Double, Boolean> validator) {
	Asserts.notNull(promptText);
	Asserts.notNull(variableName);

	boolean unmatchedValues = false;
	while (true) {

	    if (unmatchedValues) {
		writer().println(getUnmatchedValueText(variableName));
	    }

	    // Enter value
	    Double readValue = readDouble(promptText, variableName, required, validator);

	    // Confirm Value
	    writer().println(getConfirmValue(variableName));

	    String confirmValue = null;
	    if (reader().hasNextLine()) {
		confirmValue = StringUtils.trimToNull(reader().nextLine());
	    }

	    if (confirmValue == null) {
		if (readValue == null) {
		    return null;
		}

		unmatchedValues = true;
		continue;
	    }

	    Out<Boolean> parsedValid = new Out<>();
	    double confirmVal = ParseUtils.parseDouble(confirmValue, parsedValid);

	    if (!parsedValid.value || (readValue != confirmVal)) {
		unmatchedValues = true;
		continue;
	    }
	    return readValue;
	}
    }

    public static Date readDate2(String promptText, String variableName, String pattern) {
	return readDate2(promptText, variableName, pattern, true, null);
    }

    public static Date readDate2(String promptText, String variableName, String pattern, boolean required) {
	return readDate2(promptText, variableName, pattern, required, null);
    }

    public static Date readDate2(String promptText, String variableName, String pattern, boolean required, Function<Date, Boolean> validator) {
	Asserts.notNull(promptText);
	Asserts.notNull(variableName);
	Asserts.notNull(pattern);

	boolean unmatchedValues = false;
	while (true) {

	    if (unmatchedValues) {
		writer().println(getUnmatchedValueText(variableName));
	    }

	    // Enter value
	    Date readValue = readDate(promptText, variableName, pattern, required, validator);

	    // Confirm Value
	    writer().println(getConfirmValue(variableName));

	    String confirmValue = null;
	    if (reader().hasNextLine()) {
		confirmValue = StringUtils.trimToNull(reader().nextLine());
	    }

	    if (confirmValue == null) {
		if (readValue == null) {
		    return null;
		}

		unmatchedValues = true;
		continue;
	    }

	    Out<Boolean> parsedValid = new Out<>();
	    Date confirmVal = ParseUtils.parseDate(confirmValue, pattern, parsedValid);

	    if (!parsedValid.value || !Objects.equals(readValue, confirmVal)) {
		unmatchedValues = true;
		continue;
	    }
	    return readValue;
	}
    }

    public static File readDir(String promptText, String variableName) {
	return readDir(promptText, variableName, true, null);
    }

    public static File readDir(String promptText, String variableName, boolean required) {
	return readDir(promptText, variableName, required, null);
    }

    public static File readDir(String promptText, String variableName, boolean required, Function<File, Boolean> validator) {
	return read(promptText, variableName, required, (value) -> {

	    File dir = new File(value);

	    if (dir.exists() && !dir.isHidden() && dir.isDirectory()) {
		return dir;
	    }

	    return null;

	}, validator);
    }

    public static File readFile(String promptText, String variableName) {
	return readFile(promptText, variableName, true, null);
    }

    public static File readFile(String promptText, String variableName, boolean required) {
	return readFile(promptText, variableName, required, null);
    }

    public static File readFile(String promptText, String variableName, boolean required, Function<File, Boolean> validator) {
	return read(promptText, variableName, required, (value) -> {

	    File file = new File(value);

	    if (file.exists() && !file.isHidden() && file.isFile()) {
		return file;
	    }

	    return null;

	}, validator);
    }

    public static String readString(String promptText, String variableName) {
	return readString(promptText, variableName, true, null);
    }

    public static String readString(String promptText, String variableName, boolean required) {
	return readString(promptText, variableName, required, null);
    }

    public static String readString(String promptText, String variableName, boolean required, Function<String, Boolean> validator) {
	return read(promptText, variableName, required, (value) -> value, validator);
    }

    public static boolean readBoolean(String promptText, String variableName) {
	return readBoolean(promptText, variableName, true);
    }

    public static Boolean readBoolean(String promptText, String variableName, boolean required) {
	return read(promptText, variableName, required, (value) -> {

	    boolean isTrue = ParseUtils.isTrueValue(value);
	    boolean isFalse = !isTrue && ParseUtils.isFalseValue(value);

	    if (isTrue) {
		return true;
	    }

	    if (isFalse) {
		return false;
	    }

	    return null;

	}, null);
    }

    public static int readInteger(String promptText, String variableName) {
	return readInteger(promptText, variableName, true, null);
    }

    public static Integer readInteger(String promptText, String variableName, boolean required) {
	return readInteger(promptText, variableName, required, null);
    }

    public static Integer readInteger(String promptText, String variableName, boolean required, Function<Integer, Boolean> validator) {
	return read(promptText, variableName, required, (value) -> {

	    try {
		return Integer.parseInt(value);

	    } catch (NumberFormatException ex) {
		return null;
	    }

	}, validator);
    }

    public static double readDouble(String promptText, String variableName) {
	return readDouble(promptText, variableName, true, null);
    }

    public static Double readDouble(String promptText, String variableName, boolean required) {
	return readDouble(promptText, variableName, required, null);
    }

    public static Double readDouble(String promptText, String variableName, boolean required, Function<Double, Boolean> validator) {
	return read(promptText, variableName, required, (value) -> {

	    try {
		return Double.parseDouble(value);

	    } catch (NumberFormatException ex) {
		return null;
	    }

	}, validator);
    }

    public static Date readDate(String promptText, String variableName, String pattern) {
	return readDate(promptText, variableName, pattern, true, null);
    }

    public static Date readDate(String promptText, String variableName, String pattern, boolean required) {
	return readDate(promptText, variableName, pattern, required, null);
    }

    public static Date readDate(String promptText, String variableName, String pattern, boolean required, Function<Date, Boolean> validator) {
	return read(promptText, variableName, required, (value) -> {

	    try {
		return DateUtils.parse(value, pattern);

	    } catch (DateFormatException ex) {
		return null;
	    }

	}, validator);
    }

    public static <T> T read(String promptText, String variableName, boolean required, Function<String, T> converter, Function<T, Boolean> validator) {
	Asserts.notNull(promptText);

	writer().println(promptText);
	boolean invalidValue = false;

	while (true) {

	    if (invalidValue) {
		writer().println(getInvalidEnterAgain(variableName));
	    }

	    if (reader().hasNextLine()) {
		String valueStr = reader().nextLine().trim();

		if (valueStr.isEmpty()) {

		    if (!required) {
			return null;
		    } else {
			invalidValue = true;
			continue;
		    }
		}

		// Parse value
		T value = converter.apply(valueStr);

		if (value == null) {
		    invalidValue = true;
		    continue;
		}

		if ((validator != null) && !validator.apply(value)) {
		    invalidValue = true;
		    continue;
		}
		return value;
	    }
	}
    }

    static String getInvalidEnterAgain(String variableName) {
	if (variableName == null) {
	    return Resources.getString("console_utils.invalid_enter_again");
	}

	return Resources.getString("console_utils.invalid_enter_again_varname", variableName);
    }

    static String getConfirmValue(String variableName) {
	return Resources.getString("console_utils.confirm_value", variableName);
    }

    static String getUnmatchedValueText(String variableName) {
	return Resources.getString("console_utils.unmatched_values", variableName);
    }
}
