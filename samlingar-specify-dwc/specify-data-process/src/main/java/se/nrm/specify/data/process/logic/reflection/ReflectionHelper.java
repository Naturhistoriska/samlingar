package se.nrm.specify.data.process.logic.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method; 
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j; 
import se.nrm.specify.data.model.EntityBean;

/**
 *
 * @author idali
 */
@Slf4j
public class ReflectionHelper {

    private final String DATATYPE_LIST = "java.util.List";
    private final String DATATYPE_SET = "java.util.Set";

    private final String getString = "get";
    private final String emptySpace = " ";
    private StringBuilder sb;

    private static ReflectionHelper instance = null;

    public static ReflectionHelper getInstance() {
        synchronized (ReflectionHelper.class) {
            if (instance == null) {
                instance = new ReflectionHelper();
            }
        }
        return instance;
    }

    public String getStringValueFromMultipleFields(EntityBean bean, List<String> fields) {
        sb = new StringBuilder();
        fields.stream()
                .forEach(f -> {
                    Object obj = getFieldValue(bean, f);
                    if (obj != null) {
                        sb.append(String.valueOf(obj));
                        sb.append(emptySpace);
                    }
                });
        return sb.toString().trim();
    }

    public Object getValueFromFieldOrMethod(EntityBean bean, String fieldName) {
        return getFieldValue(bean, fieldName);
    }

    public Object getFieldValue(EntityBean bean, String fieldName) {
//    log.info("getObjectValue: {} -- {}", bean.getClass().getName(), fieldName);
        try {
            Field field = bean.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(bean);
        } catch (NoSuchFieldException | SecurityException 
                | IllegalArgumentException | IllegalAccessException ex) {
//      log.error(ex.getMessage());
            return getMethodValue(bean, fieldName);
        }
    }

    public Object getMethodValue(EntityBean bean, String fieldName) {

        try {
            Method m = bean.getClass().getMethod(getString + fieldName);
            return m.invoke(bean);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException 
                | IllegalArgumentException | InvocationTargetException ex) {
//      log.error(ex.getMessage()); 
        }
        return null;
    }

    public EntityBean getChildFromParent(EntityBean parent, String childName) {
        return (EntityBean) getFieldValue(parent, childName);
    }

    public List<EntityBean> getChildListFromParent(EntityBean parent, String childName) {
        return isList(parent.getClass(), childName) ? (List<EntityBean>) getFieldValue(parent, childName)
                : ((Set<EntityBean>) getFieldValue(parent, 
                        childName)).stream().collect(Collectors.toList());
    }

    public boolean isCollection(Class clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName).getType().getName().equals(DATATYPE_SET)
                    || clazz.getDeclaredField(fieldName).getType().getName().equals(DATATYPE_LIST);
        } catch (NoSuchFieldException e) {
            Class superClass = clazz.getSuperclass();
            if (superClass == null) {
//                throw new DinaException(ErrorMsg.getInstance()
//                        .getFieldNotExist(clazz.getSimpleName(), fieldName), 400);
                return false;
            } else {
                return isCollection(superClass, fieldName);
            }
        }
    }

    /**
     * Checks if the field is a collection
     *
     * @param clazz
     * @param fieldName
     * @return
     */
    public boolean isList(Class clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName).getType().getName().equals(DATATYPE_LIST);
        } catch (NoSuchFieldException e) {
            Class superClass = clazz.getSuperclass();
            if (superClass == null) {
//                throw new DinaException(ErrorMsg.getInstance()
//                        .getFieldNotExist(clazz.getSimpleName(), fieldName), 400);
                return false;
            } else {
                return isList(superClass, fieldName);
            }
        }
    }
}
