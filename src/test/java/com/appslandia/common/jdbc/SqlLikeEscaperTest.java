// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.jdbc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Loc Ha
 *
 */
public class SqlLikeEscaperTest {

  @Test
  public void test_toLikeEscape() {
    var escaper = new SqlLikeEscaper('\\', new char[] { '%', '_' });
    var val = escaper.toLikeEscape("20%");
    Assertions.assertEquals("20\\%", val);

    val = escaper.toLikeEscape("_id");
    Assertions.assertEquals("\\_id", val);
  }

  @Test
  public void test_toLikePattern() {
    var escaper = new SqlLikeEscaper('\\', new char[] { '%', '_' });
    var val = escaper.toLikePattern("20%", LikeType.CONTAINS);

    Assertions.assertEquals("%20\\%%", val);

    val = escaper.toLikePattern("_id", LikeType.CONTAINS);
    Assertions.assertEquals("%\\_id%", val);
  }
}
