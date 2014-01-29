
package briefj;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;

import com.beust.jcommander.internal.Maps;
import com.google.common.io.Files;



/**
 * 
 * Make it easier to write to csv into a group of files.
 * 
 * Usage:
 * 
 * OutputManager out = new OutputManager();
 * out.setOutputFolder("outputs");
 * out.write("firstFile", "xCoord", 5, "yCoord", 9);
 * out.write("firstFile", "xCoord", 6, "yCoord", 11);
 * out.write("secondFile", "name", "Alex");
 * out.close();
 * 
 * This will create a directory outputs, with two files 
 * in it, firstFile.csv and secondFile.csv
 * 
 * firstFile.csv would contain three rows for example
 * (1 for header, 2 with data)
 * 
 * @author Alexandre Bouchard (alexandre.bouchard@gmail.com)
 *
 */
public class OutputManager 
{
  /**
   * Write to file, and print to standard out
   * 
   * @param masterKey The name of the file (without .csv)
   * @param keyValues Alternation of keys and values
   */
  public void printWrite(String masterKey, Object... keyValues)
  {
    write(masterKey, keyValues);
    StringBuilder toPrint = new StringBuilder();
    toPrint.append(masterKey + ": ");
    final int len = keyValues.length/2;
    for (int i = 0; i < len; i++)
      toPrint.append("" + keyValues[i*2] + "=" + keyValues[i*2 + 1] + (i == len - 1 ? "" : ", "));
    System.out.println(toPrint);
  }
  
  /**
   * 
   * @param masterKey
   * @param keyValues
   */
  public void write(String masterKey, Object... keyValues)
  {
    if ((keyValues.length % 2) != 0) 
    {
      System.err.println("Malformed OutputManager.print():" + Arrays.toString(keyValues));
      return;
    }
    final boolean needPrintHeader = !hasWriter(masterKey);
    final int len = keyValues.length/2;
    Object [] heads = (needPrintHeader ? new Object[len] : null);
    Object [] values= new Object[len];
    
    for (int i = 0; i < len; i++)
    {
      Object key = keyValues[i*2],
             value=keyValues[i*2+1];
      if (needPrintHeader)
        heads[i] = key;
      values[i] = value; 
    }

    if (needPrintHeader)
      print(masterKey, heads);
    print(masterKey, values);
  }
  
  /**
   * Close all the csv writers
   */
  public void close()
  {
    for (PrintWriter out : _writers.values())
      out.close();
  }
  
  /**
   * 
   */
  public void flush()
  {
    for (PrintWriter out : _writers.values())
      out.flush();
  }
  
  private Map<String, PrintWriter> _writers = Maps.newHashMap();
  private File _resultsFolder = null;
  private final int flushInterval = 5;
  private int current = 0;
  private PrintWriter getWriter(String key)
  {
    if ((++current % flushInterval) == 0) flush();
    PrintWriter result = _writers.get(key);
    if (result != null) return result;
    result = BriefIO.output(getOutputFile(key));
    _writers.put(key, result);
    return result;
  }
  
  /**
   * 
   * @param key 
   * @return The file used to write this masterKey
   */
  public File getOutputFile(String key)
  {
    return new File(getResultsFolder(), key + ".csv");
  }
  
  private boolean hasWriter(String key) { return _writers.containsKey(key); }
  private File getResultsFolder()
  {
    if (_resultsFolder != null) return _resultsFolder;
    _resultsFolder = new File(".");
    _resultsFolder.mkdir();
    return _resultsFolder;
  }
  private void print(String key, Object... objects)
  {
    getWriter(key).println(CSV.toCSV(objects));
  }

  /**
   * 
   * @param f Directory to create csv files into
   */
  public void setOutputFolder(File f)
  {
    try
    {
      Files.createParentDirs(f);
      f.mkdir();
    } catch (IOException e)
    {
      throw new RuntimeException(e);
    }
    
    this._resultsFolder = f;
  }
  public File getOutputFolder()
  {
    return getResultsFolder();
  }

}