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

package com.appslandia.common.crypto;

import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.spec.AlgorithmParameterSpec;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.Asserts;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class AlgorithmParametersUtil<T extends AlgorithmParameterSpec> extends InitializeObject {

    private String algorithm, provider;
    private AlgorithmParameters algorithmParameters;
    private Class<T> paramSpecClass;
    private AlgorithmParameterSpec algParamSpec;

    final Object mutex = new Object();

    public AlgorithmParametersUtil() {
    }

    public AlgorithmParametersUtil(String algorithm) {
	this.algorithm = algorithm;
    }

    public AlgorithmParametersUtil(String algorithm, String provider) {
	this.algorithm = algorithm;
	this.provider = provider;
    }

    @Override
    protected void init() throws Exception {
	Asserts.notNull(this.algorithm, "algorithm is required.");
	Asserts.notNull(this.paramSpecClass, "paramSpecClass is required.");
	Asserts.notNull(this.algParamSpec, "algParamSpec is required.");

	// AlgorithmParameters
	if (this.provider == null) {
	    this.algorithmParameters = AlgorithmParameters.getInstance(this.algorithm);
	} else {
	    this.algorithmParameters = AlgorithmParameters.getInstance(this.algorithm, this.provider);
	}

	this.algorithmParameters.init(this.algParamSpec);
    }

    public T getParameterSpec() throws CryptoException {
	this.initialize();
	try {
	    synchronized (this.mutex) {
		return this.algorithmParameters.getParameterSpec(this.paramSpecClass);
	    }
	} catch (GeneralSecurityException ex) {
	    throw new CryptoException(ex);
	}
    }

    public String getAlgorithm() {
	initialize();
	return this.algorithm;
    }

    public AlgorithmParametersUtil<T> setAlgorithm(String algorithm) {
	assertNotInitialized();
	this.algorithm = algorithm;
	return this;
    }

    public String getProvider() {
	initialize();
	return this.provider;
    }

    public AlgorithmParametersUtil<T> setProvider(String provider) {
	assertNotInitialized();
	this.provider = provider;
	return this;
    }

    public AlgorithmParametersUtil<T> setParamSpecClass(Class<T> paramSpecClass) {
	assertNotInitialized();
	this.paramSpecClass = paramSpecClass;
	return this;
    }

    public AlgorithmParametersUtil<T> setAlgParamSpec(AlgorithmParameterSpec algParamSpec) {
	assertNotInitialized();
	this.algParamSpec = algParamSpec;
	return this;
    }
}
