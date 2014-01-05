package briefj;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import tutorialj.Tutorial;

import static briefj.BriefStrings.*;


public class BriefStringsTutorial
{
  /**
   * BriefStrings
   * ------------
   * 
   * To quickly select a group from a regular expression, 
   * use 
   */
  @Tutorial
  @Test
  public void testMatches()
  {
    String match = firstGroupFromFirstMatch("I need ([0-9]*)", "I need 58 bitcoins");
    Assert.assertEquals(match, "58");
    
    List<String> matches = allGroupsFromFirstMatch("I need ([0-9]*)\\s+(.*)", "I need 58 bitcoins");
    Assert.assertEquals(matches, Arrays.asList("58", "bitcoins"));
  }
}
