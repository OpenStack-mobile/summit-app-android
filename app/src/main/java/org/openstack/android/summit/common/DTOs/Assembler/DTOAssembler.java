package org.openstack.android.summit.common.DTOs.Assembler;

import org.modelmapper.ModelMapper;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.PresentationSpeaker2PersonListIemDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.PresentationSpeakerRealmProxy2PersonListItemDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.SummitEvent2EventDetailDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.SummitEvent2ScheduleItemDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.SummitEventRealmProxy2EventDetailDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.SummitEventRealmProxy2ScheduleItemDTO;

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
        modelMapper.addConverter(new PresentationSpeaker2PersonListIemDTO());
        modelMapper.addConverter(new PresentationSpeakerRealmProxy2PersonListItemDTO());
        modelMapper.addConverter(new SummitEvent2EventDetailDTO());
        modelMapper.addConverter(new SummitEventRealmProxy2EventDetailDTO());
    }

    @Override
    public <T, E> E createDTO(T source, Class<E> destinationType) {
        return modelMapper.map(source, destinationType);
    }
}
