package briefj.tomove;


import java.io.File;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.common.hash.HashCode;

import briefj.BriefIO;
import briefj.BriefStrings;
import briefj.tomove.RedirectionUtils.Tees;

//import fig.basic.InputFile;
//import fig.basic.Option;
//import fig.basic.OptionsParser;

import static briefj.tomove.ExecutionInfoFiles.*;

public class ManagedExecution
{
  
  

  public static void start(final Runnable runnable, final String [] args)
  {
    throw new RuntimeException();
//    // redirect std out, std err before everything else to help diagnosis remote problems
//    Tees tees = exists(STD_OUT_FILE) ?
//      null :
//      RedirectionUtils.createTees(getExecutionInfoFolder());
//    
//    boolean exception = false;
//    
//    try
//    {
//      
//      TODO: instead of checking existence at each step, check for only one key file produced at the end of process
//      TODO: document that this is prevision of other tool that set thing up
//      NOTE THAT SOME will need special treatment, like input folder
//    
//      OptionsParser parser = OptionsUtils.parseOptions(runnable, args);
//      if (!exists(OPTIONS_MAP))
//        OptionsUtils.recordOptions(parser);
//      
//      if (!exists(REPOSITORY_INFO))
//        if (!RepositoryUtils.recordCodeVersion(runnable))
//          // if there were dirty file (i.e. not in version control) write a random string to avoid collisions
//          BriefIO.write(
//              getFile(DIRTY_FILE_RANDOM_HASH), 
//              HashUtils.HASH_FUNCTION.hashUnencodedChars(BriefStrings.generateUniqueId()).toString());
//      
//      if (!exists(CLASSPATH_INFO))
//        DependencyUtils.recordClassPath();
//      
//      // create softlinks of input, checking they exist
//      if (!exists(INPUT_LINKS_FOLDER))
//      {
//        List<File> missingInputs = IOLinkUtils.createIOLinks(parser);
//        if (!missingInputs.isEmpty())
//          TODO
//      }
//      
//      // global hash code of the execution inputs
//      HashCode global = HashUtils.computeFileHashCodesRecursively(getExecutionInfoFolder());
//      BriefIO.write(getFile(ExecutionInfoFiles.GLOBAL_HASH), global.toString());
//      
//      // things that should not be hashed: authorship, date, time, architecture, etc
//      
//      // lookup cache database, create rerun script, store command line argument: might not be appropriate here, put in launch utils
//      
//
//      runnable.run();
//      
//      output softlinks after?
//    }
//    catch (OptionsUtils.InvalidOptionsException e) 
//    {
//      exception = true;
//    }
//    catch (Exception e)
//    {
//      exception = true;
//      System.err.println(ExceptionUtils.getStackTrace(e));
//    }
//    
//    if (exception)
//      TODO
//    
//    if (tees != null)
//      tees.close();
  }

//  public static void main(String [] args)
//  {
//    start(new Test(), args);
//  }
//  
//  public static class Test implements Runnable
//  {
//    @Option 
//    @InputFile
//    public String inp = "/a/file";
//
//    @Override
//    public void run()
//    {
//      System.out.println("hell w");
//    }
//    
//  }

}
