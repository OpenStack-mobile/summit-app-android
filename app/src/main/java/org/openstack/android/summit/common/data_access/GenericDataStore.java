package org.openstack.android.summit.common.data_access;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.entities.Company;
import org.openstack.android.summit.common.entities.DataUpdate;
import org.openstack.android.summit.common.entities.EventType;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.Image;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Presentation;
import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.entities.SummitType;
import org.openstack.android.summit.common.entities.Tag;
import org.openstack.android.summit.common.entities.TicketType;
import org.openstack.android.summit.common.entities.Track;
import org.openstack.android.summit.common.entities.TrackGroup;
import org.openstack.android.summit.common.entities.Venue;
import org.openstack.android.summit.common.entities.VenueRoom;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public class GenericDataStore implements IGenericDataStore {
    Realm realm = Realm.getDefaultInstance();

    @Override
    public <T extends RealmObject> T getByIdLocal(int id, Class<T> type) {
        return realm.where(type).equalTo("id", id).findFirst();
    }

    @Override
    public <T extends RealmObject> List<T> getaAllLocal(Class<T> type) {
        ArrayList<T> list = new ArrayList<>();
        RealmResults<T> result = realm.where(type).findAll();
        list.addAll(result.subList(0, result.size()));
        return list;
    }

    @Override
    public <T extends RealmObject> void saveOrUpdate(final T entity, IDataStoreOperationListener<T> delegate, Class<T> type) {
        T realmEntity;
        try{
            realm.beginTransaction();
            realmEntity = realm.copyToRealmOrUpdate(entity);
            realm.commitTransaction();
            if (delegate != null) {
                delegate.onSuceedWithSingleData(realmEntity);
            }
        }
        catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            delegate.onError(e.getMessage());
        }
    }

    @Override
    public <T extends RealmObject> void delete(int id, IDataStoreOperationListener<T> delegate, Class<T> type) {
        try{
            realm.beginTransaction();
            realm.where(type).equalTo("id", id).findFirst().removeFromRealm();
            realm.commitTransaction();
            if (delegate != null) {
                delegate.onSucceedWithoutData();
            }
        }
        catch (Exception e) {
            realm.cancelTransaction();
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            delegate.onError(e.getMessage());
        }
    }

    @Override
    public void clearDataLocal() {
        realm.beginTransaction();
        try{
            realm.clear(Company.class);
            realm.clear(DataUpdate.class);
            realm.clear(EventType.class);
            realm.clear(Feedback.class);
            realm.clear(Image.class);
            realm.clear(Member.class);
            realm.clear(Presentation.class);
            realm.clear(PresentationSpeaker.class);
            realm.clear(SummitAttendee.class);
            realm.clear(SummitEvent.class);
            realm.clear(SummitType.class);
            realm.clear(Tag.class);
            realm.clear(TicketType.class);
            realm.clear(Track.class);
            realm.clear(TrackGroup.class);
            realm.clear(Venue.class);
            realm.clear(VenueRoom.class);
            realm.clear(Summit.class);
            realm.commitTransaction();
        }
        catch (Exception e) {
            realm.cancelTransaction();
        }
    }
}
