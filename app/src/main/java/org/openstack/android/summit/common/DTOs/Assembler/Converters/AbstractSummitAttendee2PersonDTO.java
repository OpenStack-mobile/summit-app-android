package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.modelmapper.AbstractConverter;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.PersonDTO;
import org.openstack.android.summit.common.entities.SummitAttendee;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public class AbstractSummitAttendee2PersonDTO<S extends SummitAttendee> extends AbstractConverter<S, PersonDTO> {

    @Override
    protected PersonDTO convert(S source) {
        PersonDTO personDTO = new PersonDTO();

        try {
            personDTO.setId(source.getId());
        }
        catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            throw e;
        }

        return personDTO;
    }
}
