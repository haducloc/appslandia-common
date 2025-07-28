// The MIT License (MIT)
// Copyright © 2015 Loc Ha

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

package com.appslandia.common.base;

import java.lang.System.Logger.Level;
import java.util.function.Supplier;

import com.appslandia.common.utils.ExceptionUtils;
import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author Loc Ha
 *
 */
public class AppLogger {

  final System.Logger logger;

  public AppLogger(System.Logger logger) {
    this.logger = logger;
  }

  public AppLogger(String loggerName) {
    this.logger = System.getLogger(loggerName);
  }

  public void error(Throwable thrown) {
    this.logger.log(Level.ERROR, ExceptionUtils.buildMessage(thrown), thrown);
  }

  public void error(TaskBlock taskBlock) {
    try {
      taskBlock.run();

    } catch (Exception thrown) {
      this.logger.log(Level.ERROR, ExceptionUtils.buildMessage(thrown), thrown);
    }
  }

  public void trace(String format, Object... params) {
    this.logger.log(Level.TRACE, format, params);
  }

  public void debug(String format, Object... params) {
    this.logger.log(Level.DEBUG, format, params);
  }

  public void info(String format, Object... params) {
    this.logger.log(Level.INFO, format, params);
  }

  public void warn(String format, Object... params) {
    this.logger.log(Level.WARNING, format, params);
  }

  public void error(String format, Object... params) {
    this.logger.log(Level.ERROR, format, params);
  }

  public void trace(String message) {
    this.logger.log(Level.TRACE, message);
  }

  public void debug(String message) {
    this.logger.log(Level.DEBUG, message);
  }

  public void info(String message) {
    this.logger.log(Level.INFO, message);
  }

  public void warn(String message) {
    this.logger.log(Level.WARNING, message);
  }

  public void error(String message) {
    this.logger.log(Level.ERROR, message);
  }

  public void trace(Supplier<String> message) {
    this.logger.log(Level.TRACE, message);
  }

  public void debug(Supplier<String> message) {
    this.logger.log(Level.DEBUG, message);
  }

  public void info(Supplier<String> message) {
    this.logger.log(Level.INFO, message);
  }

  public void warn(Supplier<String> message) {
    this.logger.log(Level.WARNING, message);
  }

  public void error(Supplier<String> message) {
    this.logger.log(Level.ERROR, message);
  }

  public void trace(String message, Throwable thrown) {
    this.logger.log(Level.TRACE, message, thrown);
  }

  public void debug(String message, Throwable thrown) {
    this.logger.log(Level.DEBUG, message, thrown);
  }

  public void info(String message, Throwable thrown) {
    this.logger.log(Level.INFO, message, thrown);
  }

  public void warn(String message, Throwable thrown) {
    this.logger.log(Level.WARNING, message, thrown);
  }

  public void error(String message, Throwable thrown) {
    this.logger.log(Level.ERROR, message, thrown);
  }

  public void trace(Supplier<String> message, Throwable thrown) {
    this.logger.log(Level.TRACE, message, thrown);
  }

  public void debug(Supplier<String> message, Throwable thrown) {
    this.logger.log(Level.DEBUG, message, thrown);
  }

  public void info(Supplier<String> message, Throwable thrown) {
    this.logger.log(Level.INFO, message, thrown);
  }

  public void warn(Supplier<String> message, Throwable thrown) {
    this.logger.log(Level.WARNING, message, thrown);
  }

  public void error(Supplier<String> message, Throwable thrown) {
    this.logger.log(Level.ERROR, message, thrown);
  }

  @Override
  public String toString() {
    return ObjectUtils.toStringWrapper(this, this.logger);
  }
}
