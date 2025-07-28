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

package com.appslandia.common.threading;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author Loc Ha
 *
 */
public class ThreadFactoryBuilder {

  private String nameFormat;
  private Boolean daemon;
  private Integer priority;
  private UncaughtExceptionHandler uncaughtExceptionHandler;
  private ThreadFactory backingThreadFactory;

  public ThreadFactoryBuilder setNameFormat(String nameFormat) {
    this.nameFormat = nameFormat;
    return this;
  }

  public ThreadFactoryBuilder setDaemon(boolean daemon) {
    this.daemon = daemon;
    return this;
  }

  public ThreadFactoryBuilder setPriority(int priority) {
    this.priority = priority;
    return this;
  }

  public ThreadFactoryBuilder setUncaughtExceptionHandler(UncaughtExceptionHandler uncaughtExceptionHandler) {
    this.uncaughtExceptionHandler = uncaughtExceptionHandler;
    return this;
  }

  public ThreadFactoryBuilder setBackingThreadFactory(ThreadFactory backingThreadFactory) {
    this.backingThreadFactory = backingThreadFactory;
    return this;
  }

  public ThreadFactory build() {
    return doBuild(this);
  }

  private static ThreadFactory doBuild(ThreadFactoryBuilder builder) {

    var nameFormat = builder.nameFormat;
    var daemon = builder.daemon;
    var priority = builder.priority;
    var uncaughtExceptionHandler = builder.uncaughtExceptionHandler;
    var backingThreadFactory = (builder.backingThreadFactory != null) ? builder.backingThreadFactory
        : Executors.defaultThreadFactory();

    var count = (nameFormat != null) ? new AtomicLong(0) : null;

    return new ThreadFactory() {

      @Override
      public Thread newThread(Runnable runnable) {
        var thread = backingThreadFactory.newThread(runnable);
        if (nameFormat != null) {
          thread.setName(format(nameFormat, count.getAndIncrement()));
        }
        if (daemon != null) {
          thread.setDaemon(daemon);
        }
        if (priority != null) {
          thread.setPriority(priority);
        }
        if (uncaughtExceptionHandler != null) {
          thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        }
        return thread;
      }
    };
  }

  private static String format(String format, Object... args) {
    return String.format(Locale.ROOT, format, args);
  }
}
