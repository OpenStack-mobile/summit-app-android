package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.openstack.android.summit.common.entities.PushNotification;

/**
 * Created by sebastian on 8/20/2016.
 */
public interface IPushNotificationDeserializer {

    PushNotification deserialize(String jsonString) throws JSONException;
}
