package briefj.db;

import java.util.Collections;
import java.util.Set;

import briefj.BriefIO;

import com.google.common.collect.Sets;



public class SqlKeywords
{
  public static final Set<String> keywords = init();

  private static Set<String> init()
  {
    Set<String> result = Sets.newHashSet();
    for (String line : BriefIO.readLinesFromResource("/briefj/db/sqlKeywords.txt"))
      result.add(line);
    return Collections.unmodifiableSet(result);
  }
  
  public static void main(String [] args)
  {
    System.out.println(keywords);
  }
}
