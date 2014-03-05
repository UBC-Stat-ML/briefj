package briefj.tomove;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;


import briefj.BriefIO;
import briefj.BriefLists;

import com.google.common.collect.Sets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;



public class HashUtils
{
  
  public static final HashFunction HASH_FUNCTION = Hashing.sha1();
  public static final String HASH_NAME = "sha1";
  
  public static HashCode computeFileHashCodesRecursively(File directory)
  {
    return computeFileHashCodesRecursively(directory, standardExclusionPatterns);
  }
  
  public static HashCode computeFileHashCodesRecursively(File directory, Set<Pattern> fileNameExclusionPatterns)
  {
    Hasher hasher = HASH_FUNCTION.newHasher();
    loop:for (File file : BriefLists.sort(BriefIO.ls(directory)))
    {
      if (match(file, fileNameExclusionPatterns))
        continue loop;
      HashCode current;
      try
      {
        current = file.isDirectory() ? 
          computeFileHashCodesRecursively(file, fileNameExclusionPatterns) :
          Files.hash(file, HASH_FUNCTION);
        String currentString = current.toString();
        hasher.putUnencodedChars(currentString);
        File hashFile = new File(file.getParentFile(), "." + file.getName() + "." + HASH_NAME);
        BriefIO.write(hashFile, currentString);
      } catch (IOException e)
      {
        throw new RuntimeException(e);
      }
    }
    return hasher.hash();
  }
  
  public static Set<Pattern> standardExclusionPatterns = Sets.newHashSet(
      Pattern.compile("^[.].*$"),
      Pattern.compile("^std(err|out)[.]txt$"),
      Pattern.compile("^.*[.]" + HASH_NAME + "$"));
  
  private static boolean match(File file, Set<Pattern> fileNameExclusionPatterns)
  {
    for (Pattern p : fileNameExclusionPatterns)
      if (p.matcher(file.getName()).matches())
        return true;
    return false;
  }

  /**
   * @param args
   * @throws IOException 
   */
  public static void main(String[] args) throws IOException
  {
    
    File test = new File("/Users/bouchard/temp/bayonet/");
    
    System.out.println(computeFileHashCodesRecursively(test));
    
    
//    HashCodeBuilder builder = new HashCodeBuilder();
//    builder.append(hc);
//    System.out.println(builder.);
  }

}
