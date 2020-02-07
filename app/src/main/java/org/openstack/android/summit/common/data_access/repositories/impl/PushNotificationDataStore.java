package org.openstack.android.summit.common.data_access.repositories.impl;

import org.openstack.android.summit.common.data_access.IPushNotificationRemoteDataStore;
import org.openstack.android.summit.common.data_access.repositories.IPushNotificationDataStore;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.entities.notifications.PushNotification;
import org.openstack.android.summit.common.utils.RealmFactory;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observable;
import io.realm.Case;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by sebastian on 8/20/2016.
 */
public class PushNotificationDataStore
        extends GenericDataStore<PushNotification> implements IPushNotificationDataStore {

    private IPushNotificationRemoteDataStore remoteDataStore;

    public PushNotificationDataStore
    (
            IPushNotificationRemoteDataStore remoteDataStore,
            ISaveOrUpdateStrategy saveOrUpdateStrategy,
            IDeleteStrategy deleteStrategy
    )
    {
        super(PushNotification.class, saveOrUpdateStrategy, deleteStrategy);
        this.remoteDataStore = remoteDataStore;
    }

    @Override
    public long getNotOpenedCountBy(Integer memberId) {
        RealmQuery<PushNotification> query = RealmFactory.getSession().where(PushNotification.class).equalTo("opened", false);
        if(memberId == null || memberId == 0){
            query = query.isNull("owner");
        }
        else{
            query
                    .beginGroup()
                    .equalTo("owner.id", memberId)
                    .or()
                    .isNull("owner")
                    .endGroup();
        }
        return query.count();
    }

    @Override
    public List<PushNotification> getByFilter(String searchTerm, Integer memberId, int page, int objectsPerPage) {
        RealmQuery<PushNotification> query = RealmFactory.getSession().where(PushNotification.class);

        if(memberId == null || memberId == 0){
            query = query.isNull("owner");
        }
        else{
            query
                    .beginGroup()
                        .equalTo("owner.id", memberId)
                        .or()
                        .isNull("owner")
                    .endGroup();
        }

        if (searchTerm != null && !searchTerm.isEmpty()) {
            query
                    .beginGroup()
                        .contains("title", searchTerm, Case.INSENSITIVE)
                        .or()
                        .contains("body", searchTerm, Case.INSENSITIVE)
                    .endGroup();
        }

        RealmResults<PushNotification> results = query.findAll().sort("created_at", Sort.DESCENDING);

        ArrayList<PushNotification> notifications = new ArrayList<>();
        int startRecord                            = (page-1) * objectsPerPage;
        int endRecord                              = (startRecord + (objectsPerPage - 1)) <= results.size()
                ? startRecord + (objectsPerPage - 1)
                : results.size() - 1;

        int size = results.size();
        if (startRecord <= endRecord) {
            int index = startRecord;
            while (index  <= endRecord && index < size) {
                notifications.add(results.get(index));
                index++;
            }
        }

        return notifications;
    }

    @Override
    public Observable<List<PushNotification>> getByFilterRemote(String searchTerm, Integer memberId, int page, int objectsPerPage) {
        return remoteDataStore.get(searchTerm, page, objectsPerPage).onErrorReturn(ex -> RealmFactory.transaction(session ->
             this.getByFilter(searchTerm, memberId, page, objectsPerPage)
        )).doOnTerminate(RealmFactory::closeSession);
    }
}
