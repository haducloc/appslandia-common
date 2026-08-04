// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.json;

/**
 *
 * @author Loc Ha
 *
 */
public abstract class JsonTemporalSerializer {

  protected final String serializeIsoPattern;

  public JsonTemporalSerializer(String serializeIsoPattern) {
    this.serializeIsoPattern = serializeIsoPattern;
  }
}
