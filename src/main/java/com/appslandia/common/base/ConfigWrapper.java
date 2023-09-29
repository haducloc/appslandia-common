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
    public Iterator<String> getKeyIterator() {
	return this.cfg.getKeyIterator();
    }

    @Override
    public String getString(String key) {
	return this.cfg.getString(key);
    }

    @Override
    public String getString(String key, String defaultValue) {
	return this.cfg.getString(key, defaultValue);
    }

    @Override
    public String getStringReq(String key) {
	return this.cfg.getStringReq(key);
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
    public boolean getBool(String key, boolean defaultValIfInvalid) {
	return this.cfg.getBool(key, defaultValIfInvalid);
    }

    @Override
    public boolean getBool(String key) throws BoolFormatException {
	return this.cfg.getBool(key);
    }

    @Override
    public Boolean getBoolOpt(String key) throws BoolFormatException {
	return this.cfg.getBoolOpt(key);
    }

    @Override
    public int getInt(String key, int defaultValIfInvalid) {
	return this.cfg.getInt(key, defaultValIfInvalid);
    }

    @Override
    public int getInt(String key) throws NumberFormatException {
	return this.cfg.getInt(key);
    }

    @Override
    public Integer getIntOpt(String key) throws NumberFormatException {
	return this.cfg.getIntOpt(key);
    }

    @Override
    public long getLong(String key, long defaultValIfInvalid) {
	return this.cfg.getLong(key, defaultValIfInvalid);
    }

    @Override
    public long getLong(String key) throws NumberFormatException {
	return this.cfg.getLong(key);
    }

    @Override
    public Long getLongOpt(String key) throws NumberFormatException {
	return this.cfg.getLongOpt(key);
    }

    @Override
    public double getDouble(String key, double defaultValIfInvalid) {
	return this.cfg.getDouble(key, defaultValIfInvalid);
    }

    @Override
    public double getDouble(String key) throws NumberFormatException {
	return this.cfg.getDouble(key);
    }

    @Override
    public Double getDoubleOpt(String key) throws NumberFormatException {
	return this.cfg.getDoubleOpt(key);
    }

    @Override
    public BigDecimal getDecimal(String key, double defaultValIfInvalid) {
	return this.cfg.getDecimal(key, defaultValIfInvalid);
    }

    @Override
    public BigDecimal getDecimalReq(String key) throws NumberFormatException {
	return this.cfg.getDecimalReq(key);
    }

    @Override
    public BigDecimal getDecimal(String key) throws NumberFormatException {
	return this.cfg.getDecimal(key);
    }
}
