// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.csv;

/**
 *
 * @author Loc Ha
 *
 */
@FunctionalInterface
public interface CsvConsumer {

  void apply(int index, CsvRecord csvRecord) throws Exception;
}
