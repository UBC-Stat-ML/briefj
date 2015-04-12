package briefj;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.google.common.collect.ContiguousSet;



public class BriefParallel
{
  /**
   * Call the provided operation's accept(int i) function for all integers i from [0, numberOfTasks)
   * using numberOfThreads parallel threads. 
   * 
   * Returns once all the tasks are done.
   * 
   * If one of the threads encounters an exception, the main thread will throw this exception as 
   * well.
   * 
   * @param numberOfTasks
   * @param numberOfThreads
   * @param operation
   */
  public static void process(int numberOfTasks, final int numberOfThreads, final Consumer<Integer> operation) 
  {
    if (numberOfThreads < 1)
      throw new RuntimeException("Number of threads cannot be negative");
    
    ContiguousSet<Integer> taskIndices = BriefLists.integers(numberOfTasks);
    
    if (numberOfThreads == 1)
    {
      for (final int taskIndex : taskIndices)
        operation.accept(taskIndex);
      return;
    }
    
    final ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
    // Mutable reference used to inform the main thread of a potential exception
    final Throwable [] exception = new Throwable[1];

    for (final int taskIndex : taskIndices)
      executor.execute(() -> 
      {
        if(exception[0] == null) 
          try 
          { 
            operation.accept(taskIndex); 
          }
          catch(Throwable t) 
          { 
            synchronized (exception)
            {
              if (exception[0] != null)
                exception[0] = t; 
            }
          }
      });
    executor.shutdown();
    try 
    {
      while(!executor.awaitTermination(1, TimeUnit.SECONDS));
    } 
    catch(InterruptedException e) 
    {
      throw new RuntimeException(e);
    }
    if(exception[0] != null) 
      throw new RuntimeException(exception[0]);
  }

  private BriefParallel() {}
}
