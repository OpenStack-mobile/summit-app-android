package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.entities.notifications.PushNotification;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.R;
import java.util.Date;

public class PushNotificationDeserializer
        extends BaseDeserializer
        implements IPushNotificationDeserializer {

    private ISecurityManager securityManager;

    public PushNotificationDeserializer(){
    }

    @Override
    public PushNotification deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        int id = jsonObject.getInt("id");

        PushNotification notification = RealmFactory.getSession().where(PushNotification.class).equalTo("id", id).findFirst();

        if(notification == null)
            notification = RealmFactory.getSession().createObject(PushNotification.class, id);

        notification.setBody(
            !jsonObject.isNull("message") ? jsonObject.getString("message") : null
        );

        notification.setChannel(
            !jsonObject.isNull("channel") ? jsonObject.getString("channel") : null
        );

        notification.setCreatedAt(
            new Date(jsonObject.optLong("created") * 1000L)
        );

        notification.setTitle(
            !jsonObject.isNull("title") ? jsonObject.getString("title") : OpenStackSummitApplication.context.getString(R.string.push_notification_default_title)
        );

        if(jsonObject.has("event")){
            JSONObject jsonObjectEvent = jsonObject.getJSONObject("event");
            int eventId = jsonObjectEvent.getInt("id");
            SummitEvent summitEvent = RealmFactory.getSession().where(SummitEvent.class).equalTo("id", eventId).findFirst();

            if(summitEvent != null){
                notification.setEvent(summitEvent);
                notification.setTitle(summitEvent.getName());
            }
        }

        if(jsonObject.has("summit_id")){
            int summitId = jsonObject.getInt("summit_id");
            Summit summit = RealmFactory.getSession().where(Summit.class).equalTo("id", summitId).findFirst();

            if(summit != null){
                notification.setSummit(summit);
            }
        }

        notification.clearOwner();

        if(this.securityManager.isLoggedIn()){
            notification.setOwner(this.securityManager.getCurrentMember());
        }

        return notification;
    }

    @Override
    public void setSecurityManager(ISecurityManager securityManager) {
        this.securityManager = securityManager;
    }
}
