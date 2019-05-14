package org.openstack.android.summit.common.DTOs.Assembler.Converters;


/**
 * Created by Claudio Redi on 1/21/2016.
 */
public class SummitEventRealmProxy2EventDetailDTO extends AbstractSummitEvent2EventDetailDTO<io.realm.org_openstack_android_summit_common_entities_SummitEventRealmProxy, io.realm.org_openstack_android_summit_common_entities_PresentationSpeakerRealmProxy, io.realm.org_openstack_android_summit_common_entities_PresentationVideoRealmProxy> {
    public SummitEventRealmProxy2EventDetailDTO() {
        presentationSpeaker2PersonListIemDTO = new PresentationSpeakerRealmProxy2PersonListItemDTO();
        video2VideoDTO                       = new AbstractPresentationVideo2VideoDTO<io.realm.org_openstack_android_summit_common_entities_PresentationVideoRealmProxy>();
    }
}
