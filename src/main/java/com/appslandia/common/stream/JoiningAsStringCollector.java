// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.stream;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 *
 * @author Loc Ha
 *
 */
public class JoiningAsStringCollector<T> implements Collector<T, StringBuilder, String> {

  private int count = 0;
  final int groupSize;
  final String lineSeparator;

  public JoiningAsStringCollector() {
    this(5, System.lineSeparator());
  }

  public JoiningAsStringCollector(int groupSize, String lineSeparator) {
    this.groupSize = groupSize;
    this.lineSeparator = lineSeparator;
  }

  @Override
  public Supplier<StringBuilder> supplier() {
    return StringBuilder::new;
  }

  @Override
  public BiConsumer<StringBuilder, T> accumulator() {
    return (sb, t) -> {
      count++;

      sb.append(t).append(lineSeparator);

      if (count > 0 && count % groupSize == 0) {
        sb.append(lineSeparator);
      }
    };
  }

  @Override
  public BinaryOperator<StringBuilder> combiner() {
    return StringBuilder::append;
  }

  @Override
  public Function<StringBuilder, String> finisher() {
    return sb -> sb.toString().strip();
  }

  @Override
  public Set<Characteristics> characteristics() {
    return Collections.emptySet();
  }
}
