package org.openstack.android.summit.common.DTOs.Assembler.Converters;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import org.modelmapper.AbstractConverter;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.VenueRoomDTO;
import org.openstack.android.summit.common.entities.VenueRoom;

/**
 * Created by sebastian on 8/9/2016.
 */
public class AbstractVenueRoom2VenueRoomDTO<S extends VenueRoom> extends AbstractConverter<S, VenueRoomDTO> {

    @Override
    protected VenueRoomDTO convert(S source) {
        VenueRoomDTO venueRoomDTO = new VenueRoomDTO();
        try {

            venueRoomDTO.setId(source.getId());
            venueRoomDTO.setName(source.getName());
            venueRoomDTO.setVenueId(source.getVenue().getId());

            if(source.getFloor() != null)
                venueRoomDTO.setFloorId(source.getFloor().getId());

            venueRoomDTO.setCapacity(source.getCapacity());

        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            throw e;
        }
        return venueRoomDTO;
    }
}
