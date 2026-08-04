// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
