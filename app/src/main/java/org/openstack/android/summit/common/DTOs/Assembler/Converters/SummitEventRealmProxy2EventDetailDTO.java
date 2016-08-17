package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import org.openstack.android.summit.common.entities.PresentationVideo;

import io.realm.PresentationSpeakerRealmProxy;
import io.realm.PresentationVideoRealmProxy;
import io.realm.SummitEventRealmProxy;

/**
 * Created by Claudio Redi on 1/21/2016.
 */
public class SummitEventRealmProxy2EventDetailDTO extends AbstractSummitEvent2EventDetailDTO<SummitEventRealmProxy, PresentationSpeakerRealmProxy, PresentationVideoRealmProxy> {
    public SummitEventRealmProxy2EventDetailDTO() {
        presentationSpeaker2PersonListIemDTO = new PresentationSpeakerRealmProxy2PersonListItemDTO();
        video2VideoDTO                       = new AbstractPresentationVideo2VideoDTO<PresentationVideoRealmProxy>();
    }
}
