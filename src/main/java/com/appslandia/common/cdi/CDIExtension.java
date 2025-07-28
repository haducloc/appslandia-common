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

package com.appslandia.common.cdi;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import com.appslandia.common.base.DeployEnv;
import com.appslandia.common.base.EnableEnv;
import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.CollectionUtils;
import com.appslandia.common.utils.ObjectUtils;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;

/**
 *
 * @author Loc Ha
 *
 */
public class CDIExtension extends InitializeObject implements Extension {

  final Set<Class<?>> excludedClasses = new HashSet<>();
  final Set<String> excludedPackages = new HashSet<>();
  final Set<Class<? extends Annotation>> excludedAnnotations = new HashSet<>();

  @Override
  protected void init() throws Exception {
  }

  protected CDIExtension excludeClasses(Class<?>... beanClasses) {
    CollectionUtils.toSet(this.excludedClasses, beanClasses);
    return this;
  }

  protected CDIExtension excludePackages(String... packages) {
    CollectionUtils.toSet(this.excludedPackages, packages);
    return this;
  }

  protected CDIExtension excludePackages(Class<?>... beanClasses) {
    for (Class<?> clazz : beanClasses) {
      this.excludedPackages.add(clazz.getPackage().getName());
    }
    return this;
  }

  protected CDIExtension excludeAnnotations(Class<?>... annotationClasses) {
    for (Class<?> clazz : annotationClasses) {
      this.excludedAnnotations.add(ObjectUtils.cast(clazz));
    }
    return this;
  }

  protected boolean willExcludeClass(Class<?> beanClass) {
    return false;
  }

  protected void onExcludeClass(Class<?> beanClass) {
  }

  protected void onRegisterClass(Class<?> beanClass) {
  }

  public <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> event) {
    this.initialize();
    Class<?> beanClass = event.getAnnotatedType().getJavaClass();

    if (willExcludeClasses(this.excludedClasses, beanClass) || willExcludePackages(this.excludedPackages, beanClass)
        || willExcludeAnnotations(this.excludedAnnotations, beanClass)) {
      event.veto();
      onExcludeClass(beanClass);
      return;
    }

    // @EnableEnv
    var enableEnv = beanClass.getDeclaredAnnotation(EnableEnv.class);
    // Another Chance
    if (((enableEnv != null) && !DeployEnv.getCurrent().isAny(enableEnv.value())) || willExcludeClass(beanClass)) {
      event.veto();
      onExcludeClass(beanClass);
      return;
    }
    onRegisterClass(beanClass);
  }

  public static boolean willExcludeClasses(Set<Class<?>> excludedClasses, Class<?> beanClass) {
    if (excludedClasses.isEmpty()) {
      return false;
    }
    return excludedClasses.contains(beanClass);
  }

  public static boolean willExcludePackages(Set<String> excludedPackages, Class<?> beanClass) {
    if (excludedPackages.isEmpty()) {
      return false;
    }
    return excludedPackages.contains(beanClass.getPackage().getName());
  }

  public static boolean willExcludeAnnotations(Set<Class<? extends Annotation>> excludedAnnotations,
      Class<?> beanClass) {
    if (excludedAnnotations.isEmpty()) {
      return false;
    }
    return excludedAnnotations.stream()
        .anyMatch(annotationClass -> beanClass.getDeclaredAnnotation(annotationClass) != null);
  }
}
