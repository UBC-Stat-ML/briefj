package briefj;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;



public class BriefCollections
{
  public static <T> T pick(Collection<T> coll) { return coll.size() == 0 ? null : coll.iterator().next(); }

  public static <K,V> void putNoOverwrite(Map<K, V> map,
      K key, V value)
  {
    if (map.containsKey(key))
      throw new RuntimeException("Unexpected situation in this context: putting key that already existed in the map.");
    map.put(key, value);
  }
  
  public static <T> boolean intersects(final Set<T> s1, final Set<T> s2)
  {
    return !Collections.disjoint(s1,s2);
  }
}
