// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.cdi;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Qualifier;

/**
 *
 * @author Loc Ha
 *
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Documented
public @interface Json {

  Profile value();

  public enum Profile {
    COMPACT, PRETTY
  }

  public static final Json COMPACT = new JsonLiteral(Profile.COMPACT);

  public static final Json PRETTY = new JsonLiteral(Profile.PRETTY);

  @SuppressWarnings("all")
  public static class JsonLiteral extends AnnotationLiteral<Json> implements Json {
    private static final long serialVersionUID = 1L;

    private Profile profile;

    private JsonLiteral(Profile profile) {
      this.profile = profile;
    }

    @Override
    public Profile value() {
      return profile;
    }
  }
}
