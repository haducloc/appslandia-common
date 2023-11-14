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

import java.math.BigDecimal;
import java.util.Map;
import java.util.regex.Pattern;

import com.appslandia.common.base.AssertException;
import com.appslandia.common.base.BoolFormatException;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SYS {

	// System.getProperty

	public static String getProp(String key) {
		return StringUtils.trimToNull(System.getProperty(key));
	}

	public static String getProp(String key, String defaultValue) {
		String value = getProp(key);
		return (value != null) ? value : defaultValue;
	}

	public static String getPropReq(String key) {
		String value = getProp(key);
		if (value == null) {
			throw new AssertException(STR.fmt("No value found for the given property '{}'.", key));
		}
		return value;
	}

	public static boolean getBoolProp(String key) throws BoolFormatException {
		String value = getPropReq(key);
		return ParseUtils.parseBool(value);
	}

	public static boolean getBoolProp(String key, boolean defaultValIfInvalid) {
		String value = getProp(key);
		return (value == null) ? ParseUtils.parseBool(value, defaultValIfInvalid) : defaultValIfInvalid;
	}

	public static Boolean getBoolPropOpt(String key) throws BoolFormatException {
		String value = getProp(key);
		return (value != null) ? ParseUtils.parseBool(value) : null;
	}

	public static int getIntProp(String key) throws NumberFormatException {
		String value = getPropReq(key);
		return Integer.parseInt(value);
	}

	public static int getIntProp(String key, int defaultValIfInvalid) {
		String value = getProp(key);
		return (value == null) ? ParseUtils.parseInt(value, defaultValIfInvalid) : defaultValIfInvalid;
	}

	public static Integer getIntPropOpt(String key) throws NumberFormatException {
		String value = getProp(key);
		return (value != null) ? ParseUtils.parseInt(value) : null;
	}

	public static long getLongProp(String key) throws NumberFormatException {
		String value = getPropReq(key);
		return Long.parseLong(value);
	}

	public static long getLongProp(String key, long defaultValIfInvalid) {
		String value = getProp(key);
		return (value == null) ? ParseUtils.parseLong(value, defaultValIfInvalid) : defaultValIfInvalid;
	}

	public static Long getLongPropOpt(String key) throws NumberFormatException {
		String value = getProp(key);
		return (value != null) ? ParseUtils.parseLong(value) : null;
	}

	public static double getDoubleProp(String key) throws NumberFormatException {
		String value = getPropReq(key);
		return Double.parseDouble(value);
	}

	public static double getDoubleProp(String key, double defaultValIfInvalid) {
		String value = getProp(key);
		return (value == null) ? ParseUtils.parseDouble(value, defaultValIfInvalid) : defaultValIfInvalid;
	}

	public static Double getDoublePropOpt(String key) throws NumberFormatException {
		String value = getProp(key);
		return (value != null) ? ParseUtils.parseDouble(value) : null;
	}

	public static BigDecimal getDecimalPropReq(String key) throws NumberFormatException {
		String value = getPropReq(key);
		return new BigDecimal(value);
	}

	public static BigDecimal getDecimalProp(String key) throws NumberFormatException {
		String value = getProp(key);
		return (value != null) ? new BigDecimal(value) : null;
	}

	public static BigDecimal getDecimalProp(String key, double defaultValIfInvalid) {
		String value = getProp(key);
		return ParseUtils.parseDecimal(value, defaultValIfInvalid);
	}

	// System.getenv

	public static String getEnv(String key) {
		return StringUtils.trimToNull(System.getenv(key));
	}

	public static String getEnv(String key, String defaultValue) {
		String value = getEnv(key);
		return (value != null) ? value : defaultValue;
	}

	public static String getEnvReq(String key) {
		String value = getEnv(key);
		if (value == null) {
			throw new AssertException(STR.fmt("No value found for the given env '{}'.", key));
		}
		return value;
	}

	public static boolean getBoolEnv(String key) throws BoolFormatException {
		String value = getEnvReq(key);
		return ParseUtils.parseBool(value);
	}

	public static boolean getBoolEnv(String key, boolean defaultValIfInvalid) {
		String value = getEnv(key);
		return (value == null) ? ParseUtils.parseBool(value, defaultValIfInvalid) : defaultValIfInvalid;
	}

	public static Boolean getBoolEnvOpt(String key) throws BoolFormatException {
		String value = getEnv(key);
		return (value != null) ? ParseUtils.parseBool(value) : null;
	}

	public static int getIntEnv(String key) throws NumberFormatException {
		String value = getEnvReq(key);
		return Integer.parseInt(value);
	}

	public static int getIntEnv(String key, int defaultValIfInvalid) {
		String value = getEnv(key);
		return (value == null) ? ParseUtils.parseInt(value, defaultValIfInvalid) : defaultValIfInvalid;
	}

	public static Integer getIntEnvOpt(String key) throws NumberFormatException {
		String value = getEnv(key);
		return (value != null) ? ParseUtils.parseInt(value) : null;
	}

	public static long getLongEnv(String key) throws NumberFormatException {
		String value = getEnvReq(key);
		return Long.parseLong(value);
	}

	public static long getLongEnv(String key, long defaultValIfInvalid) {
		String value = getEnv(key);
		return (value == null) ? ParseUtils.parseLong(value, defaultValIfInvalid) : defaultValIfInvalid;
	}

	public static Long getLongEnvOpt(String key) throws NumberFormatException {
		String value = getEnv(key);
		return (value != null) ? ParseUtils.parseLong(value) : null;
	}

	public static double getDoubleEnv(String key) throws NumberFormatException {
		String value = getEnvReq(key);
		return Double.parseDouble(value);
	}

	public static double getDoubleEnv(String key, double defaultValIfInvalid) {
		String value = getEnv(key);
		return (value == null) ? ParseUtils.parseDouble(value, defaultValIfInvalid) : defaultValIfInvalid;
	}

	public static Double getDoubleEnvOpt(String key) throws NumberFormatException {
		String value = getEnv(key);
		return (value != null) ? ParseUtils.parseDouble(value) : null;
	}

	public static BigDecimal getDecimalEnvReq(String key) throws NumberFormatException {
		String value = getEnvReq(key);
		return new BigDecimal(value);
	}

	public static BigDecimal getDecimalEnv(String key) throws NumberFormatException {
		String value = getEnv(key);
		return (value != null) ? new BigDecimal(value) : null;
	}

	public static BigDecimal getDecimalEnv(String key, double defaultValIfInvalid) {
		String value = getEnv(key);
		return ParseUtils.parseDecimal(value, defaultValIfInvalid);
	}

	// resolveString

	public static String resolveString(String str) {
		if (str == null) {
			return null;
		}
		return STR.format(str, (p, expr) -> {
			// SYS
			String resolvedValue = resolve(expr);

			return (resolvedValue != null) ? resolvedValue : STR.MISSED_VALUE;
		});
	}

	public static String resolveString(String str, Map<String, Object> parameters) {
		if (str == null) {
			return null;
		}
		return STR.format(str, (pname, expr) -> {
			// Parameters
			Object resolvedValue = parameters.get(pname);

			// SYS
			if (resolvedValue == null) {
				resolvedValue = resolve(expr);
			}
			return (resolvedValue != null) ? resolvedValue : STR.MISSED_VALUE;
		});
	}

	public static String resolveString(String str, Object... parameters) {
		if (str == null) {
			return null;
		}
		return STR.format(str, (pname, expr) -> {

			Object resolvedValue = null;
			try {
				int index = Integer.parseInt(pname);

				// Parameters
				if ((0 <= index) && (index < parameters.length)) {
					resolvedValue = parameters[index];
				}
			} catch (NumberFormatException ex) {
			}

			// SYS
			if (resolvedValue == null) {
				resolvedValue = resolve(expr);
			}
			return (resolvedValue != null) ? resolvedValue : STR.MISSED_VALUE;
		});
	}

	// ${prop_name}
	// ${prop_name:defaultValue}
	// ${ENV.env_name}
	// ${ENV.env_name:defaultValue}
	// ${prop_name,ENV.env_name}
	// ${prop_name,ENV.env_name:defaultValue}

	private static final Pattern ENV_VAL_EXPR_PATTERN = Pattern.compile("[^\\s,:]+(\\s*,\\s*env.[^\\s,:]+\\s*)?(\\s*:\\s*[^\\s]+){0,1}",
			Pattern.CASE_INSENSITIVE);
	private static final Pattern ENV_VAL_HOLDER_PATTERN = Pattern.compile("\\$\\{[^}]*}");

	public static String resolve(String valueOrExpr) {
		Asserts.notNull(valueOrExpr);

		if (!ENV_VAL_HOLDER_PATTERN.matcher(valueOrExpr).matches()) {
			return valueOrExpr;
		}
		String expr = StringUtils.trimToNull(valueOrExpr.substring(2, valueOrExpr.length() - 1));

		Asserts.notNull(expr);
		Asserts.isTrue(ENV_VAL_EXPR_PATTERN.matcher(expr).matches(), () -> STR.fmt("Invalid expression '{}'.", expr));

		return doResolve(expr);
	}

	private static String doResolve(String expr) {
		int commaIdx = expr.indexOf(',');
		if (commaIdx < 0) {
			String defaultValue = null;

			int colonIdx = expr.indexOf(':');
			if (colonIdx >= 0) {
				defaultValue = StringUtils.trimToNull(expr.substring(colonIdx + 1));
			}
			if (StringUtils.startsWith(expr, "env.")) {
				// ENV
				return (colonIdx >= 0) ? getEnv(expr.substring(4, colonIdx).trim(), defaultValue) : getEnv(expr.substring(4), defaultValue);
			} else {
				// PROP
				return (colonIdx >= 0) ? getProp(expr.substring(0, colonIdx).trim(), defaultValue) : getProp(expr, defaultValue);
			}
		} else {
			// PROP
			String expr1 = expr.substring(0, commaIdx).trim();
			String resolvedValue = getProp(expr1);
			if (resolvedValue != null) {
				return resolvedValue;
			}

			// ENV
			String expr2 = expr.substring(commaIdx + 1).trim();
			String defaultValue = null;

			int colonIdx = expr2.indexOf(':');
			if (colonIdx >= 0) {
				defaultValue = StringUtils.trimToNull(expr2.substring(colonIdx + 1));
			}
			return (colonIdx >= 0) ? getEnv(expr2.substring(4, colonIdx).trim(), defaultValue) : getEnv(expr2.substring(4), defaultValue);
		}
	}
}
