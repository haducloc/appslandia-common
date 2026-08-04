// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *
 * @author Loc Ha
 *
 */
public class StreamUtils {

  public static <T> Stream<T> stream(Enumeration<T> enumer) {

    return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<T>() {

      @Override
      public T next() {
        return enumer.nextElement();
      }

      @Override
      public boolean hasNext() {
        return enumer.hasMoreElements();
      }
    }, Spliterator.ORDERED), false);
  }

  public static <T> Stream<T> stream(Iterator<T> iter) {
    return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iter, Spliterator.ORDERED), false);
  }

  public static <T> Stream<T> stream(Iterable<T> iter) {
    return StreamSupport.stream(iter.spliterator(), false);
  }
}
