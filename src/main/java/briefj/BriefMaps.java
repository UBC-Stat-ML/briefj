package briefj;

import java.util.ArrayList;
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
    return getOrPut(map, key, new LinkedHashSet<V>());
  }
  public static <K,V> List<V> getOrPutList(Map<K,List<V>> map, K key)
  {
    return getOrPut(map, key, new ArrayList<V>());
  }
}
