package briefj;

import com.beust.jcommander.JCommander;



public class CommandLineUtils
{
  /* TODO: need to modify JCommander:
   * 
   * - remove the name argument, redundant
   * - default values as textual argument, output them to property file
   * - create descent usage
   * - create briefer sub-command
   * 
   * Idea: experiments launched via vagrant style configs?
   * just detect when there is a new run file?
   * integrate workflow language with decent property file format?
   */
  
  public static void start(Runnable runnable, String [] args)
  {
    if (args.length > 0  && args[0].matches("[-][-]?help"))
    {
      new JCommander(runnable).usage();
      System.exit(0);
    }
    new JCommander(runnable, args);
    runnable.run();
  }
}
