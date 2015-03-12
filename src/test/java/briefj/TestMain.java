package briefj;


import briefj.opt.Option;
import briefj.run.Mains;



public class TestMain implements Runnable
{
  @Option
  public String test = "asdf";

  @Override
  public void run()
  {
    System.out.println("Just testing");
  }
  
  public static void main(String [] args)
  {
    Mains.instrumentedRun(args, new TestMain());
  }
}
