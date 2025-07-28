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

package com.appslandia.common.models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Loc Ha
 *
 */
public interface LocalIdSupport {

  Integer getLocalId();

  void setLocalId(Integer localId);

  public static void assignLocalIds(List<? extends LocalIdSupport> subModels) {
    for (var idx = 0; idx < subModels.size(); idx++) {
      LocalIdSupport record = subModels.get(idx);
      record.setLocalId(idx);
    }
  }

  public static void assertLocalIds(List<? extends LocalIdSupport> subModels) {
    Set<Integer> ids = new HashSet<>();
    for (var rec : subModels) {
      var localId = rec.getLocalId();

      if (localId == null) {
        throw new IllegalStateException("The LocalIdSupport.getLocalId() must be not null.");
      }
      if (!ids.add(localId)) {
        throw new IllegalStateException("The LocalIdSupport.getLocalId() must be unique.");
      }
    }
  }
}
