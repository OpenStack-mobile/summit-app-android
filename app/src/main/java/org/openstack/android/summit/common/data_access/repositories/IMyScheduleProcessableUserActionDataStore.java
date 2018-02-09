package org.openstack.android.summit.common.data_access.repositories;

import org.openstack.android.summit.common.entities.processable_user_actions.MyScheduleProcessableUserAction;

import java.util.List;

/**
 * Created by smarcet on 2/8/18.
 */

public interface IMyScheduleProcessableUserActionDataStore extends IGenericDataStore<MyScheduleProcessableUserAction>  {

    List<MyScheduleProcessableUserAction> getAllUnProcessed(int ownerId);
}
