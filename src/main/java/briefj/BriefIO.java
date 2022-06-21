package briefj;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import au.com.bytecode.opencsv.CSVParser;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.io.CharSource;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



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
    return new ReadLineIterable(new FileCharSource(file, charset)); 
  }

  public static ReadLineIterable readLinesFromURL(String url)
  {
    return readLinesFromURL(url, DefaultCharset.defaultCharset);
  }
  public static ReadLineIterable readLinesFromURL(String url, Charset charset)
  {
    return new ReadLineIterable(new URLCharSource(url, charset));
  }
  
  /**
   * @deprecated Use resourceToString(String resource, Charset charset, Class<?> classForResource)
   * @param resource
   * @param charset
   * @return
   */
  public static String resourceToString(String resource, Charset charset)
  {
    return resourceToString(resource, charset, null);
  }
  
  public static String resourceToString(String resource, Charset charset, Class<?> classForResource)
  {
    return read(new ResourceCharSource(resource, charset, classForResource));
  }
  
  /**
   * @deprecated Use resourceToString(String resource, Class<?> classForResource)
   * @param resource
   * @return
   */
  public static String resourceToString(String resource)
  {
    return resourceToString(resource, DefaultCharset.defaultCharset);
  }
  
  public static String resourceToString(String resource, Class<?> classForResource)
  {
    return resourceToString(resource, DefaultCharset.defaultCharset, classForResource);
  }
  
  public static String fileToString(File file, Charset charset)
  {
    return read(new FileCharSource(file, charset));
  }
  
  public static String fileToString(File file)
  {
    return fileToString(file, DefaultCharset.defaultCharset);
  }
  
  public static String urlToString(String url, Charset charset)
  {
    return read(new URLCharSource(url, charset));
  }
  
  public static String urlToString(String url)
  {
    return urlToString(url, DefaultCharset.defaultCharset);
  }
  
  /**
   * @deprecated Use readLinesFromResource(String resource, Class<?> classForResource)
   * @param resource
   * @return
   */
  public static ReadLineIterable readLinesFromResource(String resource)
  {
    return readLinesFromResource(resource, DefaultCharset.defaultCharset, null);
  }
  /**
   * 
   * @param resource
   * @param classForResource A class preferably in the same jar as the resource you are looking for
   * @return
   */
  public static ReadLineIterable readLinesFromResource(String resource, Class<?> classForResource)
  {
    return readLinesFromResource(resource, DefaultCharset.defaultCharset, classForResource);
  }
  
  /**
   * @deprecated Use readLinesFromResource(String resource, Charset charset, Class<?> classForResource)
   * @param resource
   * @return
   */
  public static ReadLineIterable readLinesFromResource(String resource, Charset charset)
  {
    return readLinesFromResource(resource, charset, null);
  }
  
  /**
   * 
   * @param resource
   * @param charset
   * @param classForResource A class preferably in the same jar as the resource you are looking for
   * @return
   */
  public static ReadLineIterable readLinesFromResource(String resource, Charset charset, Class<?> classForResource)
  {
    return new ReadLineIterable(new ResourceCharSource(resource, charset, classForResource));
  }
  
  public static String read(CharSource charSource)
  {
    try
    {
      return charSource.read();
    } catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  private static class FileCharSource extends CharSource
  {
    private final Charset charset;
    private final File file;
    public FileCharSource(File file, Charset charset)
    {
      this.charset = charset;
      this.file = file;
    }
    @Override
    public Reader openStream() throws IOException
    {
      if (isGZip(file))
      {
        InputStream fileStream = new FileInputStream(file);
        InputStream gzipStream = new GZIPInputStream(fileStream);
        Reader decoder = new InputStreamReader(gzipStream, charset);
        return new BufferedReader(decoder);
      }
      else
        return Files.asCharSource(file, charset).openStream();
    }
  }
  
  private static boolean isGZip(File f)
  {
    return isGZip(f.getName());
  }
  private static boolean isGZip(String str) 
  {
    return str.toUpperCase().endsWith("GZ");
  }
  
  private static class ResourceCharSource extends CharSource
  {
    private final Charset charset;
    private final String url;
    private final Class<?> classForResource;
    public ResourceCharSource(String url, Charset charset, Class<?> c)
    {
      this.charset = charset;
      this.url = url;
      this.classForResource = c == null ? ResourceCharSource.class : c;
    }
    @Override
    public Reader openStream() throws IOException
    {
      InputStream resourceStream = classForResource.getResourceAsStream(url);
      if (isGZip(url)) 
      {
        InputStream gzipStream = new GZIPInputStream(resourceStream);
        Reader decoder = new InputStreamReader(gzipStream, charset);
        return new BufferedReader(decoder);
      }
      else
        return new BufferedReader(new InputStreamReader(resourceStream, charset));
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
      InputStream urlStream = new URL(url).openStream();
      if (isGZip(url)) 
      {
        InputStream gzipStream = new GZIPInputStream(urlStream);
        Reader decoder = new InputStreamReader(gzipStream, charset);
        return new BufferedReader(decoder);
      }
      else
        return new BufferedReader(new InputStreamReader(urlStream, charset));
    }
  }
  
  public static final Function<String, List<String>> splitCSV = splitCSV(new CSVParser());
  public static Function<String, List<String>> splitCSV(final CSVParser parser) 
  {
    return new Function<String, List<String>>() {
      @Override
      public List<String> apply(String input)
      {
        try { return Arrays.asList(parser.parseLine(input)); } 
        catch (IOException e) { throw new RuntimeException(e + "\nProblematic line:" + input); }
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
          throw new RuntimeException("The number of keys should have the same length as the number of values:" + size + " vs " + values.size());
        Map<String,String> result = Maps.newLinkedHashMap();
        for (int i = 0; i < size; i++)
          result.put(keys.get(i), values.get(i));
        return result;
      }
    };
  }
  
  public static void stringToFile(File f, CharSequence thingsToBeWritten)
  {
    write(f, thingsToBeWritten);
  }
  
  public static void write(File f, CharSequence thingsToBeWritten)
  {
    BufferedWriter out = writer(f);
    try {
    out.append(thingsToBeWritten);
    out.close();
    } catch (Exception e) { throw new RuntimeException(e); }
  }
  
  public static Gson createGson()
  {
    return new GsonBuilder().setPrettyPrinting().create();
  }
  
  public static BufferedWriter writer(File f)
  {
    return writer(f, DefaultCharset.defaultCharset);
  }
  
  public static BufferedWriter writer(File f, Charset charset)
  {
    try
    {
      BriefFiles.createParentDirs(f);
      OutputStream stream = new FileOutputStream(f);
      if (isGZip(f)) 
      {
        stream = new GZIPOutputStream(stream);
      }
      return new BufferedWriter(
          new OutputStreamWriter(stream, charset));
      
    } 
    catch (Exception e) { throw new RuntimeException(e); }
  }
  
  /**
   * @deprecated Use writer instead
   * @param f
   * @return
   */
  public static PrintWriter output(File f)
  {
    return output(f, DefaultCharset.defaultCharset);
  }
  
  /**
   * @deprecated Use writer instead
   * @param f
   * @param charset
   * @return
   */
  public static PrintWriter output(File f, Charset charset)
  {
    try
    {
      BriefFiles.createParentDirs(f);
      return new PrintWriter(f, charset.name());
    } catch (FileNotFoundException e)
    {
      throw new RuntimeException(e);
    } catch (UnsupportedEncodingException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public static void println(Writer out, String line)
  {
    print(out, line + "\n");
  }
  
  public static void print(Writer out, String string)
  {
    try { out.append(string); } 
    catch (IOException e) { throw new RuntimeException(e); }
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
      return splitCSV(new CSVParser(), null);
    }
    public FluentIterable<List<String>> splitCSV(CSVParser parser)
    {
      return splitCSV(parser,null);
    }
    public FluentIterable<List<String>> splitCSV(final Character commentCharacter)
    {
      return splitCSV(new CSVParser(), commentCharacter);
    }
    public FluentIterable<List<String>> splitCSV(CSVParser parser, final Character commentCharacter)
    {
      if (commentCharacter == null)
        return filter(s -> !s.matches("^\\s*$")).transform(BriefIO.splitCSV(parser));
      else
      {
        final char c = commentCharacter.charValue();
        return filter(s -> !s.matches("^\\s*$")).filter(s -> s.length() == 0 || s.charAt(0) != c).transform(BriefIO.splitCSV(parser));
      }
    }
    
    public FluentIterable<Map<String,String>> indexCSV()
    { 
      return indexCSV(new CSVParser(), null);
    }
    public FluentIterable<Map<String,String>> indexCSV(CSVParser parser)
    {
      return indexCSV(parser, null);
    }
    public FluentIterable<Map<String,String>> indexCSV(Character commentCharacter)
    {
      return indexCSV(new CSVParser(), commentCharacter);
    }
    public FluentIterable<Map<String,String>> indexCSV(CSVParser parser, Character commentCharacter)
    {
      // read the first line, by default, zero columns (to support fully empty file)
      @SuppressWarnings("unchecked")
      final List<String> header = Iterables.getFirst(splitCSV(parser, commentCharacter), Collections.EMPTY_LIST);
      
      // for the body, skip the header:
      FluentIterable<List<String>> bodyIterable = splitCSV(parser, commentCharacter).skip(1);
      
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

  private static boolean localeSet = false;
  public static void ensureUSLocale()
  {
    if (localeSet) return;
    Locale.setDefault(Locale.US);
    localeSet = true;
  }
  
  public static String prompt(String prompt) 
  {
    BufferedReader in = DefaultCharset.getReader(System.in); 
    String str = "";
    if (prompt != null)
      System.out.print(prompt + "> ");
    try { str = in.readLine(); } 
    catch (IOException e) { throw new RuntimeException(e); }
    if (str == null) return "";
    return str;
  }
  
  public static String prompt() 
  {
    return prompt(null);
  }
  
}
