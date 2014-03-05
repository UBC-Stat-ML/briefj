package briefj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public final class Indexer<T> implements Serializable
{
  private static final long serialVersionUID = 1L;
  private final List<T> i2o = new ArrayList<T>();
  private final Map<T, Integer> o2i = new HashMap<T, Integer>();
  
  public Set<T> objects()
  {
    return Collections.unmodifiableSet(o2i.keySet());
  }
  public Indexer() {}
  public Indexer(Collection<T> coll)
  {
    addAllToIndex(coll);
  }
  

  /**
   * Throws IndexOutOfBounds if no such index exists
   * @param i
   * @return
   */
  public T i2o(int i) 
  { 
    return i2o.get(i);
  }
  /**
   * Throws NoSuchElementException if no such element is indexed
   * @param o
   * @return
   */
  public int o2i(T o)
  {
    Integer index = o2i.get(o);
    if (index == null) 
      throw new NoSuchElementException("" + o);
    return index;
  }
  
  public int o2iEasy(T o)
  {
    Integer index = o2i.get(o);
    if (index == null) return -1;
    return index;
  }
  public void addAllToIndex(Collection<T> os)
  {
    for (T o : os) 
      if (!containsObject(o))
      {
        i2o.add(o);
        o2i.put(o, i2o.size() - 1);
      }
  }
  public void addToIndex(T... os)
  {
    addAllToIndex(Arrays.asList(os));
  }
  public boolean containsIndex(int i) { return (i >= 0) && (i < i2o.size()); }
  public boolean containsObject(T o) { return o2i.containsKey(o); }
  
  public int size() { return i2o.size(); }
  @Override public String toString() { return i2o.toString(); }
  @Override public int hashCode() { return i2o.hashCode(); }
  @Override public boolean equals(Object o)
  {
    if (this == o)
      return true; // for performance
    if (o == null)
      return false;
    if (!(o instanceof Indexer))
      return false;
    final Indexer o_cast = (Indexer) o;
    // check i2o
    if (!i2o.equals(o_cast.i2o))
      return false;
    return true;
  }
}
