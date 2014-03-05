package briefj.tomove;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import binc.Command;
import briefj.BriefIO;
import briefj.BriefLists;
import briefj.BriefLog;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;



public class RepositoryUtils
{
  private static interface VersionControlRepository
  {
    public String getLocalAddress();
    public List<String> getRemoteAddresses();
    public String getCommitIdentifier();
    
    public String cloneScript();
    
    public List<File> dirtyFiles();
  }
  
  /**
   * Loosely based on examples from 
   * https://github.com/centic9/jgit-cookbook
   * and
   * http://stackoverflow.com/questions/6861881/jgit-cannot-find-a-tutorial-or-simple-example
   * 
   * @author Alexandre Bouchard (alexandre.bouchard@gmail.com)
   *
   */
  private static class GitRepository implements VersionControlRepository
  {
    private final Repository repository;
    
    public static GitRepository fromLocal(File localFile)
    {
      try
      {
        // the code below does not seem to work
//        repository = builder.setGitDir(localFile)
//          .findGitDir() // scan up the file system tree
//          .build();
        File location = findGitRoot(localFile);
        Repository repository = new FileRepository(new File(location, ".git"));
        return new GitRepository(repository);
      } 
      catch (IOException e)
      {
        return null;
      }
    }
    
    private static File findGitRoot(File location)
    {
      if (location.isDirectory())
      {
        File gitHidden = new File(location, ".git");
        if (gitHidden.exists())
          return location;
      }

      if (location.getParentFile() == null)
        return null;
      else
        return findGitRoot(location.getParentFile());
    }


    public GitRepository(Repository repository)
    {
      this.repository = repository;
    }

    @Override
    public String getLocalAddress()
    {
      return repository.getDirectory().getParentFile().getAbsolutePath();
    }

    @Override
    public List<String> getRemoteAddresses()
    {
      List<String> result = Lists.newArrayList();
      Config storedConfig = repository.getConfig();
      List<String> remotes = Lists.newArrayList(storedConfig.getSubsections("remote"));
      Collections.sort(remotes);
      for (String remoteName : remotes) 
      {
        String url = storedConfig.getString("remote", remoteName, "url");
        result.add(url);
      }
      return result;
    }

    @Override
    public String getCommitIdentifier()
    {
      try
      {
        return repository.resolve("HEAD").getName();
      } 
      catch (Exception e)
      {
        throw new RuntimeException(e);
      }
    }

    @Override
    public String cloneScript()
    {
      return 
          "#!/bin/bash\n" +
      		"git clone " + getLocalAddress() + "\n" +
      		"git checkout " + getCommitIdentifier();
    }

    @Override
    public List<File> dirtyFiles()
    {
      try
      {
        Status status = new Git(repository).status().call();
        Set<String> fileNames = Sets.newHashSet();
        fileNames.addAll(status.getAdded());
        fileNames.addAll(status.getChanged());
        fileNames.addAll(status.getConflicting());
        fileNames.addAll(status.getMissing());
        fileNames.addAll(status.getModified());
        fileNames.addAll(status.getRemoved());
        fileNames.addAll(status.getUntracked());
        fileNames.addAll(status.getUntrackedFolders());
        List<File> result = Lists.newArrayList();
        for (String fileName : BriefLists.sort(fileNames))
          result.add(new File(fileName));
        return result;
      } 
      catch (Exception e)
      {
        throw new RuntimeException(e);
      }
    }
    
  }
  
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
    File classFile = findClassFile(runnable);
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
      Command.call(Commands.chmod.withArgs("755 " + scriptFile.getAbsolutePath()));
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
    result.append("commit\t" + repository.getCommitIdentifier() + "\n");
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
  
  public static File findClassFile(Object o) 
  {
    Class<?> c = o.getClass();
    ClassLoader loader = getClassLoader(o);
    if (loader != null) 
    {
      String name = c.getCanonicalName();
      URL resource = loader.getResource(name.replace(".", "/") + ".class");
      while (resource == null && name.contains("."))
      {
        name = name.replaceAll("[.][^.]*$", "");
        resource = loader.getResource(name.replace(".", "/") + ".class");
      }
      if ( resource != null ) {
        File result = new File(resource.toString().replace("file:", ""));
        if (result.exists())
          return result;
      }
    }
    return null;
  }
}
