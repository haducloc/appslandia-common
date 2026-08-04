// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.threading;

/**
 *
 * @author Loc Ha
 *
 */
public class ThreadLocalStorage<T> {

  final ThreadLocal<T> holder;

  public ThreadLocalStorage() {
    this(false);
  }

  public ThreadLocalStorage(ThreadLocal<T> holder) {
    this.holder = holder;
  }

  public ThreadLocalStorage(boolean inheritable) {
    if (!inheritable) {
      holder = new ThreadLocal<>();
    } else {
      holder = new InheritableThreadLocal<>();
    }
  }

  public T get() {
    return holder.get();
  }

  public T val() {
    var val = holder.get();
    if (val == null) {
      throw new IllegalStateException("No current value found in the current thread.");
    }
    return val;
  }

  public void set(T value) {
    if (value == null) {
      holder.remove();
    } else {
      holder.set(value);
    }
  }

  public T remove() {
    var t = holder.get();
    holder.remove();
    return t;
  }

  public boolean hasValue() {
    return holder.get() != null;
  }
}
