// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jpa;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import jakarta.persistence.Cache;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceConfiguration;
import jakarta.persistence.PersistenceUnitTransactionType;
import jakarta.persistence.PersistenceUnitUtil;
import jakarta.persistence.Query;
import jakarta.persistence.SchemaManager;
import jakarta.persistence.SynchronizationType;
import jakarta.persistence.TypedQueryReference;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.metamodel.Metamodel;

/**
 *
 * @author Loc Ha
 *
 */
public class EntityManagerFactoryImpl implements EntityManagerFactory {

  protected final EntityManagerFactory emf;

  public EntityManagerFactoryImpl(EntityManagerFactory emf) {
    this.emf = emf;
  }

  @Override
  public EntityManagerImpl createEntityManager() {
    return new EntityManagerImpl(emf.createEntityManager());
  }

  @SuppressWarnings("rawtypes")
  @Override
  public EntityManagerImpl createEntityManager(Map map) {
    return new EntityManagerImpl(emf.createEntityManager(map));
  }

  @Override
  public EntityManagerImpl createEntityManager(SynchronizationType synchronizationType) {
    return new EntityManagerImpl(emf.createEntityManager(synchronizationType));
  }

  @SuppressWarnings("rawtypes")
  @Override
  public EntityManagerImpl createEntityManager(SynchronizationType synchronizationType, Map map) {
    return new EntityManagerImpl(emf.createEntityManager(synchronizationType, map));
  }

  @Override
  public CriteriaBuilder getCriteriaBuilder() {
    return emf.getCriteriaBuilder();
  }

  @Override
  public Metamodel getMetamodel() {
    return emf.getMetamodel();
  }

  @Override
  public boolean isOpen() {
    return emf.isOpen();
  }

  @Override
  public void close() {
    emf.close();
  }

  @Override
  public Map<String, Object> getProperties() {
    return emf.getProperties();
  }

  @Override
  public Cache getCache() {
    return emf.getCache();
  }

  @Override
  public PersistenceUnitUtil getPersistenceUnitUtil() {
    return emf.getPersistenceUnitUtil();
  }

  @Override
  public void addNamedQuery(String name, Query query) {
    emf.addNamedQuery(name, query);
  }

  @Override
  public <T> void addNamedEntityGraph(String graphName, EntityGraph<T> entityGraph) {
    emf.addNamedEntityGraph(graphName, entityGraph);
  }

  @Override
  public <T> T unwrap(Class<T> cls) {
    return emf.unwrap(cls);
  }

  /* JPA 3.2 */

  @Override
  public String getName() {
    return emf.getName();
  }

  @Override
  public PersistenceUnitTransactionType getTransactionType() {
    return emf.getTransactionType();
  }

  @Override
  public SchemaManager getSchemaManager() {
    return emf.getSchemaManager();
  }

  @Override
  public <R> Map<String, TypedQueryReference<R>> getNamedQueries(Class<R> resultType) {
    return emf.getNamedQueries(resultType);
  }

  @Override
  public <E> Map<String, EntityGraph<? extends E>> getNamedEntityGraphs(Class<E> entityType) {
    return emf.getNamedEntityGraphs(entityType);
  }

  @Override
  public void runInTransaction(Consumer<EntityManager> work) {
    emf.runInTransaction(work);
  }

  @Override
  public <R> R callInTransaction(Function<EntityManager, R> work) {
    return emf.callInTransaction(work);
  }

  // Create and return an EntityManagerFactory for the named persistence unit, using the given properties.

  public static EntityManagerFactoryImpl create(PersistenceConfiguration conf) {
    return new EntityManagerFactoryImpl(Persistence.createEntityManagerFactory(conf));
  }
}
