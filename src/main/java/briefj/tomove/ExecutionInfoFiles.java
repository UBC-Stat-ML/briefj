package briefj.tomove;

import java.io.File;



public class ExecutionInfoFiles
{
  public static File getExecutionInfoFolder()
  {
    File result = Results.getFileInResultFolder("executionInfo");
    result.mkdir();
    return result;
  }
  
  public static File getFile(String fileName)
  {
    File result = new File(getExecutionInfoFolder(), fileName);
    result.getParentFile().mkdir();
    return result;
  }
  
  public static final String OPTIONS_MAP = "arguments/options.map";
  public static final String OPTIONS_DESCRIPTIONS = "arguments/options-help-and-defaults.txt";
  
  public static final String STD_OUT_FILE = "stdout.txt";
  public static final String STD_ERR_FILE = "stderr.txt";
  
  public static final String REPOSITORY_DIRTY_FILES = "code/dirty-files.txt";
  public static final String REPOSITORY_INFO = "code/info.map";
  public static final String REPOSITORY_CLONE_SCRIPT = "code/clone-script.sh";
  
  public static final String CLASSPATH_INFO = "code/classpath.txt";
  
  public static final String GLOBAL_HASH = "global." + HashUtils.HASH_NAME;
  
  public static final String DIRTY_FILE_RANDOM_HASH = "dirty." + HashUtils.HASH_NAME;

  public static final String INPUT_LINKS_FOLDER = "inputs";
  public static final String OUTPUT_LINKS_FOLDER = "outputs";
  
  public static boolean exists(String fileName)
  {
    return getFile(fileName).exists();
  }
  
  
}
