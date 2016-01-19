package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import org.modelmapper.AbstractConverter;
import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.entities.IPerson;
import org.openstack.android.summit.common.entities.PresentationSpeaker;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public class AbstractPresentationSpeaker2PersonListIemDTO<T extends PresentationSpeaker> extends AbstractConverter<T, PersonListItemDTO> {

    @Override
    protected PersonListItemDTO convert(T source) {
        PersonListItemDTO personListItemDTO = new PersonListItemDTO();
        personListItemDTO.setId(source.getId());
        personListItemDTO.setName(source.getFirstName() + " " + source.getLastName());
        personListItemDTO.setTitle(source.getTitle());
        personListItemDTO.setPictureUrl(source.getPictureUrl());
        personListItemDTO.setIsSpeaker(true);
        personListItemDTO.setIsAttendee(false);
        return personListItemDTO;
    }
}
