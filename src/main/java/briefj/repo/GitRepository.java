package briefj.repo;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import briefj.BriefLists;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Loosely based on examples from 
 * https://github.com/centic9/jgit-cookbook
 * and
 * http://stackoverflow.com/questions/6861881/jgit-cannot-find-a-tutorial-or-simple-example
 * 
 * @author Alexandre Bouchard (alexandre.bouchard@gmail.com)
 *
 */
public class GitRepository implements VersionControlRepository
{
  private final Repository repository;
  
  public static GitRepository fromLocal(File localFile)
  {
    try
    {
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

  /**
   * 
   * @param commitId
   * @return Time of the given commitId (ms from the Epoch)
   */
  public long commitTime(String commitId)
  {
    try
    {
      Git git = new Git(repository);
      
      for (RevCommit commit : git.log().call())
        if (commit.getId().name().equals(commitId.toLowerCase()))
        {
          long secs = commit.getCommitTime();
          return secs * 1000L;
        }
      
    } catch (Exception e)
    {
      throw new RuntimeException(e);
    }
    throw new RuntimeException();
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
      return null;
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