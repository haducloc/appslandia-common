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

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ConfigWrapper implements Config {

    final Config cfg;

    public ConfigWrapper(Config cfg) {
	this.cfg = cfg;
    }

    // com.appslandia.common.base.Config

    @Override
    public String getString(String key) {
	return this.cfg.getString(key);
    }

    @Override
    public Iterator<String> getKeyIterator() {
	return this.cfg.getKeyIterator();
    }

    @Override
    public String getString(String key, String defaultValue) {
	return this.cfg.getString(key, defaultValue);
    }

    @Override
    public String getRequiredString(String key) {
	return this.cfg.getRequiredString(key);
    }

    @Override
    public String[] getStringArray(String key) {
	return this.cfg.getStringArray(key);
    }

    @Override
    public String resolve(String key) {
	return this.cfg.resolve(key);
    }

    @Override
    public String resolve(String key, Map<String, Object> parameters) {
	return this.cfg.resolve(key, parameters);
    }

    @Override
    public String resolve(String key, Object... parameters) {
	return this.cfg.resolve(key, parameters);
    }

    @Override
    public boolean getBool(String key, boolean defaultValue) {
	return this.cfg.getBool(key, defaultValue);
    }

    @Override
    public boolean getBool(String key) {
	return this.cfg.getBool(key);
    }

    @Override
    public int getInt(String key, int defaultValue) {
	return this.cfg.getInt(key, defaultValue);
    }

    @Override
    public int getInt(String key) {
	return this.cfg.getInt(key);
    }

    @Override
    public long getLong(String key, long defaultValue) {
	return this.cfg.getLong(key, defaultValue);
    }

    @Override
    public long getLong(String key) {
	return this.cfg.getLong(key);
    }

    @Override
    public double getDouble(String key, double defaultValue) {
	return this.cfg.getDouble(key, defaultValue);
    }

    @Override
    public double getDouble(String key) {
	return this.cfg.getDouble(key);
    }

    @Override
    public BigDecimal getDecimal(String key) {
	return this.cfg.getDecimal(key);
    }

    @Override
    public BigDecimal getDecimal(String key, BigDecimal defaultValue) {
	return this.cfg.getDecimal(key, defaultValue);
    }

    @Override
    public <T> T getValue(String key, Function<String, T> converter) {
	return this.cfg.getValue(key, converter);
    }
}
