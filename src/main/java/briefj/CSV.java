package briefj;

import java.util.Collection;


/**
 * 
 * @author Alexandre Bouchard (alexandre.bouchard@gmail.com)
 *
 */
public class CSV
{

  /**
   * 
   * @param objects
   * @return
   */
  public static String toCSV(@SuppressWarnings("rawtypes") Collection objects)
  {
    return toCSV(objects.toArray());
  }
  
  /**
   * Create a csv line with the provided objects' toString() methods:
   * - separate with commas
   * - add quotes and escape them inside 
   * - escape backslashes as well
   * @param objects
   * @return
   */
  public static String toCSV(Object... objects)
  {
    StringBuilder result = new StringBuilder();
    for (int i =0 ; i < objects.length; i++)
    {
      String cur = objects[i] == null ? "" : objects[i].toString();
      cur = cur.replace("\\", "\\\\");
      if (cur.contains("\"")||cur.contains(","))
        result.append("\"" + cur.replaceAll("\"", "\"\"") + "\"");
      else
        result.append(cur);
      if (i != objects.length-1)
        result.append(",");
    }
    return result.toString();
  }
}
