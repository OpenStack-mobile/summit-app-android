package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.Summit;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public interface ISummitDataStore extends IGenericDataStore {

    Summit getLatest();

    Summit getById(int summitId);

    void updateActiveSummitFromDataUpdate(Summit dataUpdateEntity);
}
