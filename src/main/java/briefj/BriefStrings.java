package briefj;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.RandomStringUtils;



public class BriefStrings
{

  public static List<String> allGroupsFromFirstMatch(String p, String string)
  {
    return allGroupsFromFirstMatch(Pattern.compile(p), string);
  }
  public static List<String> allGroupsFromFirstMatch(Pattern p, String string)
  {
    Matcher m = p.matcher(string);
    List<String> result = new ArrayList<String>();
    if (!m.find()) return result;
    for (int i = 1; i <= m.groupCount(); i++)
      result.add(m.group(i));
    return result;
  }
  
  public static String firstGroupFromFirstMatch(Pattern p, String string)
  {
    Matcher m = p.matcher(string);
    if (!m.find()) return null;
    return m.group(1);
  }
  
  public static String firstGroupFromFirstMatch(String regex, String string)
  {
    return firstGroupFromFirstMatch(Pattern.compile(regex), string);
  }
  
  private static final int ID_LENGTH = 8;
  public static String generateUniqueId() 
  {
      return RandomStringUtils.randomAlphanumeric(ID_LENGTH);
  }
}
