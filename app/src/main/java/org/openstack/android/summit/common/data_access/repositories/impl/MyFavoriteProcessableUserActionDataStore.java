package org.openstack.android.summit.common.data_access.repositories.impl;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.repositories.IMyFavoriteProcessableUserActionDataStore;
import org.openstack.android.summit.common.entities.processable_user_actions.MyFavoriteProcessableUserAction;
import org.openstack.android.summit.common.utils.RealmFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smarcet on 2/8/18.
 */

final public class MyFavoriteProcessableUserActionDataStore extends GenericDataStore<MyFavoriteProcessableUserAction>
        implements IMyFavoriteProcessableUserActionDataStore
{

    public MyFavoriteProcessableUserActionDataStore() {
        super(MyFavoriteProcessableUserAction.class, null, null);
    }

    public List<MyFavoriteProcessableUserAction> getAllUnProcessed(int ownerId){
        try{
            return RealmFactory.transaction(session -> session.where(MyFavoriteProcessableUserAction.class)
                    .equalTo("owner.id", ownerId)
                    .equalTo("isProcessed", false).sort("id").findAll());
        }
        catch (Exception e) {
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            Crashlytics.logException(e);
        }
        return new ArrayList<>();
    }
}
