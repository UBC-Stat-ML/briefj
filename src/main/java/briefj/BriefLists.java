package briefj;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;



public class BriefLists
{
  public static <T extends Comparable<T>> ArrayList<T> sort(Collection<T> unsorted)
  {
    ArrayList<T> result = Lists.newArrayList(unsorted);
    Collections.sort(result);
    return result;
  }
  
  public static <T> ArrayList<T> sort(Collection<T> unsorted, Comparator<T> comparator)
  {
    ArrayList<T> result = Lists.newArrayList(unsorted);
    Collections.sort(result, comparator);
    return result;
  }
  
  public static <T> List<T> concat(List<T> list, T item)
  {
    List<T> result = Lists.newArrayList(list);
    result.add(item);
    return result;
  }
  
  public static <T> T last(List<T> list)
  {
    return list.get(list.size() - 1);
  }
  
  /**
   * 
   * @param bound
   * @return The integers [0, 1, 2, ... bound)
   */
  public static ContiguousSet<Integer> integers(int bound)
  {
    if (bound < 0)
      throw new RuntimeException("The bound should be non-negative");
    return ContiguousSet.create(Range.closedOpen(0, bound), DiscreteDomain.integers());
  }
}
