// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
    ScheduledFuture<?> scheduledFuture = getExecutor().scheduleAtFixedRate(task, initialDelay, period, unit);

    scheduledTasks.put(task.attributes.getTaskId(), new ScheduledTask(scheduledFuture, task.attributes));
    return task.attributes.getTaskId();
  }

  public String scheduleWithFixedDelay(TaskRunnable<?> task, long initialDelay, long delay, TimeUnit unit) {
    ScheduledFuture<?> scheduledFuture = getExecutor().scheduleWithFixedDelay(task, initialDelay, delay, unit);

    scheduledTasks.put(task.attributes.getTaskId(), new ScheduledTask(scheduledFuture, task.attributes));
    return task.attributes.getTaskId();
  }

  public boolean cancel(String taskId) {
    var scheduledTask = scheduledTasks.remove(taskId);
    if (scheduledTask == null) {
      throw new IllegalArgumentException(STR.fmt("The given taskId {} is invalid.", taskId));
    }
    return scheduledTask.future.cancel(scheduledTask.attributes.mayInterruptIfRunningOnCancel());
  }

  public List<TaskAttributes> getScheduledTasks() {
    return scheduledTasks.values().stream().map(t -> t.attributes)
        .sorted(Comparator.comparing(TaskAttributes::getSubmittedTime).reversed()).toList();
  }

  public boolean isScheduled(String taskId) {
    return scheduledTasks.containsKey(taskId);
  }

  public void shutdown() {
    getExecutor().shutdown();
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
