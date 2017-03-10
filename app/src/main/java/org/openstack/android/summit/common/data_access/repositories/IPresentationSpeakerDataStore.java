package org.openstack.android.summit.common.data_access.repositories;

import org.openstack.android.summit.common.entities.PresentationSpeaker;

import java.util.List;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public interface IPresentationSpeakerDataStore extends IGenericDataStore<PresentationSpeaker> {

    List<PresentationSpeaker> getByFilter(int summitId, String searchTerm, int page, int objectsPerPage);

    List<PresentationSpeaker> getAllByFilter(int summitId, String searchTerm);
}
