package briefj.run;

import static binc.Command.call;

import java.io.File;
import java.util.Map;

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
    
    Map<String, String> argName2Path = parser.getInputFiles();
    for (String argName : argName2Path.keySet())
    {
      File path = new File(argName2Path.get(argName));
      if (!path.exists())
        return false;
      else
        try
        {
          call(Commands.ln.ranIn(inputsLinksFolder).withArgs("-s " + path.getAbsolutePath() + " " + argName));
        }
        catch (Exception e) 
        {
          BriefLog.warnOnce("Softlinks of inputs/outputs not currently supported for this architecture");
          return false;
        } 
    }
    return true;
  }

}
