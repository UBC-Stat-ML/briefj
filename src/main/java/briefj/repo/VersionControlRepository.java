package briefj.repo;

import java.io.File;
import java.util.List;



public interface VersionControlRepository
{
  public String getLocalAddress();
  public List<String> getRemoteAddresses();
  public String getCommitIdentifier();
  
  public String cloneScript();
  
  public List<File> dirtyFiles();
}