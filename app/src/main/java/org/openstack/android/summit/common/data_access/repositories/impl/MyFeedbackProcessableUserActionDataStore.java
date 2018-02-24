package org.openstack.android.summit.common.data_access.repositories.impl;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.repositories.IMyFeedbackProcessableUserActionDataStore;
import org.openstack.android.summit.common.entities.processable_user_actions.MyFeedbackProcessableUserAction;
import org.openstack.android.summit.common.utils.RealmFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smarcet on 2/8/18.
 */

public class MyFeedbackProcessableUserActionDataStore extends GenericDataStore<MyFeedbackProcessableUserAction>
        implements IMyFeedbackProcessableUserActionDataStore
{

    public MyFeedbackProcessableUserActionDataStore() {
        super(MyFeedbackProcessableUserAction.class, null, null);
    }

    public List<MyFeedbackProcessableUserAction> getAllUnProcessed(int ownerId){
        try{
            return RealmFactory.transaction(session -> {

                List<MyFeedbackProcessableUserAction>  res = session.where(MyFeedbackProcessableUserAction.class)
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