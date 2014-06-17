package briefj.unix;

import java.util.List;

import com.google.common.base.Joiner;

import binc.Command;




public class RemoteUtils
{
  public static Command ssh = Command.cmd("ssh");
  
  public static String remoteBash(String host, String script)
  {
    return ssh
      .withArg(host).appendArgs("/bin/bash -s")
      .throwOnNonZeroReturnCode()
      .callWithInputStreamContents(script);
  }
  
  public static String remoteBash(String host, List<String> script)
  {
    return remoteBash(host, Joiner.on("\n").join(script));
  }
}
