package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.modelmapper.AbstractConverter;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.VenueDTO;
import org.openstack.android.summit.common.entities.Image;
import org.openstack.android.summit.common.entities.Venue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 2/15/2016.
 */
public class AbstractVenue2VenueDTO <S extends Venue> extends AbstractConverter<S, VenueDTO> {
    @Override
    protected VenueDTO convert(S source) {
        VenueDTO venueDTO = new VenueDTO();
        try {
            venueDTO.setId(source.getId());
            venueDTO.setName(source.getName());
            venueDTO.setAddress(getAddress(source));
            venueDTO.setLat(source.getLat());
            venueDTO.setLng(source.getLng());
            venueDTO.setMaps(getMaps(source));
            venueDTO.setImages(getImages(source));
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            throw e;
        }
        return venueDTO;
    }

    private List<String> getImages(S source) {
        ArrayList<String> images = new ArrayList<>();
        for (Image image: source.getImages()) {
            images.add(image.getImageUrl());
        }
        return images;
    }

    private List<String> getMaps(S source) {
        ArrayList<String> maps = new ArrayList<>();
        for (Image map: source.getMaps()) {
            maps.add(map.getImageUrl());
        }
        return maps;
    }

    private String getAddress(S venue) {
        String fullAddress = venue.getAddress();
        String separator = ", ";

        if (venue.getCity() != null && !venue.getCity().isEmpty()) {
            fullAddress += String.format("%s%s", separator, venue.getCity());
            separator = " ";
        }

        if (venue.getState() != null && !venue.getState().isEmpty()) {
            fullAddress += String.format("%s%s", separator, venue.getState());
            separator = " ";
        }

        if (venue.getZipCode() != null && !venue.getZipCode().isEmpty()) {
            fullAddress += String.format("%s(%s)", separator, venue.getZipCode());
            separator = " ";
        }

        if (venue.getCountry() != null && !venue.getCountry().isEmpty()) {
            fullAddress += String.format("%s%s", separator, venue.getCountry());
        }

        return fullAddress;
    }
}
