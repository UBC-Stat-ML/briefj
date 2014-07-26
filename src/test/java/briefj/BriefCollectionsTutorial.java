package briefj;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import briefj.collections.Counter;
import briefj.collections.UnorderedPair;

import com.beust.jcommander.internal.Sets;
import com.google.common.collect.Maps;


import tutorialj.Tutorial;

import static briefj.BriefMaps.*;



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
  public void getOrPut()
  {
    Map<String,Set<String>> example = Maps.newHashMap();
    
    getOrPutSet(example, "colors").add("blue");
    getOrPutSet(example, "colors").add("red");
    getOrPutSet(example, "foods").add("apple");
    
    Assert.assertEquals(example.get("colors"), new HashSet<String>(Arrays.asList("blue", "red")));
  }
  
  /**
   * Pick an arbitrary elt from a collection
   */
  @Tutorial
  @Test
  public void pick()
  {
    Set<String> items = Sets.newLinkedHashSet();
    items.add("item");
    Assert.assertEquals(BriefCollections.pick(items), "item");
  }
  
  /**
   * Some convenience methods for hashes from doubles (Counter):
   */
  @Tutorial
  @Test
  public void counters()
  {
    Counter<String> counter = new Counter<String>();
    counter.incrementCount("a", 1.1);
    counter.incrementCount("a", 0.6);
    counter.incrementCount("b", -5);
    Assert.assertEquals(counter.getCount("a"), 1.1 + 0.6, 0.0);
    counter.setCount("a", -100);
    Assert.assertEquals(counter.getCount("a"), -100, 0.0);
    
    // iterate in order of insertion:
    for (String item : counter.keySet())
      System.out.println(item + "\t" + counter.getCount(item));
    Assert.assertEquals(counter.keySet().iterator().next(), "a");
    
    // iterate in decreasing order of counts
    for (String item : counter)
      System.out.println(item + "\t" + counter.getCount(item));
    Assert.assertEquals(counter.iterator().next(), "b");
    
    // get sum (normalization)
    Assert.assertEquals(counter.totalCount(), -5.0 - 100.0, 0.0);
    
    // destructively normalize
    counter.normalize();
    Assert.assertEquals(counter.getCount("b"), 5.0/105.0, 1e-10);

    // deep copy
    Counter<String> c2 = new Counter<String>(counter);
    
    // add all counter
    c2.incrementAll(counter);
    Assert.assertEquals(c2.getCount("b"), 2*5.0/105.0, 1e-10);
    Assert.assertEquals(counter.getCount("b"), 5.0/105.0, 1e-10);
  }
  
  /**
   * Unordered pairs:
   */
  @Tutorial
  @Test
  public void unorderedPairs()
  {
    UnorderedPair<Integer, Integer> 
      example = UnorderedPair.of(1, 2),
      example2= UnorderedPair.of(2, 1);
    
    Assert.assertEquals(example, example2);
  }
  
  /**
   * Indexer are convenient when you want to have an array indexed by 
   * some arbitrary type of objects. E.g. for efficient array-based
   * categorical sampling. 
   * 
   * An indexer is just a bijection between integers 0, 1, .., N and
   * a set of objects with .equals() and .hashCode() implemented.
   */
  @Tutorial
  @Test
  public void indexer()
  {
    Indexer<String> indexer = new Indexer<String>();
    indexer.addToIndex("first");
    indexer.addToIndex("second");
    indexer.addToIndex("third");
    
    // i2o maps from index to object
    // o2i maps from object to index
    Assert.assertEquals("first", indexer.i2o(indexer.o2i("first")));
  }
}
