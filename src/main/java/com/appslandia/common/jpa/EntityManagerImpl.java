// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

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
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.appslandia.common.base.Params;
import com.appslandia.common.jdbc.DbDialect;
import com.appslandia.common.jdbc.JdbcUtils;
import com.appslandia.common.utils.ObjectUtils;

import jakarta.persistence.Cache;
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
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class EntityManagerImpl implements EntityManager {

  protected final EntityManager em;
  protected final String dataSourceId;
  protected final DbDialect dbDialect;
  protected final ConnectionUnwrapper connectionUnwrapper;

  public EntityManagerImpl(EntityManager em, ConnectionUnwrapper connectionUnwrapper) {
    this(em, null, connectionUnwrapper);
  }

  public EntityManagerImpl(EntityManager em, String dataSourceId, ConnectionUnwrapper connectionUnwrapper) {
    this.em = em;
    this.connectionUnwrapper = connectionUnwrapper;
    this.dataSourceId = (dataSourceId == null) ? JdbcUtils.getDataSourceId(connectionUnwrapper.unwrap(em))
        : dataSourceId;
    this.dbDialect = lookupDbDialect();
  }

  public String getDataSourceId() {
    return this.dataSourceId;
  }

  public DbDialect getDbDialect() {
    return this.dbDialect;
  }

  public Connection unwrapConnection() {
    return this.connectionUnwrapper.unwrap(this.em);
  }

  public void insert(Object entity) {
    this.em.persist(entity);
    this.em.flush();
  }

  public void removeByPk(Class<?> type, Object primaryKey) throws EntityNotFoundException {
    Object ref = this.em.getReference(type, primaryKey);
    this.em.remove(ref);
  }

  public boolean isInCache(Class<?> type, Object primaryKey) {
    Cache cache = this.em.getEntityManagerFactory().getCache();
    return (cache != null) && cache.contains(type, primaryKey);
  }

  public void evictCache() {
    Cache cache = this.em.getEntityManagerFactory().getCache();
    if (cache != null) {
      cache.evictAll();
    }
  }

  public void evictCache(Class<?> type) {
    Cache cache = this.em.getEntityManagerFactory().getCache();
    if (cache != null) {
      cache.evict(type);
    }
  }

  public void evictCache(Class<?> type, Object primaryKey) {
    Cache cache = this.em.getEntityManagerFactory().getCache();
    if (cache != null) {
      cache.evict(type, primaryKey);
    }
  }

  public <T> T findFetch(Class<T> entityClass, Object primaryKey, String graphName) {
    return this.em.find(entityClass, primaryKey,
        new Params().set(JpaHints.HINT_JPA_FETCH_GRAPH, this.em.getEntityGraph(graphName)));
  }

  public <T> T findLoad(Class<T> entityClass, Object primaryKey, String graphName) {
    return this.em.find(entityClass, primaryKey,
        new Params().set(JpaHints.HINT_JPA_LOAD_GRAPH, this.em.getEntityGraph(graphName)));
  }

  public <T> TypedQueryImpl<T> createQueryFetch(String qlString, Class<T> resultClass, String graphName) {
    return new TypedQueryImpl<T>(this.em.createQuery(qlString, resultClass).setHint(JpaHints.HINT_JPA_FETCH_GRAPH,
        this.em.getEntityGraph(graphName)), this.dbDialect);
  }

  public <T> TypedQueryImpl<T> createQueryLoad(String qlString, Class<T> resultClass, String graphName) {
    return new TypedQueryImpl<T>(this.em.createQuery(qlString, resultClass).setHint(JpaHints.HINT_JPA_LOAD_GRAPH,
        this.em.getEntityGraph(graphName)), this.dbDialect);
  }

  public <T> TypedQueryImpl<T> createNamedQueryFetch(String name, Class<T> resultClass, String graphName) {
    return new TypedQueryImpl<T>(this.em.createNamedQuery(name, resultClass).setHint(JpaHints.HINT_JPA_FETCH_GRAPH,
        this.em.getEntityGraph(graphName)), this.dbDialect);
  }

  public <T> TypedQueryImpl<T> createNamedQueryLoad(String name, Class<T> resultClass, String graphName) {
    return new TypedQueryImpl<T>(this.em.createNamedQuery(name, resultClass).setHint(JpaHints.HINT_JPA_LOAD_GRAPH,
        this.em.getEntityGraph(graphName)), this.dbDialect);
  }

  // JpaQuery

  public <T> TypedQueryImpl<T> createQuery(JpaQuery pQuery, Class<T> resultClass) {
    return new TypedQueryImpl<T>(this.em.createQuery(pQuery.getTranslatedQuery(), resultClass), pQuery, this.dbDialect);
  }

  public QueryImpl createQuery(JpaQuery pQuery) {
    return new QueryImpl(this.em.createQuery(pQuery.getTranslatedQuery()), pQuery, this.dbDialect);
  }

  public QueryImpl createNativeQuery(JpaQuery pQuery, Class<?> resultClass) {
    return new QueryImpl(this.em.createNativeQuery(pQuery.getTranslatedQuery(), resultClass), pQuery, this.dbDialect);
  }

  public QueryImpl createNativeQuery(JpaQuery pQuery) {
    return new QueryImpl(this.em.createNativeQuery(pQuery.getTranslatedQuery()), pQuery, this.dbDialect);
  }

  public QueryImpl createNativeQuery(JpaQuery pQuery, String resultSetMapping) {
    return new QueryImpl(this.em.createNativeQuery(pQuery.getTranslatedQuery(), resultSetMapping), pQuery,
        this.dbDialect);
  }

  // jakarta.persistence.EntityManager

  @Override
  public void remove(Object entity) {
    this.em.remove(entity);
  }

  @Override
  public void lock(Object entity, LockModeType lockMode) {
    this.em.lock(entity, lockMode);
  }

  @Override
  public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
    this.em.lock(entity, lockMode, properties);
  }

  @Override
  public void setProperty(String propertyName, Object value) {
    this.em.setProperty(propertyName, value);
  }

  @Override
  public void clear() {
    this.em.clear();
  }

  @Override
  public boolean contains(Object entity) {
    return this.em.contains(entity);
  }

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey) {
    return this.em.find(entityClass, primaryKey);
  }

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
    return this.em.find(entityClass, primaryKey, properties);
  }

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
    return this.em.find(entityClass, primaryKey, lockMode);
  }

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
    return this.em.find(entityClass, primaryKey, lockMode, properties);
  }

  @Override
  public Map<String, Object> getProperties() {
    return this.em.getProperties();
  }

  @Override
  public void close() {
    this.em.close();
  }

  @Override
  public void flush() {
    this.em.flush();
  }

  @Override
  public <T> T merge(T entity) {
    return this.em.merge(entity);
  }

  @Override
  public <T> T unwrap(Class<T> cls) {
    return this.em.unwrap(cls);
  }

  @Override
  public boolean isOpen() {
    return this.em.isOpen();
  }

  @Override
  public void detach(Object entity) {
    this.em.detach(entity);
  }

  @SuppressWarnings("rawtypes")
  @Override
  public QueryImpl createNativeQuery(String sqlString, Class resultClass) {
    return new QueryImpl(this.em.createNativeQuery(sqlString, resultClass), this.dbDialect);
  }

  @Override
  public QueryImpl createNativeQuery(String sqlString) {
    return new QueryImpl(this.em.createNativeQuery(sqlString), this.dbDialect);
  }

  @Override
  public QueryImpl createNativeQuery(String sqlString, String resultSetMapping) {
    return new QueryImpl(this.em.createNativeQuery(sqlString, resultSetMapping), this.dbDialect);
  }

  @Override
  public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
    return this.em.createNamedStoredProcedureQuery(name);
  }

  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
    return this.em.createStoredProcedureQuery(procedureName, resultSetMappings);
  }

  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
    return this.em.createStoredProcedureQuery(procedureName);
  }

  @SuppressWarnings("rawtypes")
  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
    return this.em.createStoredProcedureQuery(procedureName, resultClasses);
  }

  @Override
  public boolean isJoinedToTransaction() {
    return this.em.isJoinedToTransaction();
  }

  @Override
  public EntityManagerFactory getEntityManagerFactory() {
    return this.em.getEntityManagerFactory();
  }

  @Override
  public CriteriaBuilder getCriteriaBuilder() {
    return this.em.getCriteriaBuilder();
  }

  @Override
  public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
    return this.em.createEntityGraph(rootType);
  }

  @Override
  public EntityGraph<?> createEntityGraph(String graphName) {
    return this.em.createEntityGraph(graphName);
  }

  @Override
  public void persist(Object entity) {
    this.em.persist(entity);
  }

  @Override
  public <T> T getReference(Class<T> entityClass, Object primaryKey) {
    return this.em.getReference(entityClass, primaryKey);
  }

  @Override
  public void setFlushMode(FlushModeType flushMode) {
    this.em.setFlushMode(flushMode);
  }

  @Override
  public FlushModeType getFlushMode() {
    return this.em.getFlushMode();
  }

  @Override
  public void refresh(Object entity, LockModeType lockMode) {
    this.em.refresh(entity, lockMode);
  }

  @Override
  public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
    this.em.refresh(entity, lockMode, properties);
  }

  @Override
  public void refresh(Object entity) {
    this.em.refresh(entity);
  }

  @Override
  public void refresh(Object entity, Map<String, Object> properties) {
    this.em.refresh(entity, properties);
  }

  @Override
  public LockModeType getLockMode(Object entity) {
    return this.em.getLockMode(entity);
  }

  @Override
  public <T> TypedQueryImpl<T> createQuery(CriteriaQuery<T> criteriaQuery) {
    return new TypedQueryImpl<T>(this.em.createQuery(criteriaQuery), this.dbDialect);
  }

  @Override
  public <T> TypedQueryImpl<T> createQuery(String qlString, Class<T> resultClass) {
    return new TypedQueryImpl<T>(this.em.createQuery(qlString, resultClass), this.dbDialect);
  }

  @SuppressWarnings("rawtypes")
  @Override
  public QueryImpl createQuery(CriteriaUpdate updateQuery) {
    return new QueryImpl(this.em.createQuery(updateQuery), this.dbDialect);
  }

  @SuppressWarnings("rawtypes")
  @Override
  public QueryImpl createQuery(CriteriaDelete deleteQuery) {
    return new QueryImpl(this.em.createQuery(deleteQuery), this.dbDialect);
  }

  @Override
  public QueryImpl createQuery(String qlString) {
    return new QueryImpl(this.em.createQuery(qlString), this.dbDialect);
  }

  @Override
  public <T> TypedQueryImpl<T> createNamedQuery(String name, Class<T> resultClass) {
    return new TypedQueryImpl<T>(this.em.createNamedQuery(name, resultClass), this.dbDialect);
  }

  @Override
  public QueryImpl createNamedQuery(String name) {
    return new QueryImpl(this.em.createNamedQuery(name), this.dbDialect);
  }

  @Override
  public void joinTransaction() {
    this.em.joinTransaction();
  }

  @Override
  public Object getDelegate() {
    return this.em.getDelegate();
  }

  @Override
  public EntityTransaction getTransaction() {
    return this.em.getTransaction();
  }

  @Override
  public Metamodel getMetamodel() {
    return this.em.getMetamodel();
  }

  @Override
  public EntityGraph<?> getEntityGraph(String graphName) {
    return this.em.getEntityGraph(graphName);
  }

  @Override
  public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
    return this.em.getEntityGraphs(entityClass);
  }

  @Override
  public String toString() {
    return ObjectUtils.toStringWrapper(this, this.em);
  }

  protected DbDialect lookupDbDialect() throws PersistenceException {
    return DB_DIALECTS.computeIfAbsent(this.dataSourceId, u -> {

      try {
        return DbDialect.parse(this.unwrapConnection());

      } catch (SQLException ex) {
        throw new PersistenceException(ex.getMessage(), ex);
      }
    });
  }

  private static final ConcurrentMap<String, DbDialect> DB_DIALECTS = new ConcurrentHashMap<>();
}
