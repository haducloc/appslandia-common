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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class FileNameUtilsTest {

	@Test
	public void test_toFileName() {
		String fn = FileNameUtils.toFileName("doc.pdf", null);
		Assertions.assertEquals("doc.pdf", fn);

		fn = FileNameUtils.toFileName("doc .pdf", null);
		Assertions.assertEquals("doc.pdf", fn);

		fn = FileNameUtils.toFileName(" doc.pdf", null);
		Assertions.assertEquals("doc.pdf", fn);

		fn = FileNameUtils.toFileName("d oc.pdf", null);
		Assertions.assertEquals("d-oc.pdf", fn);

		// extra
		fn = FileNameUtils.toFileName("doc.pdf", "1");
		Assertions.assertEquals("doc-1.pdf", fn);

		fn = FileNameUtils.toFileName("doc .pdf", "1");
		Assertions.assertEquals("doc-1.pdf", fn);

		fn = FileNameUtils.toFileName(" doc.pdf", "1");
		Assertions.assertEquals("doc-1.pdf", fn);

		fn = FileNameUtils.toFileName("d oc.pdf", "1");
		Assertions.assertEquals("d-oc-1.pdf", fn);
	}

	@Test
	public void test_toFileName_Null() {
		String fn = FileNameUtils.toFileName("$.$", null);
		Assertions.assertNull(fn);

		// extra
		fn = FileNameUtils.toFileName("$.$", "1");
		Assertions.assertNull(fn);
	}

	@Test
	public void test_toFileName_NoExt() {
		String fn = FileNameUtils.toFileName("doc", null);
		Assertions.assertEquals("doc", fn);

		fn = FileNameUtils.toFileName("doc ", null);
		Assertions.assertEquals("doc", fn);

		fn = FileNameUtils.toFileName(" doc", null);
		Assertions.assertEquals("doc", fn);

		fn = FileNameUtils.toFileName("d oc", null);
		Assertions.assertEquals("d-oc", fn);

		fn = FileNameUtils.toFileName("doc.$", null);
		Assertions.assertEquals("doc", fn);

		// extra
		fn = FileNameUtils.toFileName("doc", "1");
		Assertions.assertEquals("doc-1", fn);

		fn = FileNameUtils.toFileName("doc ", "1");
		Assertions.assertEquals("doc-1", fn);

		fn = FileNameUtils.toFileName(" doc", "1");
		Assertions.assertEquals("doc-1", fn);

		fn = FileNameUtils.toFileName("d oc", "1");
		Assertions.assertEquals("d-oc-1", fn);

		fn = FileNameUtils.toFileName("doc.$", "1");
		Assertions.assertEquals("doc-1", fn);
	}

	@Test
	public void test_toFileName_NoName() {
		String fn = FileNameUtils.toFileName(".pdf", null);
		Assertions.assertEquals(".pdf", fn);

		fn = FileNameUtils.toFileName("$.pdf", null);
		Assertions.assertEquals(".pdf", fn);

		// extra
		fn = FileNameUtils.toFileName(".pdf", "1");
		Assertions.assertEquals("1.pdf", fn);

		fn = FileNameUtils.toFileName("$.pdf", "1");
		Assertions.assertEquals("1.pdf", fn);
	}

	@Test
	public void test_toFileName_PunctName() {
		String fn = FileNameUtils.toFileName("doc$.pdf", null);
		Assertions.assertEquals("doc.pdf", fn);

		fn = FileNameUtils.toFileName("$doc.pdf", null);
		Assertions.assertEquals("doc.pdf", fn);

		fn = FileNameUtils.toFileName("d$oc.pdf", null);
		Assertions.assertEquals("d-oc.pdf", fn);

		// extra
		fn = FileNameUtils.toFileName("doc$.pdf", "1");
		Assertions.assertEquals("doc-1.pdf", fn);

		fn = FileNameUtils.toFileName("$doc.pdf", "1");
		Assertions.assertEquals("doc-1.pdf", fn);

		fn = FileNameUtils.toFileName("d$oc.pdf", "1");
		Assertions.assertEquals("d-oc-1.pdf", fn);
	}

	@Test
	public void test_toFileName_PunctExt() {
		String fn = FileNameUtils.toFileName("doc.pdf$", null);
		Assertions.assertEquals("doc.pdf", fn);

		fn = FileNameUtils.toFileName("doc.$pdf", null);
		Assertions.assertEquals("doc.pdf", fn);

		fn = FileNameUtils.toFileName("doc.p$df", null);
		Assertions.assertEquals("doc.p-df", fn);

		// extra
		fn = FileNameUtils.toFileName("doc.pdf$", "1");
		Assertions.assertEquals("doc-1.pdf", fn);

		fn = FileNameUtils.toFileName("doc.$pdf", "1");
		Assertions.assertEquals("doc-1.pdf", fn);

		fn = FileNameUtils.toFileName("doc.p$df", "1");
		Assertions.assertEquals("doc-1.p-df", fn);
	}
}
