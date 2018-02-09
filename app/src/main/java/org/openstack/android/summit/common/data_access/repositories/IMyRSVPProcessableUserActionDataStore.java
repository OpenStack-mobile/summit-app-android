package org.openstack.android.summit.common.data_access.repositories;

import org.openstack.android.summit.common.entities.processable_user_actions.MyRSVPProcessableUserAction;

import java.util.List;

/**
 * Created by smarcet on 2/8/18.
 */

public interface IMyRSVPProcessableUserActionDataStore extends IGenericDataStore<MyRSVPProcessableUserAction>  {
        List<MyRSVPProcessableUserAction> getAllUnProcessed(int ownerId);
}
