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

package com.appslandia.common.jpa;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.appslandia.common.jdbc.DbDialect;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.common.utils.STR;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.metamodel.Metamodel;

/**
 *
 * @author Loc Ha
 *
 */
public abstract class JpaEntityManager implements EntityManager {

  protected abstract EntityManager em();

  public void insert(Object entity) {
    this.em().persist(entity);
    this.em().flush();
  }

  public void removeByPk(Class<?> type, Object primaryKey) throws EntityNotFoundException {
    Object ref = this.em().getReference(type, primaryKey);
    this.em().remove(ref);
  }

  public boolean isInCache(Class<?> type, Object primaryKey) {
    var cache = this.em().getEntityManagerFactory().getCache();
    Asserts.notNull(cache);

    return cache.contains(type, primaryKey);
  }

  public void evictCache() {
    var cache = this.em().getEntityManagerFactory().getCache();
    Asserts.notNull(cache);

    cache.evictAll();
  }

  public void evictCache(Class<?> type) {
    var cache = this.em().getEntityManagerFactory().getCache();
    Asserts.notNull(cache);

    cache.evict(type);
  }

  public void evictCache(Class<?> type, Object primaryKey) {
    var cache = this.em().getEntityManagerFactory().getCache();
    Asserts.notNull(cache);

    cache.evict(type, primaryKey);
  }

  // JpaQuery

  public <T> TypedQueryImpl<T> createQuery(JpaQuery pQuery, Class<T> resultClass) {
    return new TypedQueryImpl<>(this.em().createQuery(pQuery.getTranslatedQuery(), resultClass), getHintMapper(),
        pQuery, getDbDialect());
  }

  public QueryImpl createQuery(JpaQuery pQuery) {
    return new QueryImpl(this.em().createQuery(pQuery.getTranslatedQuery()), pQuery, getDbDialect());
  }

  public QueryImpl createNativeQuery(JpaQuery pNativeQuery, Class<?> resultClass) {
    return new QueryImpl(this.em().createNativeQuery(pNativeQuery.getTranslatedQuery(), resultClass), pNativeQuery,
        getDbDialect());
  }

  public QueryImpl createNativeQuery(JpaQuery pNativeQuery) {
    return new QueryImpl(this.em().createNativeQuery(pNativeQuery.getTranslatedQuery()), pNativeQuery, getDbDialect());
  }

  public QueryImpl createNativeQuery(JpaQuery pNativeQuery, String resultSetMapping) {
    return new QueryImpl(this.em().createNativeQuery(pNativeQuery.getTranslatedQuery(), resultSetMapping), pNativeQuery,
        getDbDialect());
  }

  // jakarta.persistence.EntityManager

  @Override
  public void remove(Object entity) {
    this.em().remove(entity);
  }

  @Override
  public void lock(Object entity, LockModeType lockMode) {
    this.em().lock(entity, lockMode);
  }

  @Override
  public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
    this.em().lock(entity, lockMode, properties);
  }

  @Override
  public void setProperty(String propertyName, Object value) {
    this.em().setProperty(propertyName, value);
  }

  @Override
  public void clear() {
    this.em().clear();
  }

  @Override
  public boolean contains(Object entity) {
    return this.em().contains(entity);
  }

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey) {
    return this.em().find(entityClass, primaryKey);
  }

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
    return this.em().find(entityClass, primaryKey, properties);
  }

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
    return this.em().find(entityClass, primaryKey, lockMode);
  }

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
    return this.em().find(entityClass, primaryKey, lockMode, properties);
  }

  @Override
  public Map<String, Object> getProperties() {
    return this.em().getProperties();
  }

  @Override
  public void close() {
    this.em().close();
  }

  @Override
  public void flush() {
    this.em().flush();
  }

  @Override
  public <T> T merge(T entity) {
    return this.em().merge(entity);
  }

  @Override
  public <T> T unwrap(Class<T> cls) {
    return this.em().unwrap(cls);
  }

  @Override
  public boolean isOpen() {
    return this.em().isOpen();
  }

  @Override
  public void detach(Object entity) {
    this.em().detach(entity);
  }

  @SuppressWarnings("rawtypes")
  @Override
  public QueryImpl createNativeQuery(String sqlString, Class resultClass) {
    return new QueryImpl(this.em().createNativeQuery(sqlString, resultClass));
  }

  @Override
  public QueryImpl createNativeQuery(String sqlString) {
    return new QueryImpl(this.em().createNativeQuery(sqlString));
  }

  @Override
  public QueryImpl createNativeQuery(String sqlString, String resultSetMapping) {
    return new QueryImpl(this.em().createNativeQuery(sqlString, resultSetMapping));
  }

  @Override
  public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
    return this.em().createNamedStoredProcedureQuery(name);
  }

  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
    return this.em().createStoredProcedureQuery(procedureName, resultSetMappings);
  }

  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
    return this.em().createStoredProcedureQuery(procedureName);
  }

  @SuppressWarnings("rawtypes")
  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
    return this.em().createStoredProcedureQuery(procedureName, resultClasses);
  }

  @Override
  public boolean isJoinedToTransaction() {
    return this.em().isJoinedToTransaction();
  }

  @Override
  public EntityManagerFactory getEntityManagerFactory() {
    return this.em().getEntityManagerFactory();
  }

  @Override
  public CriteriaBuilder getCriteriaBuilder() {
    return this.em().getCriteriaBuilder();
  }

  @Override
  public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
    return this.em().createEntityGraph(rootType);
  }

  @Override
  public EntityGraph<?> createEntityGraph(String graphName) {
    return this.em().createEntityGraph(graphName);
  }

  @Override
  public void persist(Object entity) {
    this.em().persist(entity);
  }

  @Override
  public <T> T getReference(Class<T> entityClass, Object primaryKey) {
    return this.em().getReference(entityClass, primaryKey);
  }

  @Override
  public void setFlushMode(FlushModeType flushMode) {
    this.em().setFlushMode(flushMode);
  }

  @Override
  public FlushModeType getFlushMode() {
    return this.em().getFlushMode();
  }

  @Override
  public void refresh(Object entity, LockModeType lockMode) {
    this.em().refresh(entity, lockMode);
  }

  @Override
  public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
    this.em().refresh(entity, lockMode, properties);
  }

  @Override
  public void refresh(Object entity) {
    this.em().refresh(entity);
  }

  @Override
  public void refresh(Object entity, Map<String, Object> properties) {
    this.em().refresh(entity, properties);
  }

  @Override
  public LockModeType getLockMode(Object entity) {
    return this.em().getLockMode(entity);
  }

  @Override
  public <T> TypedQueryImpl<T> createQuery(CriteriaQuery<T> criteriaQuery) {
    return new TypedQueryImpl<>(this.em().createQuery(criteriaQuery), getHintMapper());
  }

  @Override
  public <T> TypedQueryImpl<T> createQuery(String qlString, Class<T> resultClass) {
    return new TypedQueryImpl<>(this.em().createQuery(qlString, resultClass), getHintMapper());
  }

  @SuppressWarnings("rawtypes")
  @Override
  public QueryImpl createQuery(CriteriaUpdate updateQuery) {
    return new QueryImpl(this.em().createQuery(updateQuery));
  }

  @SuppressWarnings("rawtypes")
  @Override
  public QueryImpl createQuery(CriteriaDelete deleteQuery) {
    return new QueryImpl(this.em().createQuery(deleteQuery));
  }

  @Override
  public QueryImpl createQuery(String qlString) {
    return new QueryImpl(this.em().createQuery(qlString));
  }

  @Override
  public <T> TypedQueryImpl<T> createNamedQuery(String name, Class<T> resultClass) {
    return new TypedQueryImpl<>(this.em().createNamedQuery(name, resultClass), getHintMapper());
  }

  @Override
  public QueryImpl createNamedQuery(String name) {
    return new QueryImpl(this.em().createNamedQuery(name));
  }

  @Override
  public void joinTransaction() {
    this.em().joinTransaction();
  }

  @Override
  public Object getDelegate() {
    return this.em().getDelegate();
  }

  @Override
  public EntityTransaction getTransaction() {
    return this.em().getTransaction();
  }

  @Override
  public Metamodel getMetamodel() {
    return this.em().getMetamodel();
  }

  @Override
  public EntityGraph<?> getEntityGraph(String graphName) {
    return this.em().getEntityGraph(graphName);
  }

  @Override
  public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
    return this.em().getEntityGraphs(entityClass);
  }

  @Override
  public String toString() {
    return ObjectUtils.toStringWrapper(this, this.em());
  }

  protected String getURL() throws PersistenceException {
    var props = this.em().getEntityManagerFactory().getProperties();

    var url = (String) props.get("jakarta.persistence.jdbc.url");
    if (url == null) {
      url = (String) props.get(JpaUtils.PU_PROP_JDBC_DB_URL);
    }
    if (url == null) {
      throw new PersistenceException(STR.fmt("The '{}' property was not found in the EntityManagerFactory properties.",
          JpaUtils.PU_PROP_JDBC_DB_URL));
    }
    return url;
  }

  public DbDialect getDbDialect() throws PersistenceException {
    return DB_DIALECTS.computeIfAbsent(this.getURL(), url -> DbDialect.parse(url));
  }

  private static final ConcurrentMap<String, DbDialect> DB_DIALECTS = new ConcurrentHashMap<>();

  public HintMapper getHintMapper() throws PersistenceException {
    return HINT_MAPPERS.computeIfAbsent(this.getURL(), url -> {
      final var mapper = new HintMapper();
      var props = this.em().getEntityManagerFactory().getProperties();

      props.keySet().forEach(prop -> {
        if (prop.startsWith(JpaUtils.PU_PROP_HINT_PREFIX)) {
          mapper.registerHint(prop, (String) props.get(prop));
        }
      });
      return mapper;
    });
  }

  private static final ConcurrentMap<String, HintMapper> HINT_MAPPERS = new ConcurrentHashMap<>();
}
