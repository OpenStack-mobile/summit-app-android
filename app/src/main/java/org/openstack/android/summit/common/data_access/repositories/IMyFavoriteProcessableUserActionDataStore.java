package org.openstack.android.summit.common.data_access.repositories;

import org.openstack.android.summit.common.entities.processable_user_actions.MyFavoriteProcessableUserAction;
import java.util.List;

/**
 * Created by smarcet on 2/8/18.
 */

public interface IMyFavoriteProcessableUserActionDataStore extends IGenericDataStore<MyFavoriteProcessableUserAction>  {

    List<MyFavoriteProcessableUserAction> getAllUnProcessed(int ownerId);
}

