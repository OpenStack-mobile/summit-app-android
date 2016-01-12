package org.openstack.android.summit.modules.track_list.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.entities.Track;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public class TrackListInteractor implements ITrackListInteractor {
    private IDTOAssembler dtoAssembler;
    private IGenericDataStore genericDataStore;

    @Inject
    public TrackListInteractor(IDTOAssembler dtoAssembler, IGenericDataStore genericDataStore) {
        this.dtoAssembler = dtoAssembler;
        this.genericDataStore = genericDataStore;
    }

    @Override
    public List<NamedDTO> getTracks() {
        List<Track> tracks = genericDataStore.getaAllLocal(Track.class);
        Comparator<Track> comparator = new Comparator<Track>() {
            @Override
            public int compare(Track c1, Track c2) {
                return c1.getName().compareTo(c2.getName());
            }
        };

        Collections.sort(tracks, comparator);

        ArrayList<NamedDTO> dtos = new ArrayList<>();
        NamedDTO namedDTO;
        for (Track track: tracks) {
            namedDTO = dtoAssembler.createDTO(track, NamedDTO.class);
            dtos.add(namedDTO);
        }

        return dtos;
    }
}
