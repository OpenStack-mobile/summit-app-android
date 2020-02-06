package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.openstack.android.summit.common.entities.notifications.PushNotification;
import org.openstack.android.summit.common.security.ISecurityManager;

public interface IPushNotificationDeserializer {
    PushNotification deserialize(String jsonString) throws JSONException;

    void setSecurityManager(ISecurityManager securityManager);
}
