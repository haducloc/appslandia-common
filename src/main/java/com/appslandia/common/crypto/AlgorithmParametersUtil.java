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
import java.util.function.Consumer;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.Asserts;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class AlgorithmParametersUtil extends InitializeObject implements Cloneable {

    private String algorithm, provider;
    private AlgorithmParameters algorithmParameters;
    private Consumer<AlgorithmParameters> algParametersInit;

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
	Asserts.notNull(this.algParametersInit, "algParametersInit is required.");

	// AlgorithmParameters
	if (this.provider == null) {
	    this.algorithmParameters = AlgorithmParameters.getInstance(this.algorithm);
	} else {
	    this.algorithmParameters = AlgorithmParameters.getInstance(this.algorithm, this.provider);
	}

	// init();
	this.algParametersInit.accept(this.algorithmParameters);
    }

    public <T extends AlgorithmParameterSpec> T getParameterSpec(Class<T> paramSpec) throws CryptoException {
	this.initialize();
	try {
	    synchronized (this.mutex) {
		return this.algorithmParameters.getParameterSpec(paramSpec);
	    }
	} catch (GeneralSecurityException ex) {
	    throw new CryptoException(ex);
	}
    }

    public String getAlgorithm() {
	initialize();
	return this.algorithm;
    }

    public AlgorithmParametersUtil setAlgorithm(String algorithm) {
	assertNotInitialized();
	this.algorithm = algorithm;
	return this;
    }

    public String getProvider() {
	initialize();
	return this.provider;
    }

    public AlgorithmParametersUtil setProvider(String provider) {
	assertNotInitialized();
	this.provider = provider;
	return this;
    }

    public AlgorithmParametersUtil setAlgParametersInit(Consumer<AlgorithmParameters> algParametersInit) {
	assertNotInitialized();
	this.algParametersInit = algParametersInit;
	return this;
    }

    @Override
    public AlgorithmParametersUtil clone() {
	return new AlgorithmParametersUtil().setAlgorithm(this.algorithm).setProvider(this.provider).setAlgParametersInit(this.algParametersInit);
    }
}
