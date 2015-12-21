package org.openstack.android.summit.common.network;

import org.openstack.android.summit.common.security.AccountType;

import java.security.spec.InvalidParameterSpecException;

/**
 * Created by Claudio Redi on 12/15/2015.
 */
public interface IHttpTaskFactory {
    HttpTask Create(AccountType type, String url, String method, HttpTaskListener delegate) throws InvalidParameterSpecException;
}
