package briefj.tomove;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import binc.Command;
import briefj.BriefIO;
import briefj.BriefLog;
import briefj.opt.OptionsParser;

import com.google.common.collect.Lists;


import static binc.Command.*;

public class IOLinkUtils
{

  /**
   * 
   * @param parser
   * @return List of missing arguments
   */
  public static Set<String> createIOLinks(OptionsParser parser)
  {
    throw new RuntimeException();
//    File inputsLinksFolder = ExecutionInfoFiles.getFile(ExecutionInfoFiles.INPUT_LINKS_FOLDER);
//    inputsLinksFolder.mkdir();
//    
//    List<File> missingInputs = Lists.newArrayList();
//    
//    Map<String, String> argName2Path = parser.getInputFiles();
//    for (String argName : argName2Path.keySet())
//    {
//      File path = new File(argName2Path.get(argName));
//      if (!path.exists())
//        missingInputs.add(path);
//      else
//        try
//        {
//          call(Commands.ln.ranIn(inputsLinksFolder).withArgs("-s " + path.getAbsolutePath() + " " + argName));
//        }
//        catch (Exception e) 
//        {
//          BriefLog.warnOnce("Softlinks of inputs/outputs not currently supported for this architecture");
//        } 
//    }
//    return missingInputs;
  }

}
