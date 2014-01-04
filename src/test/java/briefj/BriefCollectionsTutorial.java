package briefj;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Maps;


import tutorialj.Tutorial;

import static briefj.BriefCollections.*;



public class BriefCollectionsTutorial
{
  /**
   * BriefCollections
   * ----------------
   * 
   * To provide a default initial value in a map, which is also inserted if the key was missing:
   */
  @Tutorial
  @Test
  public void basicExample()
  {
    Map<String,Set<String>> example = Maps.newHashMap();
    
    getOrPutSet(example, "colors").add("blue");
    getOrPutSet(example, "colors").add("red");
    getOrPutSet(example, "foods").add("apple");
    
    Assert.assertEquals(example.get("colors"), new HashSet<String>(Arrays.asList("blue", "red")));
    
  }
}
