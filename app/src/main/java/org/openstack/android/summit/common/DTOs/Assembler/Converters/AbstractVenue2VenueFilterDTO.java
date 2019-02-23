package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.modelmapper.AbstractConverter;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.VenueFilterDTO;
import org.openstack.android.summit.common.entities.Venue;

public class AbstractVenue2VenueFilterDTO<S extends Venue> extends AbstractConverter<S, VenueFilterDTO> {

    @Override
    protected VenueFilterDTO convert(S source) {
        VenueFilterDTO venueDTO = new VenueFilterDTO();
        try {
            venueDTO.setId(source.getId());
            venueDTO.setName(source.getName());
            venueDTO.setHasRooms(source.hasRooms());
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            throw e;
        }
        return venueDTO;
    }
}
