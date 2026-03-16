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

import com.appslandia.common.jdbc.DbDialect;
import com.appslandia.common.jdbc.JdbcUtils;
import com.appslandia.common.jdbc.LikeType;
import com.appslandia.common.jdbc.SqlQuery;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.Asserts;

import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.CacheStoreMode;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Parameter;
import jakarta.persistence.TemporalType;
import jakarta.persistence.TypedQuery;

/**
 *
 * @author Loc Ha
 *
 */
public class TypedQueryImpl<X> implements TypedQuery<X> {

  final TypedQuery<X> query;
  final JpaQuery pQuery;
  final DbDialect dbDialect;
  final JpaProvider jpaProvider;

  public TypedQueryImpl(TypedQuery<X> query, JpaProvider jpaProvider) {
    this(query, null, jpaProvider, null);
  }

  public TypedQueryImpl(TypedQuery<X> query, JpaQuery pQuery, JpaProvider jpaProvider, DbDialect dbDialect) {
    this.query = Arguments.notNull(query, "query is required.");
    this.pQuery = pQuery;
    this.jpaProvider = Arguments.notNull(jpaProvider, "jpaProvider is required.");
    this.dbDialect = dbDialect;
  }

  protected JpaQuery getPQuery() {
    return Asserts.notNull(pQuery, "pQuery is required.");
  }

  protected DbDialect getDbDialect() {
    return Asserts.notNull(dbDialect, "dbDialect is required.");
  }

  public TypedQueryImpl<X> asReadonly() {
    switch (this.jpaProvider) {
    case HIBERNATE -> query.setHint("org.hibernate.readOnly", true);
    case ECLIPSE_LINK -> query.setHint("eclipselink.read-only", Boolean.TRUE.toString());
    default -> {
    }
    }
    return this;
  }

  // Set LIKE Parameters
  // :name IS NULL OR name LIKE :name
  // :name = '' OR name LIKE :name

  public TypedQueryImpl<X> setLike(String parameterName, String value) {
    query.setParameter(parameterName, JdbcUtils.toLikeParamValue(value, LikeType.CONTAINS, getDbDialect()));
    return this;
  }

  public TypedQueryImpl<X> setLikeSW(String parameterName, String value) {
    query.setParameter(parameterName, JdbcUtils.toLikeParamValue(value, LikeType.STARTS_WITH, getDbDialect()));
    return this;
  }

  public TypedQueryImpl<X> setLikeEW(String parameterName, String value) {
    query.setParameter(parameterName, JdbcUtils.toLikeParamValue(value, LikeType.ENDS_WITH, getDbDialect()));
    return this;
  }

  // Set LIKE_ANY Parameters
  // name LIKE_ANY :names

  public TypedQueryImpl<X> setLikeAny(String parameterName, String... values) {
    return setLikeAny(parameterName, values, LikeType.CONTAINS);
  }

  public TypedQueryImpl<X> setLikeAnySW(String parameterName, String... values) {
    return setLikeAny(parameterName, values, LikeType.STARTS_WITH);
  }

  public TypedQueryImpl<X> setLikeAnyEW(String parameterName, String... values) {
    return setLikeAny(parameterName, values, LikeType.ENDS_WITH);
  }

  protected TypedQueryImpl<X> setLikeAny(String parameterName, String[] values, LikeType likeType) {
    var arrayLen = this.getPQuery().getArrayLen(parameterName);
    Arguments.isTrue(values.length <= arrayLen);

    for (var i = 0; i < arrayLen; i++) {
      setParameter(SqlQuery.toParamName(parameterName, i),
          (i < values.length) ? JdbcUtils.toLikeParamValue(values[i], likeType, getDbDialect()) : null);
    }
    return this;
  }

  // Set IN Parameters
  // type IN :types

  public TypedQueryImpl<X> setObjectArray(String parameterName, Object... values) {
    var arrayLen = this.getPQuery().getArrayLen(parameterName);
    Arguments.isTrue(values.length <= arrayLen);

    for (var i = 0; i < arrayLen; i++) {
      setParameter(SqlQuery.toParamName(parameterName, i), (i < values.length) ? values[i] : null);
    }
    return this;
  }

  // Query & TypedQuery

  @Override
  public java.util.Map<String, Object> getHints() {
    return query.getHints();
  }

  @Override
  public int getFirstResult() {
    return query.getFirstResult();
  }

  @Override
  public FlushModeType getFlushMode() {
    return query.getFlushMode();
  }

  @Override
  public LockModeType getLockMode() {
    return query.getLockMode();
  }

  @Override
  public int getMaxResults() {
    return query.getMaxResults();
  }

  @Override
  public Parameter<?> getParameter(int position) {
    return query.getParameter(position);
  }

  @Override
  public Parameter<?> getParameter(String name) {
    return query.getParameter(name);
  }

  @Override
  public <T> Parameter<T> getParameter(int position, Class<T> type) {
    return query.getParameter(position, type);
  }

  @Override
  public <T> Parameter<T> getParameter(String name, Class<T> type) {
    return query.getParameter(name, type);
  }

  @Override
  public Object getParameterValue(int position) {
    return query.getParameterValue(position);
  }

  @Override
  public Object getParameterValue(String name) {
    return query.getParameterValue(name);
  }

  @Override
  public <T> T getParameterValue(Parameter<T> param) {
    return query.getParameterValue(param);
  }

  @Override
  public java.util.Set<Parameter<?>> getParameters() {
    return query.getParameters();
  }

  @Override
  public java.util.List<X> getResultList() {
    return query.getResultList();
  }

  @Override
  public java.util.stream.Stream<X> getResultStream() {
    return query.getResultStream();
  }

  @Override
  public X getSingleResult() {
    return query.getSingleResult();
  }

  @Override
  public TypedQueryImpl<X> setHint(String hintName, Object value) {
    query.setHint(hintName, value);
    return this;
  }

  @Override
  public TypedQueryImpl<X> setFirstResult(int startPosition) {
    query.setFirstResult(startPosition);
    return this;
  }

  @Override
  public TypedQueryImpl<X> setFlushMode(FlushModeType flushMode) {
    query.setFlushMode(flushMode);
    return this;
  }

  @Override
  public TypedQueryImpl<X> setLockMode(LockModeType lockMode) {
    query.setLockMode(lockMode);
    return this;
  }

  @Override
  public TypedQueryImpl<X> setMaxResults(int maxResult) {
    query.setMaxResults(maxResult);
    return this;
  }

  @Override
  public TypedQueryImpl<X> setParameter(int position, Object value) {
    query.setParameter(position, value);
    return this;
  }

  @Override
  public TypedQueryImpl<X> setParameter(String name, Object value) {
    query.setParameter(name, value);
    return this;
  }

  @Override
  public <T> TypedQueryImpl<X> setParameter(Parameter<T> param, T value) {
    query.setParameter(param, value);
    return this;
  }

  @Deprecated
  @Override
  public TypedQueryImpl<X> setParameter(int position, java.util.Calendar value, TemporalType temporalType) {
    query.setParameter(position, value, temporalType);
    return this;
  }

  @Deprecated
  @Override
  public TypedQueryImpl<X> setParameter(int position, java.util.Date value, TemporalType temporalType) {
    query.setParameter(position, value, temporalType);
    return this;
  }

  @Deprecated
  @Override
  public TypedQueryImpl<X> setParameter(String name, java.util.Calendar value, TemporalType temporalType) {
    query.setParameter(name, value, temporalType);
    return this;
  }

  @Deprecated
  @Override
  public TypedQueryImpl<X> setParameter(String name, java.util.Date value, TemporalType temporalType) {
    query.setParameter(name, value, temporalType);
    return this;
  }

  @Deprecated
  @Override
  public TypedQueryImpl<X> setParameter(Parameter<java.util.Calendar> param, java.util.Calendar value,
      TemporalType temporalType) {
    query.setParameter(param, value, temporalType);
    return this;
  }

  @Deprecated
  @Override
  public TypedQueryImpl<X> setParameter(Parameter<java.util.Date> param, java.util.Date value,
      TemporalType temporalType) {
    query.setParameter(param, value, temporalType);
    return this;
  }

  @Override
  public int executeUpdate() {
    return query.executeUpdate();
  }

  @Override
  public boolean isBound(Parameter<?> param) {
    return query.isBound(param);
  }

  @Override
  public <T> T unwrap(Class<T> cls) {
    return query.unwrap(cls);
  }

  /* JPA 3.2 */

  @Override
  public X getSingleResultOrNull() {
    return query.getSingleResultOrNull();
  }

  @Override
  public TypedQueryImpl<X> setCacheRetrieveMode(CacheRetrieveMode cacheRetrieveMode) {
    query.setCacheRetrieveMode(cacheRetrieveMode);
    return this;
  }

  @Override
  public TypedQueryImpl<X> setCacheStoreMode(CacheStoreMode cacheStoreMode) {
    query.setCacheStoreMode(cacheStoreMode);
    return this;
  }

  @Override
  public CacheRetrieveMode getCacheRetrieveMode() {
    return query.getCacheRetrieveMode();
  }

  @Override
  public CacheStoreMode getCacheStoreMode() {
    return query.getCacheStoreMode();
  }

  @Override
  public TypedQueryImpl<X> setTimeout(Integer timeout) {
    query.setTimeout(timeout);
    return this;
  }

  @Override
  public Integer getTimeout() {
    return query.getTimeout();
  }
}
