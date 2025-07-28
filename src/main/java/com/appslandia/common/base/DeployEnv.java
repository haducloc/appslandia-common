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

package com.appslandia.common.base;

import java.util.Arrays;
import java.util.regex.Pattern;

import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.STR;
import com.appslandia.common.utils.SYS;

/**
 *
 * @author Loc Ha
 *
 */
public class DeployEnv {

  public static final DeployEnv DEVELOPMENT = new DeployEnv("Development");
  public static final DeployEnv TESTING = new DeployEnv("Testing");
  public static final DeployEnv STAGING = new DeployEnv("Staging");
  public static final DeployEnv PRODUCTION = new DeployEnv("Production");

  final String name;

  private DeployEnv(String name) {
    this.name = Arguments.notNull(name);
  }

  public boolean isDevelopment() {
    return isEnv(DEVELOPMENT.name);
  }

  public boolean isTesting() {
    return isEnv(TESTING.name);
  }

  public boolean isStaging() {
    return isEnv(STAGING.name);
  }

  public boolean isProduction() {
    return isEnv(PRODUCTION.name);
  }

  public boolean isAny(String... environments) {
    return Arrays.stream(environments).anyMatch(env -> this.name.equalsIgnoreCase(env));
  }

  public boolean isEnv(String environment) {
    return this.name.equalsIgnoreCase(environment);
  }

  public String getName() {
    return this.name;
  }

  @Override
  public String toString() {
    return "DeployEnv: " + this.name;
  }

  @Override
  public int hashCode() {
    return this.name.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof DeployEnv that)) {
      return false;
    }
    return this.name.equalsIgnoreCase(that.name);
  }

  private static volatile DeployEnv __current;
  private static final Object MUTEX = new Object();

  public static DeployEnv getCurrent() {
    var obj = __current;
    if (obj == null) {
      synchronized (MUTEX) {
        if ((obj = __current) == null) {
          __current = obj = initCurrent();
        }
      }
    }
    return obj;
  }

  public static void setCurrent(DeployEnv env) {
    Asserts.isNull(env, "DeployEnv.__current must be null.");

    if (__current == null) {
      synchronized (MUTEX) {
        if (__current == null) {
          __current = env;
          return;
        }
      }
    }
  }

  public static void setCurrent(String env) {
    Arguments.notNull(env);
    setCurrent(toDeployEnv(env));
  }

  @SuppressWarnings("el-syntax")
  private static DeployEnv initCurrent() {
    var env = SYS.resolve("{deploy_env,env.DEPLOY_ENV}");
    if (env == null) {
      return DEVELOPMENT;
    }
    return toDeployEnv(env);
  }

  private static final Pattern ENV_NAME_PATTERN = Pattern.compile("^[a-z][a-z\\d_]*", Pattern.CASE_INSENSITIVE);

  private static DeployEnv toDeployEnv(String env) {
    if (DEVELOPMENT.name.equalsIgnoreCase(env)) {
      return DEVELOPMENT;
    }
    if (TESTING.name.equalsIgnoreCase(env)) {
      return TESTING;
    }
    if (STAGING.name.equalsIgnoreCase(env)) {
      return STAGING;
    }
    if (PRODUCTION.name.equalsIgnoreCase(env)) {
      return PRODUCTION;
    }

    if (!ENV_NAME_PATTERN.matcher(env).matches()) {
      throw new IllegalArgumentException(STR.fmt("The env '{}' is invalid.", env));
    }
    return new DeployEnv(env);
  }
}
