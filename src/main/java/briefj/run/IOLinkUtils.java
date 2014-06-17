package briefj.run;

import static binc.Command.call;

import static briefj.run.Commands.*;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import briefj.BriefLog;
import briefj.opt.OptionsParser;

public class IOLinkUtils
{

  /**
   * 
   * @param parser
   * @return Success status
   */
  public static boolean createIOLinks(OptionsParser parser)
  {
    File inputsLinksFolder = ExecutionInfoFiles.getFile(ExecutionInfoFiles.INPUT_LINKS_FOLDER);
    inputsLinksFolder.mkdir();
    
    Map<String, Pair<String,Boolean>> argName2Path = parser.getInputFiles();
    for (String argName : argName2Path.keySet())
    {
      Pair<String,Boolean> current = argName2Path.get(argName);
      File path = new File(current.getLeft());
      if (!path.exists())
        return false;
      else
        try
        {
          if (current.getRight())
            call(cp.ranIn(inputsLinksFolder).withArgs("-r").appendArg(path.getAbsolutePath()).appendArg(argName));
          else
            call(ln.ranIn(inputsLinksFolder).withArgs("-s").appendArg(path.getAbsolutePath()).appendArg(argName));
        }
        catch (Exception e) 
        {
          BriefLog.warnOnce((current.getRight() ? "Copy" : "Softlinks") + " of inputs/outputs not currently supported for this architecture");
          return false;
        } 
    }
    return true;
  }

}
