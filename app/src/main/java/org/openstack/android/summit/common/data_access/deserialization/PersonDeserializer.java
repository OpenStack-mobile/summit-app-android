package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.IPerson;

/**
 * Created by Claudio Redi on 11/16/2015.
 */
public class PersonDeserializer extends BaseDeserializer implements IPersonDeserializer {
    @Override
    public void deserialize(IPerson person, JSONObject jsonObject) throws JSONException {

        person.setFirstName(
                !jsonObject.isNull("first_name") ? jsonObject.getString("first_name") : ""
        );
        person.setLastName(
                !jsonObject.isNull("last_name") ? jsonObject.getString("last_name") : ""
        );
        person.setFullName(getFullName(person));
        person.setBio(
                !jsonObject.isNull("bio") ? jsonObject.getString("bio") : ""
        );
        person.setIrc(
                !jsonObject.isNull("irc") ? jsonObject.getString("irc") : ""
        );
        person.setPictureUrl(
                !jsonObject.isNull("pic") ? jsonObject.getString("pic") : ""
        );
        person.setTitle(
                !jsonObject.isNull("title") ? jsonObject.getString("title") : ""
        );
        person.setTwitter(
                !jsonObject.isNull("twitter") ? jsonObject.getString("twitter") : ""
        );
    }

    private String getFullName(IPerson person) {
        String fullName = null;
        if (person.getFirstName() != null && person.getLastName() != null) {
            fullName = person.getFirstName() + " " + person.getLastName();
        }
        else if (person.getFirstName() != null){
            fullName = person.getFirstName();
        }
        else  if (person.getLastName() != null){
            fullName = person.getLastName();
        }
        return fullName;
    }
}
