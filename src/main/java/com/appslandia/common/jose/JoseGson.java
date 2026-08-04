// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jose;

import com.appslandia.common.json.GsonMapAdapter;
import com.appslandia.common.json.GsonProcessor;
import com.appslandia.common.utils.ObjectUtils;
import com.google.gson.GsonBuilder;

/**
 *
 * @author Loc Ha
 *
 */
public class JoseGson {

  public static GsonBuilder newGsonBuilder(boolean serializeNulls, boolean prettyPrinting) {
    // @formatter:off
		return GsonProcessor.newBuilder(serializeNulls, prettyPrinting)

		    // JoseMap
        .registerTypeAdapter(JoseMap.class,
            new GsonMapAdapter<>(m -> new JoseMap(m))
              .setMapConverter(m -> new JoseMap(m))
            )

				// JsonWebKey
				.registerTypeAdapter(JsonWebKey.class,
						new GsonMapAdapter<>(m -> new JsonWebKey(m))
						  .setMapConverter(m -> new JoseMap(m))
						)

				// JoseHeader
				.registerTypeAdapter(JoseHeader.class,
						new GsonMapAdapter<>(m -> new JoseHeader(m))
								.setValueConverter(new String[]{"jwk"}, m -> new JsonWebKey(ObjectUtils.cast(m)))
								.setMapConverter(m -> new JoseMap(m)))

				// JwtPayload
				.registerTypeAdapter(JwtPayload.class,
				    new GsonMapAdapter<>(m -> new JwtPayload(m))
						    .setValueConverter(new String[]{"jwks\\[\\d+]"}, m -> new JsonWebKey(ObjectUtils.cast(m)))
						    .setMapConverter(m -> new JoseMap(m)));
		// @formatter:on
  }

  public static GsonProcessor newJsonProcessor() {
    return new GsonProcessor().setBuilder(newGsonBuilder(true, false));
  }
}
