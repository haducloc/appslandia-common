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

import java.util.Map;

import com.appslandia.common.utils.ObjectUtils;

import jakarta.persistence.Cache;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnitUtil;
import jakarta.persistence.Query;
import jakarta.persistence.SynchronizationType;
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
    return new EntityManagerImpl(this.emf.createEntityManager());
  }

  @SuppressWarnings("rawtypes")
  @Override
  public EntityManagerImpl createEntityManager(Map map) {
    return new EntityManagerImpl(this.emf.createEntityManager(map));
  }

  @Override
  public EntityManagerImpl createEntityManager(SynchronizationType synchronizationType) {
    return new EntityManagerImpl(this.emf.createEntityManager(synchronizationType));
  }

  @SuppressWarnings("rawtypes")
  @Override
  public EntityManagerImpl createEntityManager(SynchronizationType synchronizationType, Map map) {
    return new EntityManagerImpl(this.emf.createEntityManager(synchronizationType, map));
  }

  @Override
  public CriteriaBuilder getCriteriaBuilder() {
    return this.emf.getCriteriaBuilder();
  }

  @Override
  public Metamodel getMetamodel() {
    return this.emf.getMetamodel();
  }

  @Override
  public boolean isOpen() {
    return this.emf.isOpen();
  }

  @Override
  public void close() {
    this.emf.close();
  }

  @Override
  public Map<String, Object> getProperties() {
    return this.emf.getProperties();
  }

  @Override
  public Cache getCache() {
    return this.emf.getCache();
  }

  @Override
  public PersistenceUnitUtil getPersistenceUnitUtil() {
    return this.emf.getPersistenceUnitUtil();
  }

  @Override
  public void addNamedQuery(String name, Query query) {
    this.emf.addNamedQuery(name, query);
  }

  @Override
  public <T> void addNamedEntityGraph(String graphName, EntityGraph<T> entityGraph) {
    this.emf.addNamedEntityGraph(graphName, entityGraph);
  }

  @Override
  public <T> T unwrap(Class<T> cls) {
    return this.emf.unwrap(cls);
  }

  @Override
  public String toString() {
    return ObjectUtils.toStringWrapper(this, this.emf);
  }
}
