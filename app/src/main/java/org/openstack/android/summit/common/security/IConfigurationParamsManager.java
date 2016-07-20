package org.openstack.android.summit.common.security;

import java.security.InvalidParameterException;

/**
 * Created by sebastian on 7/20/2016.
 */
public interface IConfigurationParamsManager {

    public String findConfigParamBy(String key) throws InvalidParameterException;
}
