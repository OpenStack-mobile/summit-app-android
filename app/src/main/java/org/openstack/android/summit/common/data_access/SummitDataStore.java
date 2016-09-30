package org.openstack.android.summit.common.data_access;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.deserialization.DataStoreOperationListener;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.common.utils.Void;

import java.security.InvalidParameterException;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public class SummitDataStore extends GenericDataStore implements ISummitDataStore {
    private ISummitRemoteDataStore summitRemoteDataStore;

    @Inject
    public SummitDataStore(ISummitRemoteDataStore summitRemoteDataStore) {
        this.summitRemoteDataStore = summitRemoteDataStore;
    }

    @Override
    public void getActive(final IDataStoreOperationListener<Summit> dataStoreOperationListener) {
        List<Summit> summits = getAllLocal(Summit.class);

        if (summits.size() > 0) {
            if (dataStoreOperationListener != null) {
                dataStoreOperationListener.onSucceedWithSingleData(summits.get(0));
            }
        }
        else {

            DataStoreOperationListener<Summit> remoteDelegate = new DataStoreOperationListener<Summit>() {
                @Override
                public void onSucceedWithSingleData(Summit data) {
                    try{
                        RealmFactory.getSession().beginTransaction();
                        Summit realmEntity = RealmFactory.getSession().copyToRealmOrUpdate(data);
                        RealmFactory.getSession().commitTransaction();
                        if (dataStoreOperationListener != null) {
                            dataStoreOperationListener.onSucceedWithSingleData(realmEntity);
                        }
                    }
                    catch (Exception e) {
                        RealmFactory.getSession().cancelTransaction();
                        Crashlytics.logException(e);
                        Log.e(Constants.LOG_TAG, e.getMessage(), e);
                        dataStoreOperationListener.onError(e.getMessage());
                    }
                }

                @Override
                public void onError(String message) {
                    dataStoreOperationListener.onError(message);
                }
            };

            summitRemoteDataStore.getActive(remoteDelegate);
        }
    }

    @Override
    public Summit getActiveLocal() {
        List<Summit> summits = getAllLocal(Summit.class);
        return summits.size() > 0 ? summits.get(0) : null;
    }

    @Override
    public void updateActiveSummitFromDataUpdate(final Summit dataUpdateEntity) {
        try{

            RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                @Override
                public Void callback(Realm session) throws Exception {
                    Summit summit = session.where(Summit.class).equalTo("id", dataUpdateEntity.getId()).findFirst();
                    if(summit == null) throw new InvalidParameterException("missing current summit!");
                    summit.setName(dataUpdateEntity.getName());
                    summit.setStartShowingVenuesDate(dataUpdateEntity.getStartShowingVenuesDate());
                    summit.setStartDate(dataUpdateEntity.getStartDate());
                    summit.setEndDate(dataUpdateEntity.getEndDate());
                    return Void.getInstance();
                }
            });
        }
        catch (Exception e) {
           Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
        }
    }
}
