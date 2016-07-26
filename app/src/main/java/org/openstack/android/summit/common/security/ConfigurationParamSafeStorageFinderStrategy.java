package org.openstack.android.summit.common.security;

import java.lang.reflect.Method;

/**
 * Created by sebastian on 7/22/2016.
 */
public class ConfigurationParamSafeStorageFinderStrategy implements IConfigurationParamFinderStrategy {

    private Object storage = null;
    private Class<?> cls  = null;

    public ConfigurationParamSafeStorageFinderStrategy(){

        try{
            cls                 = Class.forName("org.openstack.android.summit.safestorage.SafeStorage");
            storage             = cls.newInstance();
        }
        catch(Exception ex){
            ex.printStackTrace();
            storage = null;
        }
    }

    @Override
    public String find(String key) {
        if(storage == null) return null;
        //String parameter
        Class[] paramString = new Class[1];
        paramString[0]      = String.class;
        try{
            Method method =  cls.getDeclaredMethod("findSafeParam", paramString);
            return method.invoke(storage, key).toString();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
