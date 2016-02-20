package org.openstack.android.summit.common.network;

import org.openstack.android.summit.common.security.AccountType;

import java.security.spec.InvalidParameterSpecException;

/**
 * Created by Claudio Redi on 12/15/2015.
 */
public interface IHttpTaskFactory {
    HttpTask create(AccountType type, String url, String method, String contentType, String content, HttpTaskListener delegate) throws InvalidParameterSpecException;
}
