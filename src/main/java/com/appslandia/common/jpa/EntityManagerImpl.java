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
import java.util.List;
import java.util.Map;

import com.appslandia.common.base.Params;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.ObjectUtils;

import jakarta.persistence.Cache;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
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

  private EntityManager em;

  public EntityManagerImpl() {
  }

  public EntityManagerImpl(EntityManager em) {
    Asserts.isTrue(!(em instanceof EntityManagerImpl));
    this.em = em;
  }

  protected EntityManager em() {
    return this.em;
  }

  public Connection getConnection() {
    return (Connection) em().unwrap(Connection.class);
  }

  public void insert(Object entity) {
    em().persist(entity);
    em().flush();
  }

  public void removeByPk(Class<?> type, Object primaryKey) throws EntityNotFoundException {
    Object ref = em().getReference(type, primaryKey);
    em().remove(ref);
  }

  public boolean isInCache(Class<?> type, Object primaryKey) {
    Cache cache = em().getEntityManagerFactory().getCache();
    return (cache != null) && cache.contains(type, primaryKey);
  }

  public void evictCache() {
    Cache cache = em().getEntityManagerFactory().getCache();
    if (cache != null) {
      cache.evictAll();
    }
  }

  public void evictCache(Class<?> type) {
    Cache cache = em().getEntityManagerFactory().getCache();
    if (cache != null) {
      cache.evict(type);
    }
  }

  public void evictCache(Class<?> type, Object primaryKey) {
    Cache cache = em().getEntityManagerFactory().getCache();
    if (cache != null) {
      cache.evict(type, primaryKey);
    }
  }

  public <T> T findFetch(Class<T> entityClass, Object primaryKey, String graphName) {
    return em().find(entityClass, primaryKey,
        new Params().set(JpaHints.HINT_JPA_FETCH_GRAPH, em().createEntityGraph(graphName)));
  }

  public <T> T findLoad(Class<T> entityClass, Object primaryKey, String graphName) {
    return em().find(entityClass, primaryKey,
        new Params().set(JpaHints.HINT_JPA_LOAD_GRAPH, em().createEntityGraph(graphName)));
  }

  public <T> TypedQueryImpl<T> createQueryFetch(String qlString, Class<T> resultClass, String graphName) {
    return new TypedQueryImpl<T>(em().createQuery(qlString, resultClass).setHint(JpaHints.HINT_JPA_FETCH_GRAPH,
        em().createEntityGraph(graphName)));
  }

  public <T> TypedQueryImpl<T> createQueryLoad(String qlString, Class<T> resultClass, String graphName) {
    return new TypedQueryImpl<T>(em().createQuery(qlString, resultClass).setHint(JpaHints.HINT_JPA_LOAD_GRAPH,
        em().createEntityGraph(graphName)));
  }

  public <T> TypedQueryImpl<T> createNamedQueryFetch(String name, Class<T> resultClass, String graphName) {
    return new TypedQueryImpl<T>(em().createNamedQuery(name, resultClass).setHint(JpaHints.HINT_JPA_FETCH_GRAPH,
        em().createEntityGraph(graphName)));
  }

  public <T> TypedQueryImpl<T> createNamedQueryLoad(String name, Class<T> resultClass, String graphName) {
    return new TypedQueryImpl<T>(em().createNamedQuery(name, resultClass).setHint(JpaHints.HINT_JPA_LOAD_GRAPH,
        em().createEntityGraph(graphName)));
  }

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

  @SuppressWarnings("rawtypes")
  @Override
  public QueryImpl createNativeQuery(String sqlString, Class resultClass) {
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
    return new TypedQueryImpl<T>(em().createQuery(criteriaQuery));
  }

  @Override
  public <T> TypedQueryImpl<T> createQuery(String qlString, Class<T> resultClass) {
    return new TypedQueryImpl<T>(em().createQuery(qlString, resultClass));
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
    return new TypedQueryImpl<T>(em().createNamedQuery(name, resultClass));
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

  @Override
  public String toString() {
    return ObjectUtils.toStringWrapper(this, this.em);
  }
}
