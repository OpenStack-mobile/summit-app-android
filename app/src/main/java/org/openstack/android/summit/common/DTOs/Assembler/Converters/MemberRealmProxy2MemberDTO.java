package org.openstack.android.summit.common.DTOs.Assembler.Converters;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public class MemberRealmProxy2MemberDTO extends AbstractMember2MemberDTO<io.realm.org_openstack_android_summit_common_entities_MemberRealmProxy, io.realm.org_openstack_android_summit_common_entities_SummitAttendeeRealmProxy, io.realm.org_openstack_android_summit_common_entities_PresentationSpeakerRealmProxy> {

    public MemberRealmProxy2MemberDTO() {
        abstractSummitAttendee2PersonDTO      = new SummitAttendeeRealmProxy2PersonDTO();
        abstractPresentationSpeaker2PersonDTO = new PresentationSpeakerRealmProxy2PersonDTO();
    }
}
