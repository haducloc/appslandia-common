// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
