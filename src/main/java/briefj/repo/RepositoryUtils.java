package briefj.repo;

import java.io.File;
import java.util.List;

import binc.Command;
import briefj.BriefIO;
import briefj.run.Commands;
import briefj.run.ExecutionInfoFiles;

import com.google.common.base.Joiner;



public class RepositoryUtils
{
  public static VersionControlRepository findRepository(File forThisFile)
  {
    return GitRepository.fromLocal(forThisFile);
  }

  /**
   * 
   * @param runnable
   * @return Whether the code was all in the repository (i.e. all successfully recorded)
   */
  public static boolean recordCodeVersion(Runnable runnable)
  {
    File classFile = findSourceFile(runnable);
    if (classFile == null) 
    {
      System.err.println("WARNING: Could not find class file (lanching from jar?)");
      return false;
    }
    VersionControlRepository repository = findRepository(classFile);
    if (repository == null)
    {
      System.err.println("WARNING: Could not find source repository in parent directories of " + classFile);
      return false;
    }
    List<File> dirtyFiles = repository.dirtyFiles();
    if (!dirtyFiles.isEmpty())
    {
      File dirtyFilesReport = ExecutionInfoFiles.getFile(ExecutionInfoFiles.REPOSITORY_DIRTY_FILES);
      System.err.println("WARNING: There were files not up to date in the code repository (see " + dirtyFilesReport.getAbsolutePath() + ")");
      BriefIO.write(dirtyFilesReport, Joiner.on('\n').join(dirtyFiles));
    }
    BriefIO.write(ExecutionInfoFiles.getFile(ExecutionInfoFiles.REPOSITORY_INFO), toString(repository));
    File scriptFile = ExecutionInfoFiles.getFile(ExecutionInfoFiles.REPOSITORY_CLONE_SCRIPT);
    BriefIO.write(scriptFile, repository.cloneScript());
    try 
    {
      Command.call(Commands.chmod.withArgs("755").appendArg(scriptFile.getAbsolutePath()));
    } 
    catch (Exception e) {}
    
    return dirtyFiles.isEmpty();
  }
  
  private static CharSequence toString(VersionControlRepository repository)
  {
    StringBuilder result = new StringBuilder();
    result.append("local\t" + repository.getLocalAddress() + "\n");
    int i = 0;
    for (String url : repository.getRemoteAddresses())
      result.append("remote_" + (i++) + "\t" + url + "\n");
    String commitId = repository.getCommitIdentifier();
    if (commitId == null)
      commitId = "UNK (epoch=" + System.currentTimeMillis() + ")";
    result.append("commit\t" + commitId + "\n");
    return result.toString();
  }

  public static ClassLoader getClassLoader(Object o)
  {
    Class<?> c = o.getClass();
    ClassLoader loader = c.getClassLoader();
    if ( loader == null ) 
    {
      // Try the bootstrap classloader - obtained from the System Class Loader.
      loader = ClassLoader.getSystemClassLoader();
      while ( loader != null && loader.getParent() != null ) {
        loader = loader.getParent();
      }
    }
    return loader;
  }
  
  public static File findSourceFile(Object o) 
  {
    try { return new File(o.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath()); } 
    catch (Exception e) { throw new RuntimeException(e); }
//    Class<?> c = o.getClass();
//    ClassLoader loader = getClassLoader(o);
//    if (loader != null) 
//    {
//      String name = c.getCanonicalName();
//      URL resource = loader.getResource(name.replace(".", "/") + ".class");
//      while (resource == null && name.contains("."))
//      {
//        name = name.replaceAll("[.][^.]*$", "");
//        resource = loader.getResource(name.replace(".", "/") + ".class");
//      }
//      if ( resource != null ) {
//        File result = new File(resource.toString().replace("file:", ""));
//        if (result.exists())
//          return result;
//      }
//    }
  }
}
