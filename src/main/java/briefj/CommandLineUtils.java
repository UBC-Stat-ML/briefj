package briefj;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;


import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.tools.ant.util.TeeOutputStream;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;

import briefj.run.Results;





public class CommandLineUtils
{
//  public static class MetaOptions
//  {
//    @Option public boolean acceptMissingGitInfo = false;
//  }
//  
//  
//  public static void start(final Runnable runnable, final String [] args)
//  {
//    // parse options
//    final OptionsParser parser = new OptionsParser();
//    parser.register(runnable);
//    if (!parser.parse(args))
//      System.exit(1);
//    
//    // redirect std out, std err
//    FileOutputStream stdOutRedirect = createTee(Results.getFileInResultFolder("stdout.txt"), true);
//    FileOutputStream stdErrRedirect = createTee(Results.getFileInResultFolder("stderr.txt"), false);
//    
//    // try to find the main code base
//    recordMainCodeBaseVersion(runnable);
//    
//    // validate that the input files actually exist
//    // TODO
//    
//    
//    // record class path
//    // TODO
//    
//    // save options
//    
//    
//    // try to figure out the version(s) of the software
//    
//    
//    // create softlinks and keep hashcode of inputs
//    
//    // create re-run script <-- actually no, cleaner to have something more outside, a la launchpad do that
//    // do try to create the repository URL though
//    
//    // run
//    try 
//    {
//      runnable.run();
//    }
//    catch (Exception e)
//    {
//      System.err.println(ExceptionUtils.getStackTrace(e));
//    }
//    try
//    {
//      stdOutRedirect.close();
//      stdErrRedirect.close();
//    } 
//    catch (IOException e)
//    {
//      throw new RuntimeException(e);
//    }
//  }
//  
//  
//  private static boolean recordMainCodeBaseVersion(Object object)
//  {
//    File location = attemptLocatingClassFile(object);
//    if (location == null) 
//    {
//      System.err.println("WARNING: Could not find class file (lanching from jar?)");
//      return false;
//    }
//    location = findGitRoot(location);
//    if (location == null)
//    {
//      System.err.println("WARNING: Could not find git repository corresponding to main code base");
//      return false;
//    }
//    
//    try
//    {
//      Repository repo = new FileRepository(new File(location, ".git"));
//      Git git = new Git(repo);
//      Status status = git.status().call();
//      boolean isClean = status.isClean();
//      if (!isClean)
//        System.err.println("WARNING: The git repository is not clean (there are uncommitted/unadded files)");
//      
//      ObjectId head = repo.resolve("HEAD");
////      System.out.println(head.getName());
////      System.out.println("is clean:" + isClean);
//      
//      // create view source script
//      // TODO
//      
//      repo.close();
//    } 
//    catch (Exception e)
//    {
//      System.err.println("");
//    }
//  }
//  
//  private static File findGitRoot(File location)
//  {
//    if (location.isDirectory())
//    {
//      File gitHidden = new File(location, ".git");
//      if (gitHidden.exists())
//        return location;
//    }
//
//    if (location.getParentFile() == null)
//      return null;
//    else
//      return findGitRoot(location.getParentFile());
//  }
//
//
//
//
//  
//  public static void main(String [] args)
//  {
//    start(new Test(), args);
//  }
//  
//  public static class Test2 
//  {
//    @Option public int number = 2;
//  }
//  
//  public static class Test implements Runnable
//  {
//    @OptionSet(name="sub") public Test2 sub = new Test2();
//
//    @Option public HashSet<String> test = new HashSet<String>();
//    @Option public HashSet<String> test2 = new HashSet<String>(Arrays.asList("a","b"));
//    
//    @Override
//    public void run()
//    {
//      System.out.println("OK");
//    }
//    
//  }
}
