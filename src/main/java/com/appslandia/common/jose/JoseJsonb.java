// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jose;

import com.appslandia.common.json.JsonbMapAdapter;
import com.appslandia.common.json.JsonbProcessor;
import com.appslandia.common.utils.ObjectUtils;

import jakarta.json.bind.JsonbConfig;

/**
 *
 * @author Loc Ha
 *
 */
public class JoseJsonb {

  public static JsonbConfig newJsonbConfig(boolean serializeNulls, boolean prettyPrinting) {
    // @formatter:off
		return JsonbProcessor.newConfig(serializeNulls, prettyPrinting)
				.withAdapters(

            // JoseMap
            new JsonbMapAdapter<>(m -> new JoseMap(m)) {}
                .setMapConverter(m -> new JoseMap(m)),

						// JsonWebKey
						new JsonbMapAdapter<>(m -> new JsonWebKey(m)) {}
						    .setMapConverter(m -> new JoseMap(m)),

						// JoseHeader
						new JsonbMapAdapter<>(m -> new JoseHeader(m)) {}
						      .setValueConverter(new String[]{"jwk"}, m -> new JsonWebKey(ObjectUtils.cast(m)))
						      .setMapConverter(m -> new JoseMap(m)),

						// JwtPayload
						new JsonbMapAdapter<>(m -> new JwtPayload(m)) {}
						      .setValueConverter(new String[]{"jwks\\[\\d+]"}, m -> new JsonWebKey(ObjectUtils.cast(m)))
						      .setMapConverter(m -> new JoseMap(m))
				    );
		// @formatter:on
  }

  public static JsonbProcessor newJsonProcessor() {
    return new JsonbProcessor().setConfig(newJsonbConfig(true, false));
  }
}
