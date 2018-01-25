package jsharp.test;

import jsharp.util.EntityUtils;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

public class fsafsa {
    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Document document= EntityUtils.create(Document.class);
        PropertyUtils.setProperty(document,"title","aaa");
        PropertyUtils.setProperty(document,"createTime",new Date());
    }
}
