package briefj;

import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;

import tutorialj.Tutorial;



public class BriefParallelTutorial
{
  /**
   * BriefParallel
   * -------------
   * 
   * Parallelize some tasks indexed by integers, with an explicit control on the 
   * number of threads. 
   */
  @Tutorial
  @Test
  public void test()
  {
    int [] items = new int[100];
    
    // execute the operation "item[i]++" for all integers i in [0, 100),
    // using 8 threads in parallel
    BriefParallel.process(100, 8, i -> items[i]++);
    
    Assert.assertEquals(IntStream.of(items).sum(), 100);
  }
}
