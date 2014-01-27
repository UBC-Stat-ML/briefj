package briefj;



public class BriefMath
{
  public static double THRESHOLD = 1e-5;
  
  /**
   * 
   * @param n1
   * @param n2
   */
  public static void checkCloseAndFinite(double n1, double n2)
  {
    checkFinite(n1);
    checkFinite(n2);
    if (Math.abs(n1 - n2) > THRESHOLD)
      throw new RuntimeException();
  }
  
  /**
   * 
   * @param n
   */
  public static void checkFinite(double n)
  {
    if (Double.isInfinite(n) || Double.isNaN(n))
      throw new RuntimeException();
  }
  
  /**
   * Check the double value is actually an int
   * throw an exception if not, or return the int
   * value otherwise
   * @param value
   * @return
   */
  public static int getAndCheckInt(double value)
  {
    if (Math.floor(value) != value)
      throw new RuntimeException();
    return (int) value;
  }
}
