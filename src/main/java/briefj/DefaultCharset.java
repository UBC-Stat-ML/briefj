package briefj;

import java.nio.charset.Charset;

import tutorialj.Tutorial;

import com.google.common.base.Charsets;



public class DefaultCharset
{
  /**
   * Charset in IO operations are optional.
   * To change the default charset, set the following field:
   */
  @Tutorial(order=4,showSource=true,showLink=true)
  public static Charset defaultCharset = Charsets.UTF_8;
}
