package briefj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;


import com.google.common.base.Charsets;


/**
 * 
 * @author Alexandre Bouchard (alexandre.bouchard@gmail.com)
 *
 */
public class DefaultCharset
{
  public static Charset defaultCharset = Charsets.UTF_8;
  
  public static BufferedReader getReader(InputStream in)
  {
    return new BufferedReader(new InputStreamReader(in, defaultCharset));
  }
}
