// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.csv;

import com.appslandia.common.jdbc.ConnectionImpl;

/**
 *
 * @author Loc Ha
 *
 */
@FunctionalInterface
public interface CsvToDbConverter {

  Object apply(String value, ConnectionImpl conn) throws Exception;
}
