// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

import com.appslandia.common.base.StringOutput;
import com.appslandia.common.base.UncheckedException;

/**
 *
 * @author Loc Ha
 *
 */
public class ExceptionUtils {

  public static Exception tryUnwrap(InvocationTargetException ex) {
    var te = ex.getTargetException();
    if (te instanceof Exception) {
      return (Exception) te;
    }
    return ex;
  }

  public static String toStackTrace(Throwable ex) {
    var out = new StringOutput();
    var w = new PrintWriter(out);
    ex.printStackTrace(w);
    w.close();
    return out.toString();
  }

  public static String buildMessage(Throwable ex) {
    var sb = new StringBuilder();
    sb.append(ex.getClass().getName());
    sb.append('(');
    sb.append("message=").append(ex.getMessage());
    sb.append("; cause=");
    if (ex.getCause() != null) {
      sb.append(ex.getCause().getClass().getName());
    } else {
      sb.append("null");
    }
    sb.append(')');
    return sb.toString();
  }

  public static RuntimeException toUncheckedException(Throwable ex) {
    if (ex instanceof RuntimeException) {
      return (RuntimeException) ex;
    }
    return new UncheckedException(ex);
  }
}
