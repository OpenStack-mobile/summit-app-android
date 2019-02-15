package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.modelmapper.AbstractConverter;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.entities.Speaker;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public class AbstractPresentationSpeaker2PersonListIemDTO<S extends PresentationSpeaker, T extends PersonListItemDTO> extends AbstractConverter<S, T> {

    protected void convertInternal(S source, PersonListItemDTO personListItemDTO) {

        personListItemDTO.setId(source.getSpeaker().getId());
        personListItemDTO.setName(source.getSpeaker().getFirstName() + " " + source.getSpeaker().getLastName());
        personListItemDTO.setTitle(source.getSpeaker().getTitle());
        personListItemDTO.setPictureUrl(source.getSpeaker().getPictureUrl());
        personListItemDTO.setIsSpeaker(true);
        personListItemDTO.setIsAttendee(false);
        personListItemDTO.setRole(source.getRole());
    }

    @Override
    protected T convert(S source) {
        PersonListItemDTO personListItemDTO = new PersonListItemDTO();
        try {
            convertInternal(source, personListItemDTO);
        }
        catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            throw e;
        }
        return (T)personListItemDTO;
    }
}