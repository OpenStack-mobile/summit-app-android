package org.openstack.android.summit.common.network;

import java.io.IOException;

/**
 * Created by Claudio Redi on 12/9/2015.
 */
public interface IHttp {
    String GET(String url) throws IOException;
}
