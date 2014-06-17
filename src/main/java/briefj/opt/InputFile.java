package briefj.opt;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface InputFile
{
  /**
   * Should we copy the input file (default is
   * false, i.e. only create softlinks).
   * @return
   */
  public boolean copy() default false;
}
