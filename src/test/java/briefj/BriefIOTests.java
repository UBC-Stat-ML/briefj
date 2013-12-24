package briefj;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static briefj.BriefIO.*;

public class BriefIOTests
{
  public static void main(String [] args) throws IOException
  {
    
//    for (Map<String,String> line : readLines("/Users/bouchard/temp/test.csv").check().indexCSV())
//      System.out.println(line);
    
    for (List<String> line : readLinesFromURL("http://www.stat.ubc.ca/~bouchard/pub/geyser.csv").splitCSV().skip(1))
      System.out.println(line);
    
    for (String line : readLinesFromResource("/test.csv"))
      System.out.println(line);
  }
}
