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

import com.appslandia.common.json.JsonbMapAdapter;
import com.appslandia.common.json.JsonbProcessor;
import com.appslandia.common.utils.ObjectUtils;

import jakarta.json.bind.JsonbConfig;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JoseJsonb {

  public static JsonbConfig newJsonbConfig(boolean serializeNulls, boolean prettyPrinting) {
    // @formatter:off
		return JsonbProcessor.newConfig(serializeNulls, prettyPrinting)
				.withAdapters(

            // JsonMapObject
            JsonbProcessor.newJsonMapObjectAdapter(),
                
            // JoseMapObject
            new JsonbMapAdapter<>(m -> new JoseMapObject(m)) {}
                .setMapConverter(m -> new JoseMapObject(m)),                
                
						// JsonWebKey
						new JsonbMapAdapter<>(m -> new JsonWebKey(m)) {}
						    .setMapConverter(m -> new JoseMapObject(m)),

						// JoseHeader
						new JsonbMapAdapter<JoseHeader>(m -> new JoseHeader(m)) {}
						      .setValueConverter(new String[]{"jwk"}, m -> new JsonWebKey(ObjectUtils.cast(m)))
						      .setMapConverter(m -> new JoseMapObject(m)),

						// JwtPayload
						new JsonbMapAdapter<JwtPayload>(m -> new JwtPayload(m)) {}
						      .setValueConverter(new String[]{"jwks\\[\\d+]"}, m -> new JsonWebKey(ObjectUtils.cast(m)))
						      .setMapConverter(m -> new JoseMapObject(m))  
				    );
		// @formatter:on
  }

  public static JsonbProcessor newJsonProcessor() {
    return new JsonbProcessor().setConfig(newJsonbConfig(true, false));
  }
}
