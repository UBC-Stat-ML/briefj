package briefj;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;



public class BriefMaps
{
  /**
   * Return the value mapped by the key if not null, otherwise, insert the 
   * default object and return it.
   * @param <K>
   * @param <V>
   * @param map
   * @param key
   * @param defaultValue
   * @return
   */
  public static <K,V> V getOrPut(Map<K,V> map, K key, V defaultValue)
  {
    V result = map.get(key);
    if (result == null)
    {
      result = defaultValue;
      map.put(key, result);
    }
    return result;
  }
  
  public static <K,V> Set<V> getOrPutSet(Map<K,Set<V>> map, K key)
  {
    // return getOrPut(map, key, new LinkedHashSet<V>());
    // specialized method below a bit repetitive but more efficient
    Set<V> result = map.get(key);
    if (result == null)
    {
      result = new LinkedHashSet<V>();
      map.put(key, result);
    }
    return result;
  }
  public static <K,V> List<V> getOrPutList(Map<K,List<V>> map, K key)
  {
    // return getOrPut(map, key, new ArrayList<V>());
    // specialized method below a bit repetitive but more efficient
    List<V> result = map.get(key);
    if (result == null)
    {
      result = new ArrayList<V>();
      map.put(key, result);
    }
    return result;
  }
  public static <K,K2,V> Map<K2,V> getOrPutMap(Map<K,Map<K2,V>> map, K key)
  {
    // return getOrPut(map, key, new LinkedHashMap<K2,V>());
    // specialized method below a bit repetitive but more efficient
    Map<K2,V> result = map.get(key);
    if (result == null)
    {
      result = new LinkedHashMap<K2,V>();
      map.put(key, result);
    }
    return result;
  }
}
