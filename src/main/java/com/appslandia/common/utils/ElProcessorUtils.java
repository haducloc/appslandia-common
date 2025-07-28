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

package com.appslandia.common.utils;

import com.appslandia.common.base.InitializeException;
import com.appslandia.common.threading.BlockingQueuePool;

import jakarta.el.ELProcessor;

/**
 *
 * @author Loc Ha
 *
 */
public class ElProcessorUtils {

  public static ELProcessor obtain() {
    return __default.obtain();
  }

  public static boolean release(ELProcessor impl) {
    return __default.release(impl);
  }

  private static final BlockingQueuePool<ELProcessor> __default = initDefault();

  private static BlockingQueuePool<ELProcessor> initDefault() {
    // -Del_pool_size=32
    var poolSize = SYS.resolveInt("{el_pool_size,env.EL_POOL_SIZE}", 32);
    return new BlockingQueuePool<ELProcessor>(() -> initELProcessor(), poolSize);
  }

  static ELProcessor initELProcessor() {
    try {
      return new ELProcessor();
    } catch (Exception ex) {
      throw new InitializeException("Failed to create an instance of jakarta.el.ELProcessor.", ex);
    }
  }
}
