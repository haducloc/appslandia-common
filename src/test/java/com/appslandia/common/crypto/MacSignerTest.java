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

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.appslandia.common.base.ThreadSafeTester;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MacSignerTest {

    @Test
    public void test() {
	MacSigner impl = new MacSigner().setAlgorithm("HmacMD5");
	impl.setSecret("secret".getBytes(StandardCharsets.UTF_8));

	try {
	    byte[] data = "data".getBytes(StandardCharsets.UTF_8);
	    byte[] digest = impl.digest(data);

	    Assertions.assertTrue(impl.verify(data, digest));

	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
    }

    @Test
    public void test_clone() {
	MacSigner impl = new MacSigner().setAlgorithm("HmacMD5");
	impl.setSecret("secret".getBytes(StandardCharsets.UTF_8));
	impl = impl.clone();

	try {
	    byte[] data = "data".getBytes(StandardCharsets.UTF_8);
	    byte[] digest = impl.digest(data);

	    Assertions.assertTrue(impl.verify(data, digest));

	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
    }

    @Test
    public void test_invalid() {
	MacSigner impl = new MacSigner().setAlgorithm("HmacMD5");
	impl.setSecret("secret".getBytes(StandardCharsets.UTF_8));

	try {
	    byte[] data = "data".getBytes(StandardCharsets.UTF_8);
	    byte[] digest = impl.digest(data);

	    Assertions.assertFalse(impl.verify("invalid".getBytes(StandardCharsets.UTF_8), digest));

	} catch (Exception ex) {
	    Assertions.fail(ex.getMessage());
	}
    }

    @Test
    public void test_threadSafe() {
	final MacSigner impl = new MacSigner().setAlgorithm("HmacMD5");
	impl.setSecret("secret".getBytes(StandardCharsets.UTF_8));

	new ThreadSafeTester() {

	    @Override
	    protected Runnable newTask() {
		return new Runnable() {

		    @Override
		    public void run() {
			try {
			    byte[] data = "data".getBytes(StandardCharsets.UTF_8);
			    byte[] digest = impl.digest(data);

			    Assertions.assertTrue(impl.verify(data, digest));

			} catch (Exception ex) {
			    Assertions.fail(ex.getMessage());
			} finally {
			    doneTask();
			}
		    }
		};
	    }
	}.execute();
    }
}
