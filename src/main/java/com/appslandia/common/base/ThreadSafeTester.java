// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
public abstract class ThreadSafeTester extends InitializingObject {

  private int tasks;
  private int threads;

  private CountDownLatch taskMonitor;
  private ExecutorService executorService;

  @Override
  protected void init() throws Exception {
    tasks = ValueUtils.valueOrMin(tasks, 100);

    var minPoolSize = ValueUtils.valueOrMin(Runtime.getRuntime().availableProcessors() / 4, 1);
    threads = ValueUtils.valueOrMin(threads, minPoolSize);

    executorService = Executors.newFixedThreadPool(threads);
    taskMonitor = new CountDownLatch(tasks);
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
    for (var i = 0; i < tasks; i++) {
      executorService.execute(newTask());
    }
    try {
      if (timeout == 0) {
        taskMonitor.await();
      } else {
        taskMonitor.await(timeout, unit);
      }

    } catch (InterruptedException ex) {
      throw new UncheckedException(ex);
    }
    executorService.shutdown();
    return this;
  }

  protected void doneTask() {
    taskMonitor.countDown();
  }
}
