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

package com.appslandia.common.jose;

import com.appslandia.common.json.GsonMapAdapter;
import com.appslandia.common.json.GsonProcessor;
import com.appslandia.common.utils.ObjectUtils;
import com.google.gson.GsonBuilder;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JoseGson {

  public static GsonBuilder newGsonBuilder(boolean serializeNulls, boolean formatting) {
    // @formatter:off
		return GsonProcessor.newBuilder(serializeNulls, formatting)

				// JsonWebKey
				.registerTypeAdapter(JsonWebKey.class,
						new GsonMapAdapter<>(true, m -> new JsonWebKey(m)))

				// JoseHeader
				.registerTypeAdapter(JoseHeader.class,
						new GsonMapAdapter<>(true, m -> new JoseHeader(m))
								.setValueConverter(new String[]{"jwk"},
										m -> new JsonWebKey(
												ObjectUtils.cast(m))))

				// JwtPayload
				.registerTypeAdapter(JwtPayload.class, new GsonMapAdapter<>(
						true, (m) -> new JwtPayload(m))
						.setValueConverter(new String[]{"jwks\\[\\d+]"},
								m -> new JsonWebKey(ObjectUtils.cast(m))));
		// @formatter:on
  }

  public static GsonProcessor newJsonProcessor() {
    return new GsonProcessor().setBuilder(newGsonBuilder(true, false));
  }
}
