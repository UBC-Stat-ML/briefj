package briefj.run;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;





public class LoadedClassUtils
{
  public static Collection<String> extractAllClassNames(Object o)  
  {
    ClassLoader appLoader = ClassLoader.getSystemClassLoader();
    ClassLoader currentLoader = o.getClass().getClassLoader();
    ClassLoader[] loaders = new ClassLoader[] { appLoader, currentLoader };
    final Class< ?>[] classes = ClassScope.getLoadedClasses(loaders);
    Set<String> names = Sets.newLinkedHashSet();
    for (Class c : classes)
    {
      String current = c.getCanonicalName();
      if (current != null && current.length() > 0)
        names.add(current);
    }
    List<String> sorted = Lists.newArrayList(names);
    Collections.sort(sorted);
    return sorted;
}
}
