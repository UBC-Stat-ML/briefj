package briefj;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomStringUtils; 

import com.beust.jcommander.internal.Lists;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;



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
  
  public static List<String> firstGroupFromAllMatches(Pattern p, String string)
  {
    List<String> result = Lists.newArrayList();
    Matcher m = p.matcher(string);
    while (m.find())
      result.add(m.group(1));
    return result;
  }
  
  public static List<String> firstGroupFromAllMatches(String regex, String string)
  {
    return firstGroupFromAllMatches(Pattern.compile(regex), string);
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
  public static String indent(String str)
  {
    return indent(str, "  ");
  }
  public static String indent(String str, String indentationStr)
  {
    List<String> lines = Splitter.on("\n").splitToList(str);
    return indentationStr + Joiner.on("\n" + indentationStr).join(lines);
  }
  
  public static void  main(String [] args)
  {
    String test = "asdfas\nasdf\n";
    System.out.println(test);
    System.out.println("---");
    System.out.println(indent(test));
    System.out.println("---");
  }
}
