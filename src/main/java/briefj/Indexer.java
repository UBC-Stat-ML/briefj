package briefj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * A bijection between integers {0, 1, 2, ...} and objects of type T.
 * 
 * Backed by an array and a hashtable for efficient access in each direction
 * of the bijection.
 * 
 * @author Alexandre Bouchard (alexandre.bouchard@gmail.com)
 *
 * @param <T> The type of the indexed objects
 */
public final class Indexer<T> implements Serializable
{
  private static final long serialVersionUID = 1L;
  private final List<T> i2o = new ArrayList<T>();
  private final Map<T, Integer> o2i = new LinkedHashMap<T, Integer>();
  
  /**
   * 
   * @return The set of indexed object (unmodifiable)
   */
  public Set<T> objects()
  {
    return Collections.unmodifiableSet(o2i.keySet());
  }
  public List<T> objectsList()
  {
    return Collections.unmodifiableList(i2o);
  }
  public Indexer() {}
  
  /**
   * Sugar into creating and calling addAllToIndex(Collection)
   * @param coll
   */
  public Indexer(Collection<T> coll)
  {
    addAllToIndex(coll);
  }
  

  /**
   * Throws IndexOutOfBounds if no such index exists
   * @param i An index
   * @return The object corresponding to this index.
   */
  public T i2o(int i) 
  { 
    return i2o.get(i);
  }
  /**
   * Throws NoSuchElementException if no such element is indexed
   * @param o An object.
   * @return The index of this object.
   */
  public int o2i(T o)
  {
    Integer index = o2i.get(o);
    if (index == null) 
      throw new NoSuchElementException("" + o);
    return index;
  }
  
  /**
   * See o2i, only the behavior when the object is not there is different.
   * @param o
   * @return The index, or -1 if not in the set of indexed objects.
   */
  public int o2iEasy(T o)
  {
    Integer index = o2i.get(o);
    if (index == null) return -1;
    return index;
  }
  
  /**
   * Add all the items one after the other in this indexer.
   * @param os
   */
  public void addAllToIndex(Collection<T> os)
  {
    for (T o : os) 
      if (!containsObject(o))
      {
        i2o.add(o);
        o2i.put(o, i2o.size() - 1);
      }
  }
  
  /**
   * Add all the items one after the other in this indexer.
   * @param os
   */
  public void addToIndex(@SuppressWarnings("unchecked") T... os)
  {
    addAllToIndex(Arrays.asList(os));
  }
  
  /**
   * 
   * @param i
   * @return Whether the provided index is in the list of indices.
   */
  public boolean containsIndex(int i) 
  { 
    return (i >= 0) && (i < i2o.size()); 
  }
  
  /**
   * 
   * @param o
   * @return
   */
  public boolean containsObject(T o) 
  { 
    return o2i.containsKey(o); 
  }
  
  /**
   * 
   * @return The number of indexed objects.
   */
  public int size() 
  { 
    return i2o.size(); 
  }
  
  /**
   * 
   */
  @Override 
  public String toString() 
  { 
    return i2o.toString(); 
  }
  
  /**
   * 
   */
  @Override 
  public int hashCode() 
  { 
    return i2o.hashCode(); 
  }
  
  /**
   * 
   */
  @SuppressWarnings("rawtypes")
  @Override 
  public boolean equals(Object o)
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
