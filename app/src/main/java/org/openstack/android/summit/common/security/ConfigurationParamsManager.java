package org.openstack.android.summit.common.security;

import java.security.InvalidParameterException;
import java.util.HashMap;

/**
 * Created by sebastian on 7/20/2016.
 */
public class ConfigurationParamsManager implements IConfigurationParamsManager {

    protected IConfigurationParamFinderStrategy[] finderStrategies;
    protected HashMap<String,String> cache;

    public ConfigurationParamsManager(IConfigurationParamFinderStrategy[] finderStrategies) {
        this.finderStrategies = finderStrategies;
        cache = new HashMap<>();
    }

    @Override
    public String findConfigParamBy(String key) throws InvalidParameterException {
        if(cache.containsKey(key)) return cache.get(key);
        for (int i = 0; i < this.finderStrategies.length; i++) {
            String value = this.finderStrategies[i].find(key);
            if (value != null && !value.isEmpty()) {
                cache.put(key, value);
                return value;
            }
        }
        throw new InvalidParameterException(String.format("Parameter %s not found!", key));
    }
}
