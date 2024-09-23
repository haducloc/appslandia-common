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

import java.util.List;

import com.appslandia.common.jdbc.DbDialect;
import com.appslandia.common.jdbc.LikeType;
import com.appslandia.common.jdbc.SqlQuery;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.ObjectUtils;

import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Parameter;
import jakarta.persistence.Query;
import jakarta.persistence.TemporalType;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class QueryImpl implements Query {

  final Query query;
  final JpaQuery pQuery;
  final DbDialect dbDialect;

  public QueryImpl(Query query, DbDialect dbDialect) {
    this(query, null, dbDialect);
  }

  public QueryImpl(Query query, JpaQuery pQuery, DbDialect dbDialect) {
    this.query = query;
    this.pQuery = pQuery;
    this.dbDialect = dbDialect;
  }

  protected JpaQuery getPQuery() {
    return Asserts.notNull(this.pQuery, "No pQuery is associated with the query.");
  }

  public <T> List<T> executeList() {
    return ObjectUtils.cast(this.query.getResultList());
  }

  public <T> T executeSingle() {
    try {
      return ObjectUtils.cast(this.query.getSingleResult());
    } catch (NoResultException ex) {
      return null;
    }
  }

  public <T> T firstOrNull() {
    try {
      return ObjectUtils.cast(this.query.setMaxResults(1).getSingleResult());
    } catch (NoResultException ex) {
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  public <S> S executeScalar() {
    Object[] values = executeSingle();
    return (values != null) ? (S) values[0] : null;
  }

  public long getLongScalar() {
    Number n = Asserts.notNull(executeScalar());
    return n.longValue();
  }

  // Set LIKE Parameters
  // :name IS NULL OR name LIKE :name
  // :name = '' OR name LIKE :name

  public QueryImpl setLike(String parameterName, String value) {
    this.query.setParameter(parameterName, this.dbDialect.toLikePattern(value, LikeType.CONTAINS));
    return this;
  }

  public QueryImpl setLikeSW(String parameterName, String value) {
    this.query.setParameter(parameterName, this.dbDialect.toLikePattern(value, LikeType.STARTS_WITH));
    return this;
  }

  public QueryImpl setLikeEW(String parameterName, String value) {
    this.query.setParameter(parameterName, this.dbDialect.toLikePattern(value, LikeType.ENDS_WITH));
    return this;
  }

  // Set LIKE_ANY Parameters
  // name LIKE_ANY :names

  public QueryImpl setLikeAny(String parameterName, String[] values) {
    return setLikeAny(parameterName, values, LikeType.CONTAINS, null);
  }

  public QueryImpl setLikeAnySW(String parameterName, String[] values) {
    return setLikeAny(parameterName, values, LikeType.STARTS_WITH, null);
  }

  public QueryImpl setLikeAnyEW(String parameterName, String[] values) {
    return setLikeAny(parameterName, values, LikeType.ENDS_WITH, null);
  }

  public QueryImpl setLikeAny(String parameterName, String[] values, LikeType likeType, String falsePattern) {
    int arrayLen = this.getPQuery().getArrayLen(parameterName);
    Asserts.isTrue(values.length <= arrayLen);

    for (int i = 0; i < arrayLen; i++) {
      setParameter(SqlQuery.toParamName(parameterName, i),
          (i < values.length) ? this.dbDialect.toLikePattern(values[i], likeType) : falsePattern);
    }
    return this;
  }

  // Set IN Parameters
  // type IN :types

  public QueryImpl setObjectArray(String parameterName, Object[] values) {
    int arrayLen = this.getPQuery().getArrayLen(parameterName);
    Asserts.isTrue(values.length <= arrayLen);

    for (int i = 0; i < arrayLen; i++) {
      setParameter(SqlQuery.toParamName(parameterName, i), (i < values.length) ? values[i] : null);
    }
    return this;
  }

  public QueryImpl setDateArray(String parameterName, java.util.Date[] values, TemporalType temporalType) {
    int arrayLen = this.getPQuery().getArrayLen(parameterName);
    Asserts.isTrue(values.length <= arrayLen);

    for (int i = 0; i < arrayLen; i++) {
      setParameter(SqlQuery.toParamName(parameterName, i), (i < values.length) ? values[i] : null, temporalType);
    }
    return this;
  }

  // Query

  @Override
  public java.util.Map<String, Object> getHints() {
    return this.query.getHints();
  }

  @Override
  public int getFirstResult() {
    return this.query.getFirstResult();
  }

  @Override
  public FlushModeType getFlushMode() {
    return this.query.getFlushMode();
  }

  @Override
  public LockModeType getLockMode() {
    return this.query.getLockMode();
  }

  @Override
  public int getMaxResults() {
    return this.query.getMaxResults();
  }

  @Override
  public Parameter<?> getParameter(int position) {
    return this.query.getParameter(position);
  }

  @Override
  public Parameter<?> getParameter(String name) {
    return this.query.getParameter(name);
  }

  @Override
  public <T> Parameter<T> getParameter(int position, Class<T> type) {
    return this.query.getParameter(position, type);
  }

  @Override
  public <T> Parameter<T> getParameter(String name, Class<T> type) {
    return this.query.getParameter(name, type);
  }

  @Override
  public Object getParameterValue(int position) {
    return this.query.getParameterValue(position);
  }

  @Override
  public Object getParameterValue(String name) {
    return this.query.getParameterValue(name);
  }

  @Override
  public <T> T getParameterValue(Parameter<T> param) {
    return this.query.getParameterValue(param);
  }

  @Override
  public java.util.Set<Parameter<?>> getParameters() {
    return this.query.getParameters();
  }

  @SuppressWarnings("rawtypes")
  @Override
  public java.util.List getResultList() {
    return this.query.getResultList();
  }

  @SuppressWarnings("rawtypes")
  @Override
  public java.util.stream.Stream getResultStream() {
    return this.query.getResultStream();
  }

  @Override
  public Object getSingleResult() {
    return this.query.getSingleResult();
  }

  @Override
  public QueryImpl setHint(String hintName, Object value) {
    this.query.setHint(hintName, value);
    return this;
  }

  @Override
  public QueryImpl setFirstResult(int startPosition) {
    this.query.setFirstResult(startPosition);
    return this;
  }

  @Override
  public QueryImpl setFlushMode(FlushModeType flushMode) {
    this.query.setFlushMode(flushMode);
    return this;
  }

  @Override
  public QueryImpl setLockMode(LockModeType lockMode) {
    this.query.setLockMode(lockMode);
    return this;
  }

  @Override
  public QueryImpl setMaxResults(int maxResult) {
    this.query.setMaxResults(maxResult);
    return this;
  }

  @Override
  public QueryImpl setParameter(int position, Object value) {
    this.query.setParameter(position, value);
    return this;
  }

  @Override
  public QueryImpl setParameter(String name, Object value) {
    this.query.setParameter(name, value);
    return this;
  }

  @Override
  public <T> QueryImpl setParameter(Parameter<T> param, T value) {
    this.query.setParameter(param, value);
    return this;
  }

  @Override
  public QueryImpl setParameter(int position, java.util.Calendar value, TemporalType temporalType) {
    this.query.setParameter(position, value, temporalType);
    return this;
  }

  @Override
  public QueryImpl setParameter(int position, java.util.Date value, TemporalType temporalType) {
    this.query.setParameter(position, value, temporalType);
    return this;
  }

  @Override
  public QueryImpl setParameter(String name, java.util.Calendar value, TemporalType temporalType) {
    this.query.setParameter(name, value, temporalType);
    return this;
  }

  @Override
  public QueryImpl setParameter(String name, java.util.Date value, TemporalType temporalType) {
    this.query.setParameter(name, value, temporalType);
    return this;
  }

  @Override
  public QueryImpl setParameter(Parameter<java.util.Calendar> param, java.util.Calendar value,
      TemporalType temporalType) {
    this.query.setParameter(param, value, temporalType);
    return this;
  }

  @Override
  public QueryImpl setParameter(Parameter<java.util.Date> param, java.util.Date value, TemporalType temporalType) {
    this.query.setParameter(param, value, temporalType);
    return this;
  }

  @Override
  public int executeUpdate() {
    return this.query.executeUpdate();
  }

  @Override
  public boolean isBound(Parameter<?> param) {
    return this.query.isBound(param);
  }

  @Override
  public <T> T unwrap(Class<T> cls) {
    return this.query.unwrap(cls);
  }

  @Override
  public String toString() {
    return ObjectUtils.toStringWrapper(this, this.query);
  }
}
