package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitWIFIConnection;
import org.openstack.android.summit.common.utils.RealmFactory;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 2/9/2016.
 */
public class WifiConnectionDeserializer extends BaseDeserializer implements IWifiConnectionDeserializer {

    @Inject
    public WifiConnectionDeserializer(){

    }

    @Override
    public SummitWIFIConnection deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        String[] missedFields = validateRequiredFields(new String[] {"id", "ssid", "password", "summit_id"},  jsonObject);
        handleMissedFieldsIfAny(missedFields);

        int wifiConnectionId = jsonObject.getInt("id");
        int summitId         = jsonObject.optInt("summit_id");

        SummitWIFIConnection wifiConnection = RealmFactory.getSession().where(SummitWIFIConnection.class).equalTo("id", wifiConnectionId).findFirst();
        if(wifiConnection == null)
            wifiConnection = RealmFactory.getSession().createObject(SummitWIFIConnection.class, wifiConnectionId);

        wifiConnection.setSsid(jsonObject.getString("ssid"));
        wifiConnection.setPassword(jsonObject.getString("password"));
        wifiConnection.setDescription(jsonObject.getString("description"));

        //first check db, and then cache storage
        Summit summit  =  RealmFactory.getSession().where(Summit.class).equalTo("id", summitId).findFirst();
        if(summit == null)
            throw new JSONException(String.format("Can't deserialize wifiConnection id %d (summit not found)!", wifiConnectionId));

        wifiConnection.setSummit(summit);

        return wifiConnection;
    }

}
