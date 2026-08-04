// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.base;

import java.util.Iterator;

/**
 *
 *
 * @author Loc Ha
 *
 */
public class UnmodifiableIterator<E> implements Iterator<E> {

  final Iterator<? extends E> iter;

  public UnmodifiableIterator(Iterator<? extends E> iter) {
    super();
    this.iter = iter;
  }

  @Override
  public boolean hasNext() {
    return iter.hasNext();
  }

  @Override
  public E next() {
    return iter.next();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }
}
