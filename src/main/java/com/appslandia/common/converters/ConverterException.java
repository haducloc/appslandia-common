// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.converters;

/**
 *
 * @author Loc Ha
 *
 */
public class ConverterException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  private String msgKey;

  public ConverterException(String message, String msgKey) {
    super(message);
    this.msgKey = msgKey;
  }

  public String getMsgKey() {
    return msgKey;
  }
}
