package org.openstack.android.openstacksummit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.openstacksummit.common.entities.*;

import javax.inject.Inject;

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
    public <T extends IEntity> T deserialize(String jsonString, Class<T> type) throws JSONException, IllegalArgumentException {

        if (type == Feedback.class) {
            return (T)feedbackDeserializer.deserialize(jsonString);
        }
        else {
            return genericDeserializer.deserialize(jsonString, type);
        }
    }
}
