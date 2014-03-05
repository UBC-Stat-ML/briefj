package briefj;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Lists;



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
  
  public static <T> T last(List<T> list)
  {
    return list.get(list.size() - 1);
  }
}
