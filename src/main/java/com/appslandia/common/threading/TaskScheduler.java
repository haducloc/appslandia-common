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

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.appslandia.common.utils.STR;

public abstract class TaskScheduler {

  protected final Map<String, ScheduledTask> scheduledTasks = new ConcurrentHashMap<>();

  protected abstract ScheduledExecutorService getExecutor();

  public String scheduleAtFixedRate(TaskRunnable<?> task, long initialDelay, long period, TimeUnit unit) {
    ScheduledFuture<?> scheduledFuture = this.getExecutor().scheduleAtFixedRate(task, initialDelay, period, unit);

    this.scheduledTasks.put(task.attributes.getTaskId(), new ScheduledTask(scheduledFuture, task.attributes));
    return task.attributes.getTaskId();
  }

  public String scheduleWithFixedDelay(TaskRunnable<?> task, long initialDelay, long delay, TimeUnit unit) {
    ScheduledFuture<?> scheduledFuture = this.getExecutor().scheduleWithFixedDelay(task, initialDelay, delay, unit);

    this.scheduledTasks.put(task.attributes.getTaskId(), new ScheduledTask(scheduledFuture, task.attributes));
    return task.attributes.getTaskId();
  }

  public boolean cancel(String taskId) {
    var scheduledTask = this.scheduledTasks.remove(taskId);
    if (scheduledTask == null) {
      throw new IllegalArgumentException(STR.fmt("The given taskId {} is invalid.", taskId));
    }
    return scheduledTask.future.cancel(scheduledTask.attributes.mayInterruptIfRunningOnCancel());
  }

  public List<TaskAttributes> getScheduledTasks() {
    return this.scheduledTasks.values().stream().map(t -> t.attributes)
        .sorted(Comparator.comparing(TaskAttributes::getSubmittedTime).reversed()).toList();
  }

  public boolean isScheduled(String taskId) {
    return this.scheduledTasks.containsKey(taskId);
  }

  public void shutdown() {
    this.getExecutor().shutdown();
  }

  static class ScheduledTask {

    final ScheduledFuture<?> future;
    final TaskAttributes attributes;

    public ScheduledTask(ScheduledFuture<?> future, TaskAttributes attributes) {

      this.future = future;
      this.attributes = attributes;
    }
  }
}
