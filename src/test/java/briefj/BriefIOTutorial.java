package briefj;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import au.com.bytecode.opencsv.CSVParser;

import tutorialj.Tutorial;

import static briefj.BriefIO.*;

/**
 * BriefIO
 * -------
 * 
 * Convenient wrappers around common IO operations.
 */
@Tutorial
public class BriefIOTutorial
{
  /**
   * Examples of succinct calls, which do not 
   * need typed exceptions, and also maintain memory efficiency (i.e. they are
   * not dumped into a large list, so file that do not fit in memory can still
   * be iterated over):
   */
  @Tutorial(showSource=true)
  @Test
  public void examples()
  {
    for (String line : readLines("src/test/resources/test.csv"))
      System.out.println(line);
    
    for (String line : readLinesFromResource("/test.csv"))
      System.out.println(line);
    
    for (String line : readLinesFromURL("http://stat.ubc.ca/~bouchard/pub/geyser.csv"))
      System.out.println(line);
  }
  
  /**
   * If you want to add typed exception back (e.g., later in development),
   * just add ``.check()``:
   */
  @Tutorial(showSource=true, nextStep = DefaultCharset.class)
  @Test
  public void examplesTyped() throws IOException
  {
    for (String line : readLinesFromURL("http://stat.ubc.ca/~bouchard/pub/geyser.csv").check())
      System.out.println(line);
  }
  
  /**
   * Returning a ``FluentIterable`` (from the guava project), it is easy to limit, filter, etc
   * (see guava project for more):
   * 
   */
  @Tutorial(showSource=true)
  @Test
  public void examplesFluent()
  {
    for (String line : readLinesFromURL("http://stat.ubc.ca/~bouchard/pub/geyser.csv").skip(1).limit(10))
      System.out.println(line);
  }
  
  /**
   * Convenient access to CSV files:
   */
  @Tutorial(showSource=true)
  @Test
  public void examplesCSV()
  {
    for (List<String> line : readLinesFromURL("http://stat.ubc.ca/~bouchard/pub/geyser.csv").splitCSV().limit(10))
      System.out.println(line);
  }
  
  /**
   * Which can also be indexed by the name of the columns of the first row via a map:
   */
  @Tutorial(showSource=true)
  @Test
  public void examplesCSVMap()
  {
    for (Map<String,String> line : readLinesFromURL("http://stat.ubc.ca/~bouchard/pub/geyser.csv").indexCSV().limit(10))
      System.out.println(line);
  }
  
  /**
   * Different CSV options can be used (see au.com.bytecode.opencsv for details):
   */
  @Tutorial(showSource=true)
  @Test
  public void examplesCSVCustom()
  {
    for (Map<String,String> line : readLinesFromURL("http://stat.ubc.ca/~bouchard/pub/geyser.csv").indexCSV(new CSVParser(';')).limit(10))
      System.out.println(line);
  }
}
