package briefj;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import tutorialj.Tutorial;

import au.com.bytecode.opencsv.CSVParser;

import com.beust.jcommander.internal.Maps;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.io.CharSource;
import com.google.common.io.Files;

/**
 * BriefIO
 * -------
 * 
 * Convenient wrappers around common IO operations.
 */
@Tutorial(order=1)
public class BriefIO
{
  
  public static ReadLineIterable readLines(File file)
  {
    return readLines(file, DefaultCharset.defaultCharset);
  }
  public static ReadLineIterable readLines(String file)
  {
    return readLines(new File(file));
  }
  public static ReadLineIterable readLines(File file, Charset charset)
  {
    return new ReadLineIterable(Files.asCharSource(file, charset));
  }
  

  public static ReadLineIterable readLinesFromURL(String url)
  {
    return readLinesFromURL(url, DefaultCharset.defaultCharset);
  }
  public static ReadLineIterable readLinesFromURL(String url, Charset charset)
  {
    return new ReadLineIterable(new URLCharSource(url, charset));
  }
  
  private static class ResourceCharSource extends CharSource
  {
    private final Charset charset;
    private final String url;
    public ResourceCharSource(String url, Charset charset)
    {
      this.charset = charset;
      this.url = url;
    }
    @Override
    public Reader openStream() throws IOException
    {
      return new BufferedReader(new InputStreamReader(new Object().getClass().getResourceAsStream(url), charset));
    }
  }
  
  private static class URLCharSource extends CharSource
  {
    private final Charset charset;
    private final String url;
    public URLCharSource(String url, Charset charset)
    {
      this.charset = charset;
      this.url = url;
    }
    @Override
    public Reader openStream() throws IOException
    {
      return new BufferedReader(new InputStreamReader(new URL(url).openStream(), charset));
    }
  }
  
  /**
   * 
   * @param str
   * @return
   * 
   * Note: to make work in eclipse, add resources folder in Run config/classpath/user entry/advanced/folder
   */
  public static ReadLineIterable readLinesFromResource(String resource)
  {
    return readLinesFromResource(resource, DefaultCharset.defaultCharset);
  }
  public static ReadLineIterable readLinesFromResource(String resource, Charset charset)
  {
    return new ReadLineIterable(new ResourceCharSource(resource, charset));
  }
  

  
  public static final Function<String, List<String>> splitCSV = splitCSV(new CSVParser());
  public static Function<String, List<String>> splitCSV(final CSVParser parser) 
  {
    return new Function<String, List<String>>() {
      @Override
      public List<String> apply(String input)
      {
        try { return Arrays.asList(parser.parseLine(input)); } 
        catch (IOException e) { throw new RuntimeException(e); }
      }
    };
  }
  
  public static Function<List<String>, Map<String,String>> listToMap(final List<String> keys)
  {
    return new Function<List<String>, Map<String,String>>() {

      @Override
      public Map<String, String> apply(List<String> values)
      {
        final int size = keys.size();
        if (size != values.size())
          throw new RuntimeException("The number of keys should have the same length as the number of values.");
        Map<String,String> result = Maps.newHashMap();
        for (int i = 0; i < size; i++)
          result.put(keys.get(i), values.get(i));
        return result;
      }
    };
  }
  
  public static class ReadLineIterable extends FluentIterable<String>
  {
    private final CharSource input;
    
    private ReadLineIterable(CharSource input) { this.input = input; }
    
    public ReadLineIterable check() throws IOException
    {
      input.readFirstLine();
      return this;
    }
    
    public FluentIterable<List<String>> splitCSV()
    {
      return splitCSV(new CSVParser());
    }
    public FluentIterable<List<String>> splitCSV(CSVParser parser)
    {
      return transform(BriefIO.splitCSV(parser));
    }
    
    public FluentIterable<Map<String,String>> indexCSV()
    { 
      return indexCSV(new CSVParser());
    }
    public FluentIterable<Map<String,String>> indexCSV(CSVParser parser)
    {
      // read the first line, by default, zero columns (to support fully empty file)
      @SuppressWarnings("unchecked")
      final List<String> header = Iterables.getFirst(splitCSV(parser), Collections.EMPTY_LIST);
      
      // for the body, skip the header:
      FluentIterable<List<String>> bodyIterable = splitCSV(parser).skip(1);
      
      return bodyIterable.transform(listToMap(header));
    }

    @Override
    public Iterator<String> iterator()
    {
      try { return new BufferedReaderIterator(input.openBufferedStream()); } 
      catch (IOException e) { throw new RuntimeException(e); }
    }
  }
  
  private static class BufferedReaderIterator implements Iterator<String>
  {
    private BufferedReader reader = null;
    private String currentLine = null;
    
    public BufferedReaderIterator(BufferedReader r)
    {
      this.reader = r;
    }
    boolean closed = false;
    public boolean hasNext()
    {
      if (currentLine != null)
      {
        return true;
      }
      if (closed) return false;
      try
      {
        currentLine = reader.readLine();
        if (currentLine == null)
        {
          reader.close();
          closed = true;
        }
        return !(currentLine == null);
      }
      catch (IOException ioe)
      {
        throw new RuntimeException(ioe);
      }
    }
    public String next()
    {
      if (!hasNext())
      {
        throw new NoSuchElementException();
      }
      String result = currentLine;
      currentLine = null;
      return result;
    }
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
  
}
