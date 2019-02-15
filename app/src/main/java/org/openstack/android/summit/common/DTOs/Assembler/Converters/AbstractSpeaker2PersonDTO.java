package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.PersonDTO;
import org.openstack.android.summit.common.entities.Speaker;

public class AbstractSpeaker2PersonDTO <S extends Speaker> extends AbstractSpeaker2PersonListIemDTO<S, PersonDTO> {

    @Override
    protected PersonDTO convert(S source) {
        PersonDTO personDTO = new PersonDTO();

        try {
            convertInternal(source, personDTO);
            personDTO.setBio(source.getBio());
            personDTO.setTwitter(source.getTwitter());
            personDTO.setIrc(source.getIrc());
        }
        catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            throw e;
        }

        return personDTO;
    }
}