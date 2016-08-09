package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.modelmapper.AbstractConverter;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.VenueFloorDTO;
import org.openstack.android.summit.common.entities.VenueFloor;

/**
 * Created by sebastian on 8/9/2016.
 */
public class AbstractVenueFloor2VenueFloorDTO <S extends VenueFloor> extends AbstractConverter<S, VenueFloorDTO> {

    @Override
    protected VenueFloorDTO convert(S source) {
        VenueFloorDTO floor = new VenueFloorDTO();
        try {
            floor.setId(source.getId());
            floor.setName(source.getName());
            floor.setVenueId(source.getVenue().getId());
            floor.setDescription(source.getDescription());
            floor.setPictureUrl(source.getPictureUrl());
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            throw e;
        }
        return floor;
    }
}
