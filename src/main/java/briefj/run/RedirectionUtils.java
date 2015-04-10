package briefj.run;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.commons.io.output.TeeOutputStream;





public class RedirectionUtils
{
  public static Tees createTees(File folder)
  {
    Tees result = new Tees();
    result.out = createTee(new File(folder, ExecutionInfoFiles.STD_OUT_FILE), true);
    result.err  = createTee(new File(folder, ExecutionInfoFiles.STD_ERR_FILE), false);
    return result;
  }
  
  public static class Tees
  {
    FileOutputStream out, err;
    private Tees() {}
    public void close()
    {
      try
      {
        out.close();
        err.close();
      } catch (IOException e) {}
    }
  }
  
  public static FileOutputStream createTee(File f, boolean stdOut)
  {
    try 
    {
      FileOutputStream fos = new FileOutputStream(f);
      TeeOutputStream myOut = new TeeOutputStream(stdOut ? System.out : System.err, fos);
      PrintStream ps = new PrintStream(myOut);
      if (stdOut)
        System.setOut(ps);
      else
        System.setErr(ps);
      return fos;
    } 
    catch (Exception e) 
    {
      throw new RuntimeException(e);
    }
  }
}
