package briefj.run;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;

import briefj.BriefFiles;
import briefj.BriefStrings;
import briefj.OutputManager;



/**
 * Manages a pool of experimental results.
 * 
 * The main responsibility of this class is to provide a directory
 * unique to this process when getResultFolder() is called. This folder
 * is called the result folder.
 * 
 * The common behavior is that the directory 'results/all' will 
 * be created, in which a subdirectory with a unique name and the suffix
 * '.exec'.
 * 
 * This behavior can be overwritten by defining the environment variable
 * SPECIFIED_RESULT_FOLDER. This is used when for example one job is
 * responsible for launching sub-job into a remote server.
 * 
 * @author Alexandre Bouchard (alexandre.bouchard@gmail.com)
 *
 */
public class Results
{
  public static final String SPECIFIED_RESULT_FOLDER = "SPECIFIED_RESULT_FOLDER";
  public static final String LATEST_STRING = "latest";
  public static String DEFAULT_ALL_NAME = "all";
  public static String DEFAULT_POOL_NAME = "results";

  /**
   * Main point of entry: this will return a directory unique
   * to this process.
   * 
   * @return
   */
  public static File getResultFolder()
  {
    if (resultFolder != null)
      return resultFolder;
    // check if the working dir is already a result folder
    synchronized (SPECIFIED_RESULT_FOLDER)
    {
      if (resultFolder != null)
        return resultFolder;
      resultFolder = initResultFolder();
      return resultFolder;
    }
  }
  
  /**
   * Syntactic sugar for new File(getResultFolder(), fileName);
   * @param fileName
   * @return
   */
  public static File getFileInResultFolder(String fileName)
  {
    return new File(getResultFolder(), fileName);
  }
  
  private static OutputManager globalOutputManager = null;
  public static OutputManager getGlobalOutputManager()
  {
    if (globalOutputManager == null)
    {
      globalOutputManager = new OutputManager();
      globalOutputManager.setOutputFolder(getResultFolder());
    }
    return globalOutputManager;
  }
  
  private static File resultFolder = null;
  
  private static void refreshSoftlinks(File result)
  {
    File poolFolder = result.getParentFile().getParentFile(); // up one is 'all', up two is 'results'
    
    File latestFolderSoftLink = new File(poolFolder, LATEST_STRING);
    latestFolderSoftLink.delete();
    try { Files.createSymbolicLink(latestFolderSoftLink.toPath(), Paths.get(DEFAULT_POOL_NAME).relativize(result.toPath())); } 
    catch (IOException e) {}
  }
  
  public static String nextRandomResultFolderName()
  {
    return BriefStrings.currentDataString() + "-" + BriefStrings.generateUniqueId() + ".exec";
  }
  
  public static File initResultFolder()
  {
    String fromEnvironment = System.getenv().get(SPECIFIED_RESULT_FOLDER);
    return initResultFolder(fromEnvironment);
  }

  public static File initResultFolder(String fromEnvironment)
  {
    // if set by an env variable, use that
    // in this case, do not refresh soft links, as the directory structure could be different
    
    if (!StringUtils.isEmpty(fromEnvironment))
    {
      File result = new File(fromEnvironment);
      BriefFiles.createParentDirs(result);
      result.mkdir();
      resultFolder = result;
      return result;
    }
    else
    {
      // otherwise, create a new one
      String name = nextRandomResultFolderName(); //getMainClassString() + "-" + BriefStrings.generateUniqueId() + ".exec";
      
      if (!poolFolder.exists())
        poolFolder.mkdir();
      if (!poolFolder.isDirectory())
        throw new RuntimeException();
      
      File allResults = new File(poolFolder, DEFAULT_ALL_NAME);
      
      allResults.mkdir();
      File result = new File(allResults, name);
      result.mkdir();
      
      // refresh recent softlinks
      refreshSoftlinks(result);
      
      resultFolder = result;
      return result;
    }
  }
  
  private static File poolFolder = new File(DEFAULT_POOL_NAME);

  /**
   * Create a subdirectory in the result folder and return 
   * a pointer to that subdirectory.
   * @param string
   * @return
   */
  public static File getFolderInResultFolder(String string)
  {
    File result = getFileInResultFolder(string);
    result.mkdirs();
    return result;
  }

  /**
   * Set a directory where 'all' and 'latest' will be created in.
   * Default is ./results
   * @param poolFolder
   */
  public static void setResultsFolder(File poolFolder)
  {
    if (resultFolder != null)
      throw new RuntimeException("Cannot set the pool after the result folder created (it is too late)");
    Results.poolFolder = poolFolder;
  }
}
