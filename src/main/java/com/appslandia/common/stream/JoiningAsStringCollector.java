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
      this.count++;

      sb.append(t).append(this.lineSeparator);

      if (this.count > 0 && this.count % this.groupSize == 0) {
        sb.append(this.lineSeparator);
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
