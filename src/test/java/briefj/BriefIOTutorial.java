package briefj;


import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import au.com.bytecode.opencsv.CSVParser;


import static briefj.BriefIO.*;


public class BriefIOTutorial
{
  /**
   * BriefIO
   * -------
   * 
   * Convenient wrappers around common IO operations.
   * Examples of succinct calls, which do not 
   * need typed exceptions, and also maintain memory efficiency (i.e. they are
   * not dumped into a large list, so file that do not fit in memory can still
   * be iterated over):
   */
  @Test
  public void examples()
  {
    for (String line : readLines("src/test/resources/test.csv"))
      System.out.println(line);
    
    for (String line : readLinesFromResource("/test.csv"))
      System.out.println(line);
    
    for (String line : readLinesFromURL("http://www.stat.ubc.ca/~bouchard/pub/geyser.csv"))
      System.out.println(line);
  }
  
  /**
   * If you want to add typed exception back (e.g., later in development),
   * just add ``.check()``:
   */
  @Test
  public void examplesTyped() throws IOException
  {
    for (String line : readLinesFromURL("http://www.stat.ubc.ca/~bouchard/pub/geyser.csv").check())
      System.out.println(line);
  }
  
  /**
   * Returning a ``FluentIterable`` (from the guava project), it is easy to limit, filter, etc
   * (see guava project for more):
   * 
   */
  @Test
  public void examplesFluent()
  {
    for (String line : readLinesFromURL("http://www.stat.ubc.ca/~bouchard/pub/geyser.csv").skip(1).limit(10))
      System.out.println(line);
  }
  
  /**
   * Convenient access to CSV files:
   */
  @Test
  public void examplesCSV()
  {
    for (List<String> line : readLinesFromURL("http://www.stat.ubc.ca/~bouchard/pub/geyser.csv").splitCSV().limit(10))
      System.out.println(line);
  }
  
  /**
   * Which can also be indexed by the name of the columns of the first row via a map:
   */
  @Test
  public void examplesCSVMap()
  {
    for (Map<String,String> line : readLinesFromURL("http://www.stat.ubc.ca/~bouchard/pub/geyser.csv").indexCSV().limit(10))
      System.out.println(line);
  }
  
  /**
   * Different CSV options can be used (see au.com.bytecode.opencsv for details):
   */
  @Test
  public void examplesCSVCustom()
  {
    for (Map<String,String> line : readLinesFromURL("http://www.stat.ubc.ca/~bouchard/pub/geyser.csv").indexCSV(new CSVParser(';')).limit(10))
      System.out.println(line);
  }
  
  /**
   * Output without checked exception, optional charset:
   */
  @Test
  public void examplesOutput()
  {
    File temp = BriefFiles.createTempFile();
    PrintWriter out = output(temp);
    out.println("Hello world");
    out.close();
  }
  
  /**
   * Lists files in directory, with or without suffix filter (without period)
   */
  public void exampleLs()
  {
    for (File f : BriefFiles.ls(new File(".")))
      System.out.println(f);
    for (File f : BriefFiles.ls(new File("."), "txt"))
      System.out.println(f);
    
  }
  
  @Test
  public void testCompression() throws IOException
  {
    File testFile = BriefFiles.createTempFile(".gz");
    BufferedWriter writer = BriefIO.writer(testFile);
    String myString = "test successful";
    writer.write(myString);
    writer.close();
    
    String read = BriefIO.fileToString(testFile);
    Assert.assertEquals(myString, read);
  }
}
