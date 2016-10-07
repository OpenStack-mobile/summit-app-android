package org.openstack.android.summit.common.data_access.deserialization;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.PushNotification;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.utils.RealmFactory;

import java.util.Date;

import javax.inject.Inject;

/**
 * Created by sebastian on 8/20/2016.
 */
public class PushNotificationDeserializer extends BaseDeserializer implements IPushNotificationDeserializer {

    @Inject
    public PushNotificationDeserializer(){

    }

    @Override
    public PushNotification deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        String[] missedFields = validateRequiredFields(new String[]{"id" }, jsonObject);

        if (missedFields.length > 0) {
            throw new JSONException("Following fields are missed " + TextUtils.join(",", missedFields));
        }

        PushNotification pushNotification =  RealmFactory.getSession().createObject(PushNotification.class);
        pushNotification.setReceived(new Date());
        pushNotification.setOpened(false);
        pushNotification.setId(jsonObject.getInt("id"));
        pushNotification.setSubject(jsonObject.optString("title","OpenStack Summit Notification"));
        pushNotification.setBody(jsonObject.optString("alert",""));
        pushNotification.setType(jsonObject.optString("type",""));
        pushNotification.setEventId(jsonObject.optInt("event_id",0));


        int summitId  = jsonObject.optInt("summit_id", 0);
        Summit summit = RealmFactory.getSession().where(Summit.class).equalTo("id", summitId).findFirst();
        if(summit != null){
            pushNotification.setSummit(summit);
        }

        /*Member currentMember =  securityManager.getCurrentMember();
        if( currentMember != null){
            pushNotification.setOwner(currentMember);
        }*/

        return pushNotification;

    }
}
