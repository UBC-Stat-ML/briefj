package briefj;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.io.Files;



public class BriefFiles
{

  /**
   * list, filtering with the given suffix (case unsensitive) and sort by name
   * @param basePath
   * @return
   */
  public static List<File> ls(final File basePath) 
  {
    return BriefFiles.ls(basePath, "");
  }

  /**
   * 
   * @param basePath
   * @param suffixFilter Do NOT include the period
   * @return The files under the basePath that match the filter, sorted by name.
   */
  public static List<File> ls(final File basePath, final String suffixFilter) 
  {
    final FilenameFilter filter = (suffixFilter == null || suffixFilter.equals("") ?
        new FilenameFilter() {
          public boolean accept(File arg0, String arg1) { return true; }
        } :
        BriefFiles.suffixFilter(suffixFilter));
    if (!basePath.isDirectory())
      throw new RuntimeException("Directory does not exists:" + basePath);
    List<File> result = new ArrayList<File>();
    for (final File item : basePath.listFiles(filter)) result.add(item);
    Collections.sort(result);
    return result;
  }

  public static void createParentDirs(File file)
  {
    try
    {
      Files.createParentDirs(file);
    } catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  public static FilenameFilter suffixFilter(final String ... suffixesWithoutPeriod)
  {
    return new FilenameFilter() 
    {
      public boolean accept(File dir, String file) 
      {
        if (suffixesWithoutPeriod == null || suffixesWithoutPeriod.equals("")) return true;
        for (String suffixWithoutPeriod : suffixesWithoutPeriod)
          if (file.toUpperCase().matches(".*[.]" + suffixWithoutPeriod.toUpperCase() + "$"))
            return true;
        return false;
      }
    };
  }
  
  public static File createTempFile()
  {
    return createTempFile("temp");
  }

  public static File createTempFile(String suffix)
  {
    File result;
    try
    {
      result = File.createTempFile("Briefj-" + System.currentTimeMillis(), "." + suffix);
      return result;
    } catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public static File currentDirectory()
  {
    try
    {
      return new File("").getCanonicalFile();
    } 
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  /**
   * Check if the file currentDir/fileName exists, if not 
   * try recursively with the parents, i.e. currentDir/../fileName,
   * currentDir/../../fileName, etc
   * and return the first match or null if nothing is found.
   * @param currentDir
   * @param fileName
   * @return
   */
  public static File findFileInParents(File currentDir, String fileName)
  {
    currentDir = currentDir.getAbsoluteFile();
    File potential = new File(currentDir, fileName);
    if (potential.exists())
      return potential;
    File parent = currentDir.getParentFile();
    if (parent == null)
      return null;
    else
      return findFileInParents(parent, fileName);
  }
  
  /**
   * Same as findFileInParents(File currentDir, String fileName), but
   * from the current directory.
   * @param fileName
   * @return
   */
  public static File findFileInParents(String fileName)
  {
    return findFileInParents(currentDirectory(), fileName);
  }

}
