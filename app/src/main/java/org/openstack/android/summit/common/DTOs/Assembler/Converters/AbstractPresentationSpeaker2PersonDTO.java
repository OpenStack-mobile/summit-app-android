package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import org.openstack.android.summit.common.DTOs.PersonDTO;
import org.openstack.android.summit.common.entities.PresentationSpeaker;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public class AbstractPresentationSpeaker2PersonDTO<S extends PresentationSpeaker> extends AbstractPresentationSpeaker2PersonListIemDTO<S, PersonDTO> {

    @Override
    protected PersonDTO convert(S source) {
        PersonDTO personDTO = new PersonDTO();
        convertInternal(source, personDTO);
        personDTO.setBio(source.getBio());
        personDTO.setTwitter(source.getTwitter());
        personDTO.setIrc(source.getIrc());

        return personDTO;
    }
}