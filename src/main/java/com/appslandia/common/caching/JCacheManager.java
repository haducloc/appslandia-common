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

package com.appslandia.common.caching;

import javax.cache.Cache;
import javax.cache.CacheManager;

import com.appslandia.common.utils.STR;

/**
 *
 * @author Loc Ha
 *
 */
public class JCacheManager implements AppCacheManager {

  final CacheManager cacheManager;

  public JCacheManager(CacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }

  @Override
  public <K, V> AppCache<K, V> getCache(String cacheName) throws IllegalArgumentException {
    Cache<K, V> cache = cacheManager.getCache(cacheName);
    if (cache == null) {
      throw new IllegalArgumentException(STR.fmt("The cacheName '{}' is invalid.", cacheName));
    }
    return new JCache<>(cache);
  }

  @Override
  public boolean clearCache(String cacheName) {
    Cache<?, ?> cache = cacheManager.getCache(cacheName);
    if (cache != null) {
      cache.clear();
      return true;
    }
    return false;
  }

  @Override
  public boolean destroyCache(String cacheName) {
    if (cacheManager.getCache(cacheName) != null) {
      cacheManager.destroyCache(cacheName);
      return true;
    }
    return false;
  }

  @Override
  public Iterable<String> getCacheNames() {
    return cacheManager.getCacheNames();
  }

  @Override
  public void close() {
    cacheManager.close();
  }
}
