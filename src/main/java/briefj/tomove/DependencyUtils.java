package briefj.tomove;

import java.io.File;

import briefj.BriefIO;



public class DependencyUtils
{

  public static void recordClassPath()
  {
    String cp = System.getProperties().getProperty("java.class.path", null);
    File destination = ExecutionInfoFiles.getFile(ExecutionInfoFiles.CLASSPATH_INFO);
    BriefIO.write(destination, cp.replaceAll("[:]", "\n"));
  }
  
}
