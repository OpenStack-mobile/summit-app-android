package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.DataUpdate;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.IEntity;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Presentation;
import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmObject;

/**
 * Created by Claudio Redi on 11/4/2015.
 */

public class Deserializer implements IDeserializer {
    IGenericDeserializer genericDeserializer;
    IFeedbackDeserializer feedbackDeserializer;
    IMemberDeserializer memberDeserializer;
    IPresentationDeserializer presentationDeserializer;
    IPresentationSpeakerDeserializer presentationSpeakerDeserializer;
    ISummitAttendeeDeserializer summitAttendeeDeserializer;
    ISummitDeserializer summitDeserializer;
    ISummitEventDeserializer summitEventDeserializer;

    public Deserializer()
    {
    }

    @Inject
    public Deserializer(IGenericDeserializer genericDeserializer,
                        IFeedbackDeserializer feedbackDeserializer,
                        IMemberDeserializer memberDeserializer,
                        IPresentationDeserializer presentationDeserializer,
                        IPresentationSpeakerDeserializer presentationSpeakerDeserializer,
                        ISummitAttendeeDeserializer summitAttendeeDeserializer,
                        ISummitDeserializer summitDeserializer,
                        ISummitEventDeserializer summitEventDeserializer)
    {
        this.genericDeserializer = genericDeserializer;
        this.feedbackDeserializer = feedbackDeserializer;
        this.memberDeserializer = memberDeserializer;
        this.presentationDeserializer = presentationDeserializer;
        this.presentationSpeakerDeserializer = presentationSpeakerDeserializer;
        this.summitAttendeeDeserializer = summitAttendeeDeserializer;
        this.summitDeserializer = summitDeserializer;
        this.summitEventDeserializer = summitEventDeserializer;
    }

    @Override
    public <T extends RealmObject & IEntity> T deserialize(String jsonString, Class<T> type) throws JSONException, IllegalArgumentException {

        if (type == Feedback.class) {
            return (T)feedbackDeserializer.deserialize(jsonString);
        }
        else if (type == Member.class) {
            return (T)memberDeserializer.deserialize(jsonString);
        }
        else if (type == Presentation.class) {
            return (T)presentationDeserializer.deserialize(jsonString);
        }
        else if (type == PresentationSpeaker.class) {
            return (T)presentationSpeakerDeserializer.deserialize(jsonString);
        }
        else if (type == SummitAttendee.class) {
            return (T)summitAttendeeDeserializer.deserialize(jsonString);
        }
        else if (type == Summit.class) {
            return (T)summitDeserializer.deserialize(jsonString);
        }
        else if (type == SummitEvent.class) {
            return (T)summitEventDeserializer.deserialize(jsonString);
        }
        else {
            return genericDeserializer.deserialize(jsonString, type);
        }
    }

    public <T extends RealmObject & IEntity> List<T> deserializeList(String jsonString, Class<T> type) throws JSONException, IllegalArgumentException {
        JSONArray jsonArray = new JSONArray(jsonString);
        JSONObject jsonObject;
        List<T> list = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            list.add(deserialize(jsonObject.toString(), type));
        }
        return list;
    }

    public <T extends RealmObject & IEntity> List<T> deserializePage(String jsonString, Class<T> type) throws JSONException, IllegalArgumentException {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        List<T> list = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            list.add(deserialize(jsonObject.toString(), type));
        }
        return list;
    }

}
