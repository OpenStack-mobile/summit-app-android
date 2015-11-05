package org.openstack.android.openstacksummit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.openstacksummit.common.entities.Company;
import org.openstack.android.openstacksummit.common.entities.EventType;
import org.openstack.android.openstacksummit.common.entities.IEntity;
import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/4/2015.
 */
@SuppressWarnings("unchecked")
public class Deserializer {
    @Inject
    ICompanyDeserializer companyDeserializer;
    @Inject
    IEventTypeDeserializer eventTypeDeserializer;

    @Inject
    public Deserializer() {}

    public IEntity deserialize(JSONObject jsonObject, Class type) throws JSONException, IllegalArgumentException {

        if (type == Company.class) {
            return companyDeserializer.deserialize(jsonObject);
        }
        else if (type == EventType.class) {
            return eventTypeDeserializer.deserialize(jsonObject);
        }
        else {
            throw new IllegalArgumentException();
        }
    }
}
