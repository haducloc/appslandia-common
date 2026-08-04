// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.crypto;

import java.security.Provider;
import java.security.Provider.Service;
import java.security.Security;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import com.appslandia.common.utils.Arguments;

/**
 *
 * @author Loc Ha
 *
 */
public class SecurityProviderApp {

  public static void main(String[] args) {
    // queryServiceTypes();
    querySecurityProviders("Signature", ".*ECDSA.*");
  }

  public static void queryServiceTypes() {
    System.out.println(String.format("***** Java Runtime Version: %s *****", Runtime.version().toString()));
    System.out.println("***** Installed Services *****");
    System.out.println();

    Set<String> serviceTypes = new TreeSet<>();

    Arrays.stream(Security.getProviders()).forEach(p -> {
      p.getServices().stream().forEach(s -> serviceTypes.add(s.getType()));
    });

    serviceTypes.forEach(s -> System.out.println(String.format("* Service: %s", s)));

    System.out.println();
    System.out.println("***** Done *****");
    System.out.println();
  }

  public static void querySecurityProviders(String serviceType, String algorithmPattern) {
    Arguments.notNull(serviceType);

    System.out.println(String.format("***** Java Runtime Version: %s *****", Runtime.version().toString()));
    System.out.println(String.format("***** Installed Security Providers for Service Type %s *****", serviceType));
    System.out.println();

    var seq = 0;
    for (Provider p : Security.getProviders()) {

      System.out.println(String.format("[%02d] Provider: %s, Version: %s", (++seq), p.getName(), p.getVersionStr()));
      System.out.println(p.getInfo());
      System.out.println();

      var services = p.getServices();

      for (Service service : services) {
        if (service.getType().equalsIgnoreCase(serviceType)) {
          if (algorithmPattern == null
              || Pattern.compile(algorithmPattern, Pattern.CASE_INSENSITIVE).matcher(algorithmPattern).matches()) {

            System.out
                .println(String.format("* Service: %s, Algorithm: %s", service.getType(), service.getAlgorithm()));
            System.out.println(service);
          }
        }
      }
    }

    System.out.println("***** Done *****");
    System.out.println();
  }
}
