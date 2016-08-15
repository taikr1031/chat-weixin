package com.zm.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DefectHelper java 反射机制工具类
 *
 * @author squall
 */
@SuppressWarnings("rawtypes")
public final class DefectHelper {
  private static final Logger LOG = LoggerFactory.getLogger(DefectHelper.class);

  private DefectHelper() {
  }

  public static Class loadClass(String className) {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    if(classLoader == null) {
      classLoader = DefectHelper.class.getClassLoader();
    }

    try {
      return classLoader.loadClass(className);
    } catch(ClassNotFoundException ex) {
      LOG.error(className + "not found.", ex);
      return null;
    }
  }

  public static Map<String, Object> getClassConstants(String className) {
    Map<String, Object> result = new HashMap<String, Object>();
    Class clazz = loadClass(className);
    if(clazz == null) {
      return result;
    }

    Field[] fields = clazz.getFields();
    for(int i = 0; i < fields.length; i++) {
      Field field = fields[i];
      int modifiers = field.getModifiers();
      if(Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers)) {
        Object value = null;
        try {
          value = field.get(null);
        } catch(Exception ex) {
          LOG.warn("Can't invoke static field[" + field.getName() + "] " + className, ex);
        }
        if(value != null) {
          result.put(field.getName(), value);
        }
      }
    }

    return result;
  }

  @SuppressWarnings("unchecked")
  public static void setFieldValue(Object target, String fname, Class ftype, Object fvalue) {
    if(target == null) {
      return;
    }
    if(StringUtils.isBlank(fname)) {
      return;
    }
    if(fvalue != null && !ftype.isAssignableFrom(fvalue.getClass())) {
      return;
    }

    Class clazz = target.getClass();
    try {
      Field field = clazz.getDeclaredField(fname);
      if(!Modifier.isPublic(field.getModifiers())) {
        field.setAccessible(true);
      }
      field.set(target, fvalue);
    } catch(Exception ex) {
      if(LOG.isDebugEnabled()) {
        LOG.debug(ex.getMessage(), ex);
      }
    }
  }

  public static Map<String, List<Method>> getMethodListMap(Class clazz, boolean isLevelKey) {
    Map<String, List<Method>> result = new HashMap<String, List<Method>>();
    Method[] methods = clazz.getMethods();
    for(Method m : methods) {
      String name = getMethodKey(m, isLevelKey);
      List<Method> methodList = result.get(name);
      if(methodList == null) {
        methodList = new ArrayList<Method>(1);
        result.put(name, methodList);
      }
      methodList.add(m);
    }
    return result;
  }

  public static String getMethodKey(Method method, boolean isLevelKey) {
    StringBuilder result = new StringBuilder();
    result.append(method.getName());
    if(isLevelKey) {
      Class[] paramTypes = method.getParameterTypes();
      for(Class paramType : paramTypes) {
        result.append("_").append(paramType.getName());
      }
    }
    return result.toString();
  }

  public static Class getSuperClassGenericType(final Class clazz) {
    return getSuperClassGenericType(clazz, 0);
  }

  public static Class getSuperClassGenericType(final Class clazz, final int index) {
    Type genType = null;
    Class superclass = clazz;

    while(superclass != null) {
      genType = superclass.getGenericSuperclass();

      if(genType instanceof ParameterizedType) {
        break;
      }

      superclass = superclass.getSuperclass();
    }

    if(!(genType instanceof ParameterizedType)) {
      LOG.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
      return Object.class;
    }

    Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

    if(index >= params.length || index < 0) {
      LOG.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
      return Object.class;
    }
    if(!(params[index] instanceof Class)) {
      LOG.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
      return Object.class;
    }

    return (Class) params[index];
  }

  public static Map<String, Object> describe(Object bean, String... ignoreProperties) throws IllegalAccessException,
      InvocationTargetException, NoSuchMethodException {
    if(bean == null) {
      return new HashMap<String, Object>();
    }

    Map<String, Object> result = new HashMap<String, Object>();
    List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;
    PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(bean);
    for(int i = 0; i < descriptors.length; i++) {
      String name = descriptors[i].getName();
      if((ignoreList != null) && (ignoreList.contains(name))) {
        continue;
      }

      if(PropertyUtils.getReadMethod(descriptors[i]) != null) {
        result.put(name, PropertyUtils.getNestedProperty(bean, name));
      }
    }
    return result;
  }

  public static Method getStaticMethod(Class clazz, String methodName) {
    Method method = null;
    Method[] methods = clazz.getMethods();
    for(int i = 0; i < methods.length; i++) {
      Method tmpMethod = methods[i];
      int modifier = tmpMethod.getModifiers();
      if(tmpMethod.getName().equals(methodName) && Modifier.isPublic(modifier) && Modifier.isStatic(modifier)) {
        method = tmpMethod;
        break;
      }
    }
    return method;
  }

  public static Field getStaticField(Class clazz, String fieldName) {
    Field field = null;
    Field[] fields = clazz.getFields();
    for(int i = 0; i < fields.length; i++) {
      Field tmpField = fields[i];
      int modifier = tmpField.getModifiers();
      if(tmpField.getName().equals(fieldName) && Modifier.isStatic(modifier) && Modifier.isPublic(modifier)) {
        field = tmpField;
        break;
      }
    }
    return field;
  }

  public static Object getStaticFieldValue(String className, String fieldName) {
    Class clazz = loadClass(className);
    if(clazz == null) {
      return null;
    }

    Field field = getStaticField(clazz, fieldName);
    if(field == null) {
      return null;
    }

    Object obj = null;
    try {
      obj = field.get(field);
    } catch(Exception ex) {
      LOG.warn("Can't invoke static field[" + fieldName + "] " + className, ex);
    }

    return obj;
  }

  /**
   * 根据反射机制调用指定对象的指定方法
   * 
   * @param obj
   *          指定对象
   * @param methodName
   *          方法名
   * @param paramsType
   *          参数列表类型
   * @param params
   *          参数列表
   * @return 对象方法的返回值
   * @throws InvocationTargetException
   * @throws IllegalAccessException
   * @throws NoSuchMethodException
   */
  public static Object invokeMethod(final Object obj, final String methodName, final Object[] params)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    return MethodUtils.invokeMethod(obj, methodName, params);
  }

  public static Object invokeStaticMethod(String className, String methodName, final Object[] params) {
    Class clazz = loadClass(className);
    if(clazz == null) {
      return null;
    }

    Method method = getStaticMethod(clazz, methodName);
    if(method == null) {
      return null;
    }

    Object obj = null;
    try {
      obj = method.invoke(method, params);
    } catch(Exception ex) {
      LOG.warn("Can't invoke static method[" + methodName + "] " + className, ex);
    }

    return obj;
  }

  public static PropertyResult getPropertyResult(Class<?> entityClass, String property) {
    String p = StringUtils.replace(property, "_", ".");
    String[] propertyNames = StringUtils.split(p, ".");

    Method readMethod = getPropertyReadMethod(entityClass, propertyNames[0]);
    if(readMethod == null) {
      return new PropertyResult(false, null);
    }

    Class<?> type = getPropertyType(readMethod);
    if(propertyNames.length > 1) {
      return getPropertyResult(type, StringUtils.replaceOnce(p, propertyNames[0] + ".", ""));
    }

    return new PropertyResult(true, type);
  }

  public static class PropertyResult {
    private boolean exist;

    private Class<?> type;

    public PropertyResult(boolean exist, Class<?> type) {
      this.exist = exist;
      this.type = type;
    }

    public boolean isExist() {
      return exist;
    }

    public Class<?> getType() {
      return type;
    }
  }

  private static Method getPropertyReadMethod(Class<?> entityClass, String property) {
    PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(entityClass);
    for(int i = 0; i < propertyDescriptors.length; i++) {
      if(property.equals(propertyDescriptors[i].getName())) {
        return propertyDescriptors[i].getReadMethod();
      }
    }
    return null;
  }

  private static Class<?> getPropertyType(Method method) {
    if(!Collection.class.isAssignableFrom(method.getReturnType())) {
      return method.getReturnType();
    }

    Type type = method.getGenericReturnType();
    if(!(type instanceof ParameterizedType)) {
      return (Class<?>) type;
    }
    
    ParameterizedType paramType = (ParameterizedType) type;
    Type[] actualTypes = paramType.getActualTypeArguments();
    for(Type aType : actualTypes) {
      if(aType instanceof Class) {
        return (Class<?>) aType;
      }
      if(aType instanceof TypeVariable) {
        GenericDeclaration d = ((TypeVariable) aType).getGenericDeclaration();
        if(d instanceof Class) {
          return (Class<?>) d;
        }
      }
    }
    return method.getReturnType();
  }
}