package briefj;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;



public class BriefLog
{
  private static Set<String> sentMessages = Collections.synchronizedSet(new HashSet<String>());
  /**
   * Print something, making sure that the same message is not displayed
   * again in the execution of the program.
   * 
   * Application: A useful way for the programmer to remind him/herself of something
   * critical is to just print it every time the program is run. However,
   * when this message is in some inner loop, this might pollute the std out.
   * @param string
   */
  public static void warnOnce(String string)
  {
    if (sentMessages.contains(string)) return;
    sentMessages.add(string);
    warn(string);
  }
  
  public static void warn(String string)
  {
    System.err.println(WARNING_STRING + string);
    nWarnings++;
  }
  
  public static int getNumberOfWarnings() { return nWarnings; }
  
  private static int nWarnings = 0;
  
  public static String WARNING_STRING = "WARNING: ";
}
