// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

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

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class TaskScheduler<T> {

    protected final ScheduledExecutorService executor;
    protected final AtomicLong taskIdGenerator = new AtomicLong(0);
    protected final Map<Long, WeakTask> taskMap = new ConcurrentHashMap<>();

    public TaskScheduler() {
	this(Executors.newSingleThreadScheduledExecutor());
    }

    public TaskScheduler(ScheduledExecutorService executor) {
	this.executor = executor;
    }

    protected long nextTaskId() {
	return this.taskIdGenerator.incrementAndGet();
    }

    public long schedule(Task<T> task, long delay, TimeUnit unit) {
	long taskId = nextTaskId();
	ScheduledFuture<?> scheduledFuture = this.executor.schedule(task, delay, unit);

	this.taskMap.put(taskId, new WeakTask(scheduledFuture, task.cancelMayInterruptIfRunning));
	return taskId;
    }

    public long scheduleAtFixedRate(Task<T> task, long initialDelay, long period, TimeUnit unit) {
	long taskId = nextTaskId();
	ScheduledFuture<?> scheduledFuture = this.executor.scheduleAtFixedRate(task, initialDelay, period, unit);

	this.taskMap.put(taskId, new WeakTask(scheduledFuture, task.cancelMayInterruptIfRunning));
	return taskId;
    }

    public long scheduleWithFixedDelay(Task<T> task, long initialDelay, long delay, TimeUnit unit) {
	long taskId = nextTaskId();
	ScheduledFuture<?> scheduledFuture = this.executor.scheduleWithFixedDelay(task, initialDelay, delay, unit);

	this.taskMap.put(taskId, new WeakTask(scheduledFuture, task.cancelMayInterruptIfRunning));
	return taskId;
    }

    public void cancel(long taskId) {
	WeakTask weakTask = this.taskMap.remove(taskId);
	if (weakTask != null) {
	    ScheduledFuture<?> t = weakTask.get();
	    if (t != null) {
		t.cancel(weakTask.cancelMayInterruptIfRunning);
	    }
	}
    }

    public Set<Long> getTaskIds() {
	return Collections.unmodifiableSet(this.taskMap.keySet());
    }

    public void shutdown() {
	this.executor.shutdown();
    }

    public List<Task<?>> shutdownNow() {
	List<Runnable> unstartedTasks = this.executor.shutdownNow();
	return unstartedTasks.stream().map(t -> (Task<?>) t).collect(Collectors.toList());
    }

    public static abstract class Task<T> implements Runnable {

	protected final boolean cancelMayInterruptIfRunning;
	protected final T data;

	public Task(T data) {
	    this(data, true);
	}

	public Task(T data, boolean cancelMayInterruptIfRunning) {
	    this.data = data;
	    this.cancelMayInterruptIfRunning = cancelMayInterruptIfRunning;
	}
    }

    private static class WeakTask extends WeakReference<ScheduledFuture<?>> {

	protected final boolean cancelMayInterruptIfRunning;

	public WeakTask(ScheduledFuture<?> referent, boolean cancelMayInterruptIfRunning) {
	    super(referent);

	    this.cancelMayInterruptIfRunning = cancelMayInterruptIfRunning;
	}
    }
}
