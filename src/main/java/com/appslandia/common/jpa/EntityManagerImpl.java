// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jpa;

import jakarta.persistence.EntityManager;

/**
 *
 * @author Loc Ha
 *
 */
public class EntityManagerImpl extends JpaEntityManager {

  final EntityManager em;

  public EntityManagerImpl(EntityManager em) {
    this.em = em;
  }

  @Override
  protected EntityManager em() {
    return em;
  }
}
