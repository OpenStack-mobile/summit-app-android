package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.PresentationSpeaker;

import java.util.List;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public interface IPresentationSpeakerDataStore extends IGenericDataStore {
    List<PresentationSpeaker> getByFilterLocal(int summitId, String searchTerm, int page, int objectsPerPage);
}
