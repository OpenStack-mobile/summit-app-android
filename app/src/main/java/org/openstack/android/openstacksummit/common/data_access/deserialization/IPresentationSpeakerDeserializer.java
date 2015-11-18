package org.openstack.android.openstacksummit.common.data_access.deserialization;

import org.json.JSONException;
import org.openstack.android.openstacksummit.common.entities.PresentationSpeaker;

/**
 * Created by Claudio Redi on 11/16/2015.
 */
public interface IPresentationSpeakerDeserializer {
    PresentationSpeaker deserialize(String jsonString) throws JSONException;
}