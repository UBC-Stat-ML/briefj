package briefj.run;

import static briefj.BriefIO.*;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.common.hash.HashCode;

import briefj.BriefStrings;
import briefj.opt.OptionsParser;
import briefj.repo.RepositoryUtils;
import briefj.run.RedirectionUtils.Tees;

import static briefj.run.ExecutionInfoFiles.*;


public class Mains
{
  /**
   * Instrument various aspect of this run.
   * 
   * 
   * Note that we check for existence of each file in case this is being 
   * called within a larger context that already created these files.
   * 
   * @param args
   * @param mainClass
   */
  public static void instrumentedRun(String [] args, Runnable mainClass)
  {
    long startTime = System.currentTimeMillis();
    
    // redirect std in / std out
    Tees tees = exists(STD_OUT_FILE) ?
        null :
        RedirectionUtils.createTees(getExecutionInfoFolder());
    
    boolean optionReadSuccessfully = recordPermanentState(args, mainClass);
    recordTransientInfo(args, mainClass, startTime);
    
    if (optionReadSuccessfully)
      try 
      {
        // start job
        mainClass.run();
      }
      catch (Exception e)
      {
        System.err.println(ExceptionUtils.getStackTrace(e));
        write(getFile(EXCEPTION_FILE), ExceptionUtils.getStackTrace(e));
      }
      
    // record end time
    long endTime = System.currentTimeMillis();
    if (!exists(END_TIME_FILE))
      write(getFile(END_TIME_FILE), "" + endTime);
    
    System.out.println("executionMilliseconds : " + (endTime - startTime));
    System.out.println("outputFolder : " + Results.getResultFolder().getAbsolutePath());
    
    if (tees != null)
      tees.close();
  }

  private static void recordTransientInfo(String[] args, Runnable mainClass, long startTime)
  {
    // create start time file
    if (!exists(START_TIME_FILE))
      write(getFile(START_TIME_FILE), "" + startTime);
    
    // record machine info
    if (!exists(HOST_INFO_FILE))
      write(getFile(HOST_INFO_FILE), hostInfo());
  }

  private static CharSequence hostInfo()
  {
    return "Host\t" + SysInfoUtils.getHostName() + "\n" +
           "CPUSpeed\t" + SysInfoUtils.getCPUSpeedStr() + "\n" +
           "MaxMemory\t" + SysInfoUtils.getMaxMemoryStr() + "\n" + 
           "NumCPUs\t" + SysInfoUtils.getNumCPUs();
  }

  /**
   * 
   * @param args
   * @param mainClass
   * @return If the options were read successfully.
   */
  private static boolean recordPermanentState(String[] args, Runnable mainClass)
  {
    // record command line options
    OptionsParser parser;
    try 
    {
      parser = OptionsUtils.parseOptions(mainClass, args);
      if (!exists(OPTIONS_MAP))
        OptionsUtils.recordOptions(parser);
    }
    catch (OptionsUtils.InvalidOptionsException e)
    {
      return false;
    }
    
    if (!exists(REPOSITORY_INFO))
      if (!RepositoryUtils.recordCodeVersion(mainClass))
        // if there were dirty file (i.e. not in version control) write a random string to avoid collisions
        write(
            getFile(DIRTY_FILE_RANDOM_HASH), 
            HashUtils.HASH_FUNCTION.hashUnencodedChars(BriefStrings.generateUniqueId()).toString());
    
    if (!exists(CLASSPATH_INFO))
      DependencyUtils.recordClassPath();
     
    // create softlinks of input, checking they exist
    if (!exists(INPUT_LINKS_FOLDER))
    {
      boolean success = IOLinkUtils.createIOLinks(parser);
      if (!success)
        // if there were missing input file, write a random string to avoid collisions
        write(
            getFile(DIRTY_FILE_RANDOM_HASH), 
            HashUtils.HASH_FUNCTION.hashUnencodedChars(BriefStrings.generateUniqueId()).toString());
    }
    
    // global hash code of the execution inputs, repository, etc
    HashCode global = HashUtils.computeFileHashCodesRecursively(getExecutionInfoFolder());
    write(getFile(GLOBAL_HASH), global.toString());
    return true;
  }
}
