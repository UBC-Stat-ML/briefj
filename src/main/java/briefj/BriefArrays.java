package briefj;

import java.util.List;



public class BriefArrays
{
  /**
   * 
   * @param datum
   * @return
   */
  public static double[] parseDoublesToArray(List<String> datum)
  {
    double [] result = new double[datum.size()];
    for (int i = 0; i < datum.size(); i++)
      result[i] = Double.parseDouble(datum.get(i));
    return result;
  }
}
