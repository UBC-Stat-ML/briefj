package briefj.opt;

import java.lang.annotation.*;

/**
 * From: https://github.com/percyliang/fig
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OptionSet {
  String name();
}

