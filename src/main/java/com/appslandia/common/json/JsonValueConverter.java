// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.json;

import java.util.Iterator;
import java.util.Map;

import com.appslandia.common.base.Out;

/**
 *
 * @author Loc Ha
 *
 */
public interface JsonValueConverter {

  boolean isJsonNull(Object element);

  String asNumber(Object element, Out<Boolean> asResult);

  String asString(Object element, Out<Boolean> asResult);

  boolean asBoolean(Object element, Out<Boolean> asResult);

  Iterator<Object> asJsonArray(Object element, Out<Boolean> asResult);

  Iterator<Map.Entry<String, Object>> asJsonObject(Object element, Out<Boolean> asResult);
}
