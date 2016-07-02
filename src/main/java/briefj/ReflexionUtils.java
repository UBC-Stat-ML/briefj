package briefj;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

public class ReflexionUtils
{
  /*
   * Note: At some point, look at: https://github.com/EsotericSoftware/reflectasm
   */
  
  /**
   * Retrieving fields list of specified class
   * If recursively is true, retrieving fields from all class hierarchy
   *
   * @param clazz where fields are searching
   * @param recursively param
   * @return list of fields
   */
  public static List<Field> getDeclaredFields(@SuppressWarnings("rawtypes") Class clazz, boolean recursively) 
  {
    List<Field> fields = new ArrayList<Field>();
    Field[] declaredFields = clazz.getDeclaredFields();
    Collections.addAll(fields, declaredFields);

    @SuppressWarnings("rawtypes")
    Class superClass = clazz.getSuperclass();

    if(superClass != null && recursively) 
      fields.addAll(getDeclaredFields(superClass, recursively));

    return fields;
  }
  
  /**
   * Retrieving fields list of specified class and which
   * are annotated by incoming annotation class
   * If recursively is true, retrieving fields from all class hierarchy
   *
   * @param clazz - where fields are searching
   * @param annotationClass - specified annotation class
   * @param recursively param
   * @return list of annotated fields
   */
  @SuppressWarnings("rawtypes") 
  public static List<Field> getAnnotatedDeclaredFields(
      Class clazz, 
      Class<? extends Annotation> annotationClass, 
      boolean recursively) 
  {
    List<Field> allFields = getDeclaredFields(clazz, recursively);
    List<Field> annotatedFields = new ArrayList<Field>();

    for (Field field : allFields) 
      if(field.isAnnotationPresent(annotationClass))
        annotatedFields.add(field);

    return annotatedFields;
  }
  
  public static void setFieldValue(Field f, Object instance, Object value)
  {
    try
    {
      boolean isAccessible = f.isAccessible();
      if (!isAccessible)
        f.setAccessible(true);
      f.set(instance, value); 
      if (!isAccessible)
        f.setAccessible(false);
    } catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  public static Object getFieldValue(Field f, Object instance)
  {
    try
    {
      boolean isAccessible = f.isAccessible();
      if (!isAccessible)
        f.setAccessible(true);
      Object result = f.get(instance);
      if (!isAccessible)
        f.setAccessible(false);
      return result;
    } catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public static <T> T instantiate(Class<T> clazz)
  {
    try
    {
      Constructor<T> constructor = clazz.getDeclaredConstructor();
      boolean isAccessible = constructor.isAccessible();
      if (!isAccessible)
        constructor.setAccessible(true);
      T result = constructor.newInstance();
      if (!isAccessible)
        constructor.setAccessible(false);
      return result;
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public static Object callMethod(Object instance, String methodName)
  {
    try 
    {
      Method method = instance.getClass().getDeclaredMethod (methodName);
      return method.invoke (instance);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
  
  @SuppressWarnings({ "unchecked" })
  public static <T> List<T> sublistOfGivenType(List<?> originalList, Class<T> ofType)
  {
    List<T> result = Lists.newArrayList();
    for (Object variable : originalList)
      if (ofType.isAssignableFrom(variable.getClass())) // i.e. if variable extends/implements ofType
        result.add((T) variable);
    return result;
  }

}
