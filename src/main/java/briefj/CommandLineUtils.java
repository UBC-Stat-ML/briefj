package briefj;

import com.beust.jcommander.JCommander;



public class CommandLineUtils
{
  public static void start(Runnable runnable, String [] args)
  {
    new JCommander(runnable, args);
    runnable.run();
  }
}
