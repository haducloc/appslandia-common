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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JpaTestUtils {

  public static Map<String, String> getTestDbPuProps() {
    Map<String, String> props = new HashMap<>();

    final String url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";

    props.put("jakarta.persistence.jdbc.driver", "org.h2.Driver");
    props.put("jakarta.persistence.jdbc.url", url);
    props.put("jakarta.persistence.jdbc.user", "sa");
    props.put("jakarta.persistence.jdbc.password", "");

    props.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
    props.put("hibernate.hbm2ddl.auto", "create-drop");
    props.put("hibernate.show_sql", "true");
    props.put("hibernate.format_sql", "true");

    // To help determine the database type from the given URL
    props.put("jdbc.url", url);

    return props;
  }

//  public void testPU() {
//    PersistenceUnitInfoImpl pui = new PersistenceUnitInfoImpl();
//
//    pui.setPersistenceUnitName("testPU");
//    pui.setPersistenceProviderClassName(HibernatePersistenceProvider.class.getName());
//    pui.addManagedClassName(User.class.getName());
//    pui.setExcludeUnlistedClasses(true);
//    pui.setTransactionType(PersistenceUnitTransactionType.RESOURCE_LOCAL);
//
//    var testDbPuProps = JpaTestUtils.getTestDbPuProps();
//    try (var emf = new HibernatePersistenceProvider().createContainerEntityManagerFactory(pui, testDbPuProps)) {
//      try (var em = emf.createEntityManager()) {
//      }
//    }
//  }
}
