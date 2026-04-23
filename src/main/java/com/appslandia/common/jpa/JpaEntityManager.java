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

import java.sql.Connection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.appslandia.common.jdbc.DbDialect;
import com.appslandia.common.jdbc.JdbcUtils;
import com.appslandia.common.jdbc.UncheckedSQLException;
import com.appslandia.common.threading.LazyValue;
import com.appslandia.common.utils.Asserts;

import jakarta.persistence.Cache;
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.CacheStoreMode;
import jakarta.persistence.ConnectionConsumer;
import jakarta.persistence.ConnectionFunction;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.FindOption;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.LockOption;
import jakarta.persistence.RefreshOption;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.TypedQueryReference;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaSelect;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.metamodel.Metamodel;

/**
 *
 * @author Loc Ha
 *
 */
public abstract class JpaEntityManager implements EntityManager {

  protected abstract EntityManager em();

  public void persistFlush(Object entity) {
    em().persist(entity);
    em().flush();
  }

  public <T> void removeByPk(Class<T> type, Object primaryKey) throws EntityNotFoundException {
    var ref = em().getReference(type, primaryKey);
    em().remove(ref);
  }

  public Cache getL2Cache() {
    var cache = em().getEntityManagerFactory().getCache();
    return Asserts.notNull(cache, "No second-level cache in use.");
  }

  // JpaQuery

  public <T> TypedQueryImpl<T> createQuery(JpaQuery pQuery, Class<T> resultClass) {
    var q = em().createQuery(pQuery.getTranslatedQuery(), resultClass);
    return new TypedQueryImpl<>(q, pQuery, getJpaProvider(), getDbDialect());
  }

  public QueryImpl createQuery(JpaQuery pQuery) {
    var q = em().createQuery(pQuery.getTranslatedQuery());
    return new QueryImpl(q, pQuery, getDbDialect());
  }

  public QueryImpl createNativeQuery(JpaQuery pNativeQuery, Class<?> resultClass) {
    var q = em().createNativeQuery(pNativeQuery.getTranslatedQuery(), resultClass);
    return new QueryImpl(q, pNativeQuery, getDbDialect());
  }

  public QueryImpl createNativeQuery(JpaQuery pNativeQuery) {
    var q = em().createNativeQuery(pNativeQuery.getTranslatedQuery());
    return new QueryImpl(q, pNativeQuery, getDbDialect());
  }

  public QueryImpl createNativeQuery(JpaQuery pNativeQuery, String resultSetMapping) {
    var q = em().createNativeQuery(pNativeQuery.getTranslatedQuery(), resultSetMapping);
    return new QueryImpl(q, pNativeQuery, getDbDialect());
  }

  // jakarta.persistence.EntityManager

  @Override
  public void remove(Object entity) {
    em().remove(entity);
  }

  @Override
  public void lock(Object entity, LockModeType lockMode) {
    em().lock(entity, lockMode);
  }

  @Override
  public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
    em().lock(entity, lockMode, properties);
  }

  @Override
  public void setProperty(String propertyName, Object value) {
    em().setProperty(propertyName, value);
  }

  @Override
  public void clear() {
    em().clear();
  }

  @Override
  public boolean contains(Object entity) {
    return em().contains(entity);
  }

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey) {
    return em().find(entityClass, primaryKey);
  }

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
    return em().find(entityClass, primaryKey, properties);
  }

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
    return em().find(entityClass, primaryKey, lockMode);
  }

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
    return em().find(entityClass, primaryKey, lockMode, properties);
  }

  @Override
  public Map<String, Object> getProperties() {
    return em().getProperties();
  }

  @Override
  public void close() {
    em().close();
  }

  @Override
  public void flush() {
    em().flush();
  }

  @Override
  public <T> T merge(T entity) {
    return em().merge(entity);
  }

  @Override
  public <T> T unwrap(Class<T> cls) {
    return em().unwrap(cls);
  }

  @Override
  public boolean isOpen() {
    return em().isOpen();
  }

  @Override
  public void detach(Object entity) {
    em().detach(entity);
  }

  @Override
  public <T> QueryImpl createNativeQuery(String sqlString, Class<T> resultClass) {
    return new QueryImpl(em().createNativeQuery(sqlString, resultClass));
  }

  @Override
  public QueryImpl createNativeQuery(String sqlString) {
    return new QueryImpl(em().createNativeQuery(sqlString));
  }

  @Override
  public QueryImpl createNativeQuery(String sqlString, String resultSetMapping) {
    return new QueryImpl(em().createNativeQuery(sqlString, resultSetMapping));
  }

  @Override
  public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
    return em().createNamedStoredProcedureQuery(name);
  }

  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
    return em().createStoredProcedureQuery(procedureName, resultSetMappings);
  }

  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
    return em().createStoredProcedureQuery(procedureName);
  }

  @SuppressWarnings("rawtypes")
  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
    return em().createStoredProcedureQuery(procedureName, resultClasses);
  }

  @Override
  public boolean isJoinedToTransaction() {
    return em().isJoinedToTransaction();
  }

  @Override
  public EntityManagerFactory getEntityManagerFactory() {
    return em().getEntityManagerFactory();
  }

  @Override
  public CriteriaBuilder getCriteriaBuilder() {
    return em().getCriteriaBuilder();
  }

  @Override
  public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
    return em().createEntityGraph(rootType);
  }

  @Override
  public EntityGraph<?> createEntityGraph(String graphName) {
    return em().createEntityGraph(graphName);
  }

  @Override
  public void persist(Object entity) {
    em().persist(entity);
  }

  @Override
  public <T> T getReference(Class<T> entityClass, Object primaryKey) {
    return em().getReference(entityClass, primaryKey);
  }

  @Override
  public void setFlushMode(FlushModeType flushMode) {
    em().setFlushMode(flushMode);
  }

  @Override
  public FlushModeType getFlushMode() {
    return em().getFlushMode();
  }

  @Override
  public void refresh(Object entity, LockModeType lockMode) {
    em().refresh(entity, lockMode);
  }

  @Override
  public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
    em().refresh(entity, lockMode, properties);
  }

  @Override
  public void refresh(Object entity) {
    em().refresh(entity);
  }

  @Override
  public void refresh(Object entity, Map<String, Object> properties) {
    em().refresh(entity, properties);
  }

  @Override
  public LockModeType getLockMode(Object entity) {
    return em().getLockMode(entity);
  }

  @Override
  public <T> TypedQueryImpl<T> createQuery(CriteriaQuery<T> criteriaQuery) {
    return new TypedQueryImpl<>(em().createQuery(criteriaQuery), getJpaProvider());
  }

  @Override
  public <T> TypedQueryImpl<T> createQuery(String qlString, Class<T> resultClass) {
    return new TypedQueryImpl<>(em().createQuery(qlString, resultClass), getJpaProvider());
  }

  @SuppressWarnings("rawtypes")
  @Override
  public QueryImpl createQuery(CriteriaUpdate updateQuery) {
    return new QueryImpl(em().createQuery(updateQuery));
  }

  @SuppressWarnings("rawtypes")
  @Override
  public QueryImpl createQuery(CriteriaDelete deleteQuery) {
    return new QueryImpl(em().createQuery(deleteQuery));
  }

  @Override
  public QueryImpl createQuery(String qlString) {
    return new QueryImpl(em().createQuery(qlString));
  }

  @Override
  public <T> TypedQueryImpl<T> createNamedQuery(String name, Class<T> resultClass) {
    return new TypedQueryImpl<>(em().createNamedQuery(name, resultClass), getJpaProvider());
  }

  @Override
  public QueryImpl createNamedQuery(String name) {
    return new QueryImpl(em().createNamedQuery(name));
  }

  @Override
  public void joinTransaction() {
    em().joinTransaction();
  }

  @Override
  public Object getDelegate() {
    return em().getDelegate();
  }

  @Override
  public EntityTransaction getTransaction() {
    return em().getTransaction();
  }

  @Override
  public Metamodel getMetamodel() {
    return em().getMetamodel();
  }

  @Override
  public EntityGraph<?> getEntityGraph(String graphName) {
    return em().getEntityGraph(graphName);
  }

  @Override
  public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
    return em().getEntityGraphs(entityClass);
  }

  /* JPA 3.2 */

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey, FindOption... options) {
    return em().find(entityClass, primaryKey, options);
  }

  @Override
  public <T> T find(EntityGraph<T> entityGraph, Object primaryKey, FindOption... options) {
    return em().find(entityGraph, primaryKey, options);
  }

  @Override
  public <T> T getReference(T entity) {
    return em().getReference(entity);
  }

  @Override
  public void lock(Object entity, LockModeType lockMode, LockOption... options) {
    em().lock(entity, lockMode, options);
  }

  @Override
  public void refresh(Object entity, RefreshOption... options) {
    em().refresh(entity, options);
  }

  @Override
  public void setCacheRetrieveMode(CacheRetrieveMode cacheRetrieveMode) {
    em().setCacheRetrieveMode(cacheRetrieveMode);
  }

  @Override
  public void setCacheStoreMode(CacheStoreMode cacheStoreMode) {
    em().setCacheStoreMode(cacheStoreMode);
  }

  @Override
  public CacheRetrieveMode getCacheRetrieveMode() {
    return em().getCacheRetrieveMode();
  }

  @Override
  public CacheStoreMode getCacheStoreMode() {
    return em().getCacheStoreMode();
  }

  @Override
  public <T> TypedQueryImpl<T> createQuery(CriteriaSelect<T> selectQuery) {
    return new TypedQueryImpl<>(em().createQuery(selectQuery), getJpaProvider());
  }

  @Override
  public <T> TypedQueryImpl<T> createQuery(TypedQueryReference<T> reference) {
    return new TypedQueryImpl<>(em().createQuery(reference), getJpaProvider());
  }

  @Override
  public <C> void runWithConnection(ConnectionConsumer<C> action) {
    em().runWithConnection(action);
  }

  @Override
  public <C, T> T callWithConnection(ConnectionFunction<C, T> function) {
    return em().callWithConnection(function);
  }

  private final LazyValue<JpaProvider> jpaProvider = new LazyValue<>();

  public JpaProvider getJpaProvider() {
    return jpaProvider.get(this::doGetJpaProvider);
  }

  private JpaProvider doGetJpaProvider() {
    var providerValue = em().getEntityManagerFactory().getProperties().get("jakarta.persistence.provider");

    if (providerValue == null) {
      return JpaProvider.OTHER;
    }
    var provider = providerValue.toString().toLowerCase(Locale.ROOT);

    if (provider.contains("hibernate")) {
      return JpaProvider.HIBERNATE;
    }
    if (provider.contains("eclipse")) {
      return JpaProvider.ECLIPSE_LINK;
    }
    return JpaProvider.OTHER;
  }

  protected String getURL() throws UncheckedSQLException {

    return em().callWithConnection(c -> {
      if (!(c instanceof Connection conn)) {
        throw new IllegalStateException("callWithConnection() must supply a java.sql.Connection, but got: "
            + (c == null ? "null" : c.getClass().getName()));
      }
      return JdbcUtils.getDataSourceId(conn);
    });
  }

  public DbDialect getDbDialect() throws UncheckedSQLException {
    return DB_DIALECTS.computeIfAbsent(getURL(), url -> DbDialect.parse(url));
  }

  private static final ConcurrentMap<String, DbDialect> DB_DIALECTS = new ConcurrentHashMap<>();
}
