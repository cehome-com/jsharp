package jsharp.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import jsharp.sql.Entity;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class EntityMethodInterceptor implements MethodInterceptor, java.io.Serializable {
    private Set<String> properties = new HashSet<String>();

    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 设置监控有效性
     *
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<String> getProperties() {
        return properties;
    }

    public void clearProperties() {
        properties.clear();
    }

    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Object result = proxy.invokeSuper(obj, args);
        String name = method.getName();

        if (isEnabled() && name.length() > 3 && name.startsWith("set") && args.length == 1 && method.getReturnType() == Void.TYPE)
        // if (name.startsWith("set"))
        {
            //System.out.println("--------"+this+" "+obj+" "+name);
            String prop = fix(name);
            //.substring(3).toLowerCase());
            if (obj instanceof Entity) {
                //if(!Entity.DYN_F_NAME_SET.equals(prop)) {
                properties.add(prop);
                ((Entity) obj).removeSqlValue(prop);
                //}
                /*if(((Entity)obj).sqlValueMap()!=null) {
					((Entity) obj).sqlValueMap().remove(prop);
				}*/
            } else {
                properties.add(prop);
            }
        }

        return result;
    }

    private static String fix(String name) {
        return Character.toLowerCase(name.charAt(3)) + name.substring(4);
    }


    public static EntityMethodInterceptor getEntityMethodInterceptor(Object entity) {
        if (Enhancer.isEnhanced(entity.getClass()))
        //if (entity.getClass().getName().indexOf("$$EnhancerByCGLIB$$") > 0)
        {

            try {
                return (EntityMethodInterceptor) entity.getClass()
                        .getDeclaredMethod("getCallback", new Class[]{Integer.TYPE})
                        .invoke(entity, new Object[]{new Integer(0)});
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        return null;
    }


}