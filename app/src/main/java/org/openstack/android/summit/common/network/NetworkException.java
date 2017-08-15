package org.openstack.android.summit.common.network;

import java.io.IOException;

/**
 * Created by smarcet on 8/14/17.
 */

public class NetworkException extends IOException {

    public NetworkException(String message) {
        super(message);
    }
}
