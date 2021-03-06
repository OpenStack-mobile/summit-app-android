package org.openstack.android.summit.common.data_access.repositories.impl;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.repositories.IMyRSVPProcessableUserActionDataStore;
import org.openstack.android.summit.common.entities.processable_user_actions.MyRSVPProcessableUserAction;
import org.openstack.android.summit.common.utils.RealmFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smarcet on 2/8/18.
 */

public class MyRSVPProcessableUserActionDataStore extends GenericDataStore<MyRSVPProcessableUserAction>
        implements IMyRSVPProcessableUserActionDataStore
{

    public MyRSVPProcessableUserActionDataStore() {
        super(MyRSVPProcessableUserAction.class, null, null);
    }

    public List<MyRSVPProcessableUserAction> getAllUnProcessed(int ownerId){
        try{
            return RealmFactory.transaction(session -> {
                List<MyRSVPProcessableUserAction> res = session.where(MyRSVPProcessableUserAction.class)
                        .equalTo("owner.id", ownerId)
                        .equalTo("isProcessed", false).sort("id").findAll();

                return session.copyFromRealm(res);
            });
        }
        catch (Exception e) {
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            Crashlytics.logException(e);
        }
        return new ArrayList<>();
    }
}
