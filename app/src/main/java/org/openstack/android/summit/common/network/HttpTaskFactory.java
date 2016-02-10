package org.openstack.android.summit.common.network;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.security.AccountType;
import org.openstack.android.summit.common.security.ITokenManager;
import org.openstack.android.summit.common.security.ITokenManagerFactory;
import org.openstack.android.summit.common.security.TokenManagerFactory;

import java.security.spec.InvalidParameterSpecException;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 12/15/2015.
 */
public class HttpTaskFactory implements IHttpTaskFactory {
    private ITokenManagerFactory tokenManagerFactory;
    private IHttpFactory httpFactory;

    @Inject
    public HttpTaskFactory(ITokenManagerFactory tokenManagerFactory, IHttpFactory httpFactory) {
        this.tokenManagerFactory = tokenManagerFactory;
        this.httpFactory = httpFactory;
    }

    @Override
    public HttpTask create(AccountType type, String url, String method, HttpTaskListener delegate) throws InvalidParameterSpecException {
        ITokenManager tokenManager;
        if (type == AccountType.OIDC) {
            tokenManager = tokenManagerFactory.Create(TokenManagerFactory.TokenManagerType.OIDC);
        }
        else if (type == AccountType.ServiceAccount) {
            tokenManager = tokenManagerFactory.Create(TokenManagerFactory.TokenManagerType.ServiceAccount);
        }
        else {
            throw new InvalidParameterSpecException(String.format("Account type %1 is invalid", type));
        }

        HttpTaskConfig httpTaskConfig = new HttpTaskConfig();
        httpTaskConfig.setUrl(url);
        httpTaskConfig.setMethod(method);
        httpTaskConfig.setDelegate(delegate);
        httpTaskConfig.setHttp(httpFactory.create(tokenManager));
        HttpTask httpTask = new HttpTask(httpTaskConfig);

        return httpTask;
    }
}
