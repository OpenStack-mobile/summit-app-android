package org.openstack.android.summit.common.DTOs.Assembler;

import org.modelmapper.ModelMapper;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.Feedback2FeedbackDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.FeedbackRealmProxy2FeedbackDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.Member2MemberDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.MemberRealmProxy2MemberDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.PresentationSpeaker2PersonDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.PresentationSpeaker2PersonListIemDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.PresentationSpeakerRealmProxy2PersonDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.PresentationSpeakerRealmProxy2PersonListItemDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.PushNotification2PushNotificationListItemDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.PushNotificationRealmProxy2PushNotificationDetailDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.PushNotificationRealmProxy2PushNotificationListItemDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.SummitAttendee2PersonDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.SummitAttendeeRealmProxy2PersonDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.SummitEvent2EventDetailDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.SummitEvent2ScheduleItemDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.SummitEventRealmProxy2EventDetailDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.SummitEventRealmProxy2ScheduleItemDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.Venue2VenueDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.Venue2VenueFilterDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.VenueFloorRealmProxy2VenueFloorDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.VenueRealmProxy2VenueDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.VenueRealmProxy2VenueFilterDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.VenueRoomRealmProxy2VenueRoomDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.PushNotification2PushNotificationDetailDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.WifiConnection2WifiListItemDTO;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public class DTOAssembler implements IDTOAssembler {
    private ModelMapper modelMapper = new ModelMapper();

    @Inject
    public DTOAssembler() {
        modelMapper.addConverter(new SummitEvent2ScheduleItemDTO());
        modelMapper.addConverter(new SummitEventRealmProxy2ScheduleItemDTO());
        modelMapper.addConverter(new SummitEvent2EventDetailDTO());
        modelMapper.addConverter(new SummitEventRealmProxy2EventDetailDTO());
        modelMapper.addConverter(new PresentationSpeaker2PersonListIemDTO());
        modelMapper.addConverter(new PresentationSpeakerRealmProxy2PersonListItemDTO());
        modelMapper.addConverter(new PresentationSpeaker2PersonDTO());
        modelMapper.addConverter(new PresentationSpeakerRealmProxy2PersonDTO());
        modelMapper.addConverter(new SummitAttendee2PersonDTO());
        modelMapper.addConverter(new SummitAttendeeRealmProxy2PersonDTO());
        modelMapper.addConverter(new Member2MemberDTO());
        modelMapper.addConverter(new MemberRealmProxy2MemberDTO());
        modelMapper.addConverter(new Feedback2FeedbackDTO());
        modelMapper.addConverter(new FeedbackRealmProxy2FeedbackDTO());
        modelMapper.addConverter(new Venue2VenueDTO());
        modelMapper.addConverter(new VenueRoomRealmProxy2VenueRoomDTO());
        modelMapper.addConverter(new VenueRealmProxy2VenueDTO());
        modelMapper.addConverter(new VenueFloorRealmProxy2VenueFloorDTO());
        modelMapper.addConverter(new PushNotification2PushNotificationListItemDTO());
        modelMapper.addConverter(new PushNotificationRealmProxy2PushNotificationListItemDTO());
        modelMapper.addConverter(new PushNotification2PushNotificationDetailDTO());
        modelMapper.addConverter(new PushNotificationRealmProxy2PushNotificationDetailDTO());
        modelMapper.addConverter(new WifiConnection2WifiListItemDTO());
        modelMapper.addConverter(new Venue2VenueFilterDTO());
        modelMapper.addConverter(new VenueRealmProxy2VenueFilterDTO());
    }

    @Override
    public <T, E> E createDTO(T source, Class<E> destinationType) {
        return modelMapper.map(source, destinationType);
    }
}
