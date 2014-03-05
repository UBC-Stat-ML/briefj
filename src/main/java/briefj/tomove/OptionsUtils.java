package briefj.tomove;

import fig.basic.OptionsParser;

import static briefj.tomove.ExecutionInfoFiles.*;

public class OptionsUtils
{
  public static void recordOptions(OptionsParser parser)
  {
    parser.getOptionPairs().printEasy(getFile(OPTIONS_MAP)); 
    parser.getOptionStrings().printEasy(getFile(OPTIONS_DESCRIPTIONS)); 
  }

  public static OptionsParser parseOptions(Object object, String[] args)
  {
    final OptionsParser parser = new OptionsParser();
    parser.register(object);
    if (!parser.parse(args))
    {
      throw new InvalidOptionsException();
    }
    return parser;
  }
  
  public static class InvalidOptionsException extends RuntimeException
  {
    
  }
}
