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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class CharArrayWriterTest {

	@Test
	public void test_append() {
		try (CharArrayWriter caw = new CharArrayWriter()) {
			caw.append('a');
			caw.append("bcd");
			caw.append("e_fgh", 1, 4);

			caw.flush();

			Assertions.assertEquals("abcd_fg", caw.toString());
		} catch (Exception ex) {
			Assertions.fail(ex.getMessage());
		}
	}

	@Test
	public void test_write() {
		try (CharArrayWriter caw = new CharArrayWriter()) {
			caw.write('a');
			caw.write("bcd");
			caw.write("ef".toCharArray());
			caw.write("g_hk", 1, 3);
			caw.write("x_yzt".toCharArray(), 1, 3);

			caw.flush();

			Assertions.assertEquals("abcdef_hk_yz", caw.toString());
		} catch (Exception ex) {
			Assertions.fail(ex.getMessage());
		}
	}

	@Test
	public void test_writeNull() {
		try (CharArrayWriter caw = new CharArrayWriter()) {
			caw.append("a");
			caw.append("_");
			caw.append(null);

			caw.flush();

			Assertions.assertEquals("a_null", caw.toString());
		} catch (Exception ex) {
			Assertions.fail(ex.getMessage());
		}
	}
}
