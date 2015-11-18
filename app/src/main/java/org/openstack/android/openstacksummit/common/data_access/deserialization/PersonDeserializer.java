package org.openstack.android.openstacksummit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.openstacksummit.common.entities.IPerson;

/**
 * Created by Claudio Redi on 11/16/2015.
 */
public class PersonDeserializer extends BaseDeserializer implements IPersonDeserializer {
    @Override
    public void deserialize(IPerson person, JSONObject jsonObject) throws JSONException {
        person.setId(jsonObject.getInt("id"));
        //person.setEmail(jsonObject.getString("email"));
        person.setFirstName(jsonObject.getString("first_name"));
        person.setLastName(jsonObject.getString("last_name"));
        person.setBio(
                !jsonObject.isNull("bio") ? jsonObject.getString("bio") : null
        );

        person.setIrc(
                !jsonObject.isNull("irc") ? jsonObject.getString("irc") : null
        );
        person.setMemberId(
                !jsonObject.isNull("member_id") ? jsonObject.getInt("member_id") : null
        );
        person.setPictureUrl(
                !jsonObject.isNull("pic") ? jsonObject.getString("pic") : null
        );
        person.setTitle(
                !jsonObject.isNull("title") ? jsonObject.getString("title") : null
        );
        person.setTwitter(
                !jsonObject.isNull("twitter") ? jsonObject.getString("twitter") : null
        );
    }
}
