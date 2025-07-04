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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.appslandia.common.utils.ValueUtils;

/**
 *
 * @author Loc Ha
 *
 */
public abstract class ThreadSafeTester extends InitializeObject {

  private int tasks;
  private int threads;

  private CountDownLatch taskMonitor;
  private ExecutorService executorService;

  @Override
  protected void init() throws Exception {
    this.tasks = ValueUtils.valueOrMin(this.tasks, 100);

    var minPoolSize = ValueUtils.valueOrMin(Runtime.getRuntime().availableProcessors() / 4, 1);
    this.threads = ValueUtils.valueOrMin(this.threads, minPoolSize);

    this.executorService = Executors.newFixedThreadPool(this.threads);
    this.taskMonitor = new CountDownLatch(this.tasks);
  }

  public ThreadSafeTester setTasks(int tasks) {
    assertNotInitialized();
    this.tasks = tasks;
    return this;
  }

  public ThreadSafeTester setThreads(int threads) {
    assertNotInitialized();
    this.threads = threads;
    return this;
  }

  protected abstract Runnable newTask();

  public ThreadSafeTester execute() {
    return execute(0, TimeUnit.MILLISECONDS);
  }

  public ThreadSafeTester execute(long timeout, TimeUnit unit) {
    initialize();
    for (var i = 0; i < this.tasks; i++) {
      this.executorService.execute(newTask());
    }
    try {
      if (timeout == 0) {
        this.taskMonitor.await();
      } else {
        this.taskMonitor.await(timeout, unit);
      }

    } catch (InterruptedException ex) {
      throw new UncheckedException(ex);
    }
    this.executorService.shutdown();
    return this;
  }

  protected void doneTask() {
    this.taskMonitor.countDown();
  }
}
