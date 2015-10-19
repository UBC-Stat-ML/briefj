package briefj.run;

import java.io.File;




public class ExecutionInfoFiles
{
  public static final String infoFileDirectoryName = "executionInfo";
  
  public static File getExecutionInfoFolder()
  {
    File result = Results.getFileInResultFolder(infoFileDirectoryName);
    result.mkdir();
    return result;
  }
  
  public static File getFile(String fileName)
  {
    File result = new File(getExecutionInfoFolder(), fileName);
    result.getParentFile().mkdir();
    return result;
  }
  
  public static File getFile(String fileName, File execFolder)
  {
    File infoFileDirectory = new File(execFolder, infoFileDirectoryName);
    return new File(infoFileDirectory, fileName);
  }
  
  public static final String OPTIONS_MAP = "options.map";
  public static final String OPTIONS_DESCRIPTIONS = "options-help-and-defaults.txt";
  
  public static final String OUT_MAP = "output.map";
  
  public static final String STD_OUT_FILE = "stdout.txt";
  public static final String STD_ERR_FILE = "stderr.txt";
  
  public static final String START_TIME_FILE = "start-time.txt";
  public static final String END_TIME_FILE = "end-time.txt";
  
  public static final String MAIN_CLASS_FILE = "main-class.txt";
  public static final String JVM_OPTIONS = "jvm-options.txt";
  public static final String CLASSPATH_INFO = "code/classpath.txt";
  public static final String EXCEPTION_FILE = "exception-message.txt";
  public static final String JAVA_ARGUMENTS = "java-arguments.txt";
  
  public static final String REPOSITORY_DIRTY_FILES = "code/dirty-files.txt";
  public static final String REPOSITORY_INFO = "code/info.map";
  public static final String REPOSITORY_CLONE_SCRIPT = "code/clone-script.sh";
  
  public static final String WORKING_DIR = "working-dir.txt";
  
  public static final String HOST_INFO_FILE = "host-info.map";
  
  public static final String GLOBAL_HASH = "global." + HashUtils.HASH_NAME;
  
  public static final String DIRTY_FILE_RANDOM_HASH = "dirty." + HashUtils.HASH_NAME;

  public static final String INPUT_LINKS_FOLDER = "inputs";
  public static final String OUTPUT_LINKS_FOLDER = "outputs";
  
  public static boolean exists(String fileName)
  {
    return getFile(fileName).exists();
  }
  
  
}
