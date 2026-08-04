// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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
    return Arrays.stream(environments).anyMatch(env -> name.equalsIgnoreCase(env));
  }

  public boolean isEnv(String environment) {
    return name.equalsIgnoreCase(environment);
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "DeployEnv: " + name;
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof DeployEnv that)) {
      return false;
    }
    return name.equalsIgnoreCase(that.name);
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
    var env = SYS.resolve("${deploy_env,env.DEPLOY_ENV}");
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
