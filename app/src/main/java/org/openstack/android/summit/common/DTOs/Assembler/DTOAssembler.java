package org.openstack.android.summit.common.DTOs.Assembler;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.SummitEvent2ScheduleItemDTO;
import org.openstack.android.summit.common.DTOs.Assembler.Converters.SummitEventRealmProxy2ScheduleItemDTO;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.entities.Company;
import org.openstack.android.summit.common.entities.ISummitEvent;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.entities.SummitType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.inject.Inject;

import io.realm.SummitEventRealmProxy;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public class DTOAssembler implements IDTOAssembler {
    private ModelMapper modelMapper = new ModelMapper();

    @Inject
    public DTOAssembler() {
        modelMapper.createTypeMap(SummitEvent.class, ScheduleItemDTO.class).setConverter(new SummitEvent2ScheduleItemDTO());
        modelMapper.createTypeMap(SummitEventRealmProxy.class, ScheduleItemDTO.class).setConverter(new SummitEventRealmProxy2ScheduleItemDTO());
    }

    @Override
    public <T,E> E createDTO(T source, Class<E> destinationType) {

        return modelMapper.map(source, destinationType);
    }
}
