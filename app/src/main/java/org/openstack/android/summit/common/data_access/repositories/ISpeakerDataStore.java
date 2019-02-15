package org.openstack.android.summit.common.data_access.repositories;

import org.openstack.android.summit.common.entities.Speaker;

import java.util.List;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public interface ISpeakerDataStore extends IGenericDataStore<Speaker> {

    List<Speaker> getByFilter(int summitId, String searchTerm, int page, int objectsPerPage);

    List<Speaker> getAllByFilter(int summitId, String searchTerm);
}
