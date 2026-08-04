// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.csv;

import com.appslandia.common.data.DataRecord;

/**
 *
 * @author Loc Ha
 *
 */
@FunctionalInterface
public interface CsvDebugger {

  void apply(int index, CsvRecord csvRecord, DataRecord dataRecord) throws Exception;
}
