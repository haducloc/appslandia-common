// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import jakarta.el.ELProcessor;

/**
 *
 * @author Loc Ha
 *
 */
public class ResettableELProcessor {

  private final ELProcessor processor;

  private final Set<String> variableNames = new HashSet<>();
  private final Set<String> beanNames = new HashSet<>();
  private final Set<FunctionKey> functionNames = new HashSet<>();

  public ResettableELProcessor() {
    this.processor = new ELProcessor();
  }

  public ResettableELProcessor(ELProcessor processor) {
    this.processor = processor;
  }

  // Delegated Methods

  public <T> T eval(String expression) {
    return this.processor.eval(expression);
  }

  public <T> T getValue(String expression, Class<T> expectedType) {
    return this.processor.getValue(expression, expectedType);
  }

  public Object getValue(String expression) {
    return this.processor.getValue(expression, Object.class);
  }

  public void setValue(String expression, Object value) {
    this.processor.setValue(expression, value);
  }

  // Tracking Methods

  public void setVariable(String name, String expression) {
    this.processor.setVariable(name, expression);
    this.variableNames.add(name);
  }

  public void defineBean(String name, Object bean) {
    this.processor.defineBean(name, bean);
    this.beanNames.add(name);
  }

  public void defineFunction(String prefix, String name, Method method) throws NoSuchMethodException {
    this.processor.defineFunction(prefix, name, method);
    this.functionNames.add(new FunctionKey(prefix, name));
  }

  public void defineFunction(String prefix, String name, String className, String method)
      throws ClassNotFoundException, NoSuchMethodException {

    this.processor.defineFunction(prefix, name, className, method);
    this.functionNames.add(new FunctionKey(prefix, name));
  }

  public void defineFunction(String prefix, String name, Class<?> clazz, String method)
      throws ClassNotFoundException, NoSuchMethodException {

    this.processor.defineFunction(prefix, name, clazz.getName(), method);
    this.functionNames.add(new FunctionKey(prefix, name));
  }

  // Reset

  public void reset() {
    // Variables
    for (String name : this.variableNames) {
      this.processor.setVariable(name, null);
    }
    this.variableNames.clear();

    // Beans
    for (String name : this.beanNames) {
      this.processor.defineBean(name, null);
    }
    this.beanNames.clear();

    // Functions
    for (FunctionKey key : this.functionNames) {
      this.processor.getELManager().mapFunction(key.prefix(), key.name(), null);
    }
    this.functionNames.clear();
  }

  // Tracking Info

  public Set<String> getVariableNames() {
    return Set.copyOf(this.variableNames);
  }

  public Set<String> getBeanNames() {
    return Set.copyOf(this.beanNames);
  }

  public Set<FunctionKey> getFunctionNames() {
    return Set.copyOf(this.functionNames);
  }

  // Inner Types

  public record FunctionKey(String prefix, String name) {
  }
}
