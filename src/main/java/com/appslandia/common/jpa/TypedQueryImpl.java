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

import com.appslandia.common.jdbc.DbDialect;
import com.appslandia.common.jdbc.JdbcUtils;
import com.appslandia.common.jdbc.LikeType;
import com.appslandia.common.jdbc.SqlQuery;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.ObjectUtils;

import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;
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
  final HintMapper hintMapper;

  public TypedQueryImpl(TypedQuery<X> query, HintMapper hintMapper) {
    this(query, hintMapper, null, null);
  }

  public TypedQueryImpl(TypedQuery<X> query, HintMapper hintMapper, JpaQuery pQuery, DbDialect dbDialect) {
    this.query = query;
    this.hintMapper = hintMapper;
    this.pQuery = pQuery;
    this.dbDialect = dbDialect;
  }

  protected JpaQuery getPQuery() {
    return Asserts.notNull(this.pQuery,
        "No pQuery found. Please ensure that JpaQuery was used to create this QueryImpl instance.");
  }

  protected DbDialect getDbDialect() {
    return Asserts.notNull(this.dbDialect,
        "No dbDialect found. Please ensure that JpaQuery was used to create this QueryImpl instance.");
  }

  public List<X> executeList() {
    return this.query.getResultList();
  }

  public X executeSingle() {
    try {
      return this.query.getSingleResult();
    } catch (NoResultException ex) {
      return null;
    }
  }

  public X firstOrNull() {
    try {
      return this.query.setMaxResults(1).getSingleResult();
    } catch (NoResultException ex) {
      return null;
    }
  }

  public TypedQueryImpl<X> asReadonly() {
    var hintName = this.hintMapper.getHint(JpaUtils.PU_PROP_HINT_QUERY_READONLY);
    if (hintName != null) {
      this.query.setHint(hintName, true);
    }
    return this;
  }

  // Set LIKE Parameters
  // :name IS NULL OR name LIKE :name
  // :name = '' OR name LIKE :name

  public TypedQueryImpl<X> setLike(String parameterName, String value) {
    this.query.setParameter(parameterName, JdbcUtils.toLikeParamValue(value, LikeType.CONTAINS, getDbDialect()));
    return this;
  }

  public TypedQueryImpl<X> setLikeSW(String parameterName, String value) {
    this.query.setParameter(parameterName, JdbcUtils.toLikeParamValue(value, LikeType.STARTS_WITH, getDbDialect()));
    return this;
  }

  public TypedQueryImpl<X> setLikeEW(String parameterName, String value) {
    this.query.setParameter(parameterName, JdbcUtils.toLikeParamValue(value, LikeType.ENDS_WITH, getDbDialect()));
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

  public TypedQueryImpl<X> setDateArray(String parameterName, TemporalType temporalType, java.util.Date... values) {
    var arrayLen = this.getPQuery().getArrayLen(parameterName);
    Arguments.isTrue(values.length <= arrayLen);

    for (var i = 0; i < arrayLen; i++) {
      setParameter(SqlQuery.toParamName(parameterName, i), (i < values.length) ? values[i] : null, temporalType);
    }
    return this;
  }

  // Query & TypedQuery

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

  @Override
  public java.util.List<X> getResultList() {
    return this.query.getResultList();
  }

  @Override
  public java.util.stream.Stream<X> getResultStream() {
    return this.query.getResultStream();
  }

  @Override
  public X getSingleResult() {
    return this.query.getSingleResult();
  }

  @Override
  public TypedQueryImpl<X> setHint(String hintName, Object value) {
    this.query.setHint(hintName, value);
    return this;
  }

  @Override
  public TypedQueryImpl<X> setFirstResult(int startPosition) {
    this.query.setFirstResult(startPosition);
    return this;
  }

  @Override
  public TypedQueryImpl<X> setFlushMode(FlushModeType flushMode) {
    this.query.setFlushMode(flushMode);
    return this;
  }

  @Override
  public TypedQueryImpl<X> setLockMode(LockModeType lockMode) {
    this.query.setLockMode(lockMode);
    return this;
  }

  @Override
  public TypedQueryImpl<X> setMaxResults(int maxResult) {
    this.query.setMaxResults(maxResult);
    return this;
  }

  @Override
  public TypedQueryImpl<X> setParameter(int position, Object value) {
    this.query.setParameter(position, value);
    return this;
  }

  @Override
  public TypedQueryImpl<X> setParameter(String name, Object value) {
    this.query.setParameter(name, value);
    return this;
  }

  @Override
  public <T> TypedQueryImpl<X> setParameter(Parameter<T> param, T value) {
    this.query.setParameter(param, value);
    return this;
  }

  @Override
  public TypedQueryImpl<X> setParameter(int position, java.util.Calendar value, TemporalType temporalType) {
    this.query.setParameter(position, value, temporalType);
    return this;
  }

  @Override
  public TypedQueryImpl<X> setParameter(int position, java.util.Date value, TemporalType temporalType) {
    this.query.setParameter(position, value, temporalType);
    return this;
  }

  @Override
  public TypedQueryImpl<X> setParameter(String name, java.util.Calendar value, TemporalType temporalType) {
    this.query.setParameter(name, value, temporalType);
    return this;
  }

  @Override
  public TypedQueryImpl<X> setParameter(String name, java.util.Date value, TemporalType temporalType) {
    this.query.setParameter(name, value, temporalType);
    return this;
  }

  @Override
  public TypedQueryImpl<X> setParameter(Parameter<java.util.Calendar> param, java.util.Calendar value,
      TemporalType temporalType) {
    this.query.setParameter(param, value, temporalType);
    return this;
  }

  @Override
  public TypedQueryImpl<X> setParameter(Parameter<java.util.Date> param, java.util.Date value,
      TemporalType temporalType) {
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
