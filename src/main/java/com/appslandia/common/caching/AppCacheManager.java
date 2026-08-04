// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.caching;

/**
 *
 * @author Loc Ha
 *
 */
public interface AppCacheManager {

  <K, V> AppCache<K, V> getCache(String cacheName) throws IllegalArgumentException;

  boolean clearCache(String cacheName);

  boolean destroyCache(String cacheName);

  Iterable<String> getCacheNames();

  void close();
}
