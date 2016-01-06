package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.IEntity;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Presentation;
import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;

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
        if (type == Member.class) {
            return (T)memberDeserializer.deserialize(jsonString);
        }
        if (type == Presentation.class) {
            return (T)presentationDeserializer.deserialize(jsonString);
        }
        if (type == PresentationSpeaker.class) {
            return (T)presentationSpeakerDeserializer.deserialize(jsonString);
        }
        if (type == SummitAttendee.class) {
            return (T)summitAttendeeDeserializer.deserialize(jsonString);
        }
        if (type == Summit.class) {
            return (T)summitDeserializer.deserialize(jsonString);
        }
        if (type == SummitEvent.class) {
            return (T)summitEventDeserializer.deserialize(jsonString);
        }
        else {
            return genericDeserializer.deserialize(jsonString, type);
        }
    }
}
