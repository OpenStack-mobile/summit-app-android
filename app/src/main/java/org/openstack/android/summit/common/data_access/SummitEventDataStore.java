package org.openstack.android.summit.common.data_access;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.data_access.deserialization.DataStoreOperationListener;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.Presentation;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.utils.RealmFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import io.realm.Case;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Claudio Redi on 12/20/2015.
 */
public class SummitEventDataStore extends GenericDataStore implements ISummitEventDataStore {

    ISummitEventRemoteDataStore summitEventRemoteDataStore;

    public SummitEventDataStore(ISummitEventRemoteDataStore summitEventRemoteDataStore) {
        this.summitEventRemoteDataStore = summitEventRemoteDataStore;
    }

    @Override
    public List<SummitEvent> getByFilterLocal
    (
        DateTime startDate,
        DateTime endDate,
        List<Integer> eventTypes,
        List<Integer> summitTypes,
        List<Integer> trackGroups,
        List<Integer> tracks,
        List<String> tags,
        List<String> levels
    )
    {

        RealmQuery<SummitEvent> query = RealmFactory.getSession().where(SummitEvent.class)
                .greaterThanOrEqualTo("start", startDate.toDate())
                .lessThanOrEqualTo("end", endDate.toDate());

        boolean isFirst;

        isFirst = true;
        if (eventTypes != null) {
            query.beginGroup();
            for (int eventTypeId : eventTypes) {
                if (!isFirst) {
                    query = query.or();
                }
                query = query.equalTo("eventType.id", eventTypeId);
                isFirst = false;
            }
            query.endGroup();
        }

        isFirst = true;
        if (tags != null) {
            query.beginGroup();
            for (String tag : tags) {
                if (!isFirst) {
                    query = query.or();
                }
                query = query.equalTo("tags.tag", tag);
                isFirst = false;
            }
            query.endGroup();
        }

        isFirst = true;
        if (summitTypes != null) {
            query.beginGroup();
            for (int summitTypeId : summitTypes) {
                if (!isFirst) {
                    query = query.or();
                }
                query = query.equalTo("summitTypes.id", summitTypeId);
                isFirst = false;
            }
            query.endGroup();
        }

        isFirst = true;
        if (levels != null) {
            query.beginGroup();
            for (String level : levels) {
                if (!isFirst) {
                    query = query.or();
                }
                query = query.equalTo("presentation.level", level);
                isFirst = false;
            }
            query.endGroup();
        }

        isFirst = true;
        if (tracks != null) {
            query.beginGroup();
            for (int trackId : tracks) {
                if (!isFirst) {
                    query = query.or();
                }
                query = query.equalTo("presentation.track.id", trackId);
                isFirst = false;
            }
            query.endGroup();
        }

        isFirst = true;
        if (trackGroups != null) {
            query.beginGroup();
            for (int trackGroupId : trackGroups) {
                if (!isFirst) {
                    query = query.or();
                }
                query = query.equalTo("presentation.track.trackGroups.id", trackGroupId);
                isFirst = false;
            }
            query.endGroup();
        }

        return query.findAllSorted(new String[] { "start", "end", "name"}, new Sort[] { Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING });
    }

    public SummitEvent getByIdLocal(int id) {
        return getByIdLocal(id, SummitEvent.class);
    }

    @Override
    public List<String> getPresentationLevelsLocal() {
        ArrayList<String> levels = new ArrayList<>();
        RealmResults<Presentation> presentations = RealmFactory.getSession().where(Presentation.class).findAll();
        for (Presentation presentation : presentations) {
            if (!levels.contains(presentation.getLevel())) {
                levels.add(presentation.getLevel());
            }
        }

        Collections.sort(levels);

        // HACK: this is to return in a specific order (begginer - intermediate - advanced)
        ArrayList<String> levelsSorted = new ArrayList<>();
        if (levels.size() == 3) {
            levelsSorted.add(levels.get(1));
            levelsSorted.add(levels.get(2));
            levelsSorted.add(levels.get(0));
        }
        else {
            levelsSorted = levels;
        }
        return levelsSorted;
    }

    @Override
    public List<SummitEvent> getBySearchTerm(String searchTerm) {
        RealmQuery<SummitEvent> query = RealmFactory.getSession().where(SummitEvent.class)
                .contains("name", searchTerm, Case.INSENSITIVE)
                .or()
                .contains("tags.tag", searchTerm, Case.INSENSITIVE)
                .or()
                .contains("presentation.speakers.fullName", searchTerm, Case.INSENSITIVE)
                .or()
                .contains("presentation.level", searchTerm, Case.INSENSITIVE);

        RealmResults<SummitEvent> results = query.findAll();
        results.sort(new String[] { "start", "end", "name"}, new Sort[] { Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING });
        return results;
    }

    @Override
    public List<SummitEvent> getSpeakerEvents(int speakerId, DateTime startDate, DateTime endDate) {
        RealmQuery<SummitEvent> query = RealmFactory.getSession().where(SummitEvent.class)
                .greaterThanOrEqualTo("start", startDate.toDate())
                .lessThanOrEqualTo("end", endDate.toDate())
                .equalTo("presentation.speakers.id", speakerId);

        return query.findAllSorted(new String[] { "start", "end", "name"}, new Sort[] { Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING });
    }

    @Override
    public void getFeedbackOrigin(int eventId, int page, int objectsPerPage, final IDataStoreOperationListener<Feedback> dataStoreOperationListener) {
        IDataStoreOperationListener<Feedback> remoteDataStoreOperationListener = new DataStoreOperationListener<Feedback>() {
            @Override
            public void onSucceedWithDataCollection(List<Feedback> data) {
                super.onSucceedWithDataCollection(data);
                dataStoreOperationListener.onSucceedWithDataCollection(data);
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                dataStoreOperationListener.onError(message);
            }
        };

        summitEventRemoteDataStore.getFeedback(eventId, page, objectsPerPage, remoteDataStoreOperationListener);
    }

    @Override
    public void getAverageFeedbackOrigin(int eventId, final IDataStoreOperationListener<SummitEvent> dataStoreOperationListener) {
        IDataStoreOperationListener<SummitEvent> remoteDataStoreOperationListener = new DataStoreOperationListener<SummitEvent>() {

            @Override
            public void onSucceedWithSingleData(SummitEvent data) {
                super.onSucceedWithSingleData(data);
                dataStoreOperationListener.onSucceedWithSingleData(data);
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                dataStoreOperationListener.onError(message);
            }
        };

        summitEventRemoteDataStore.getAverageFeedback(eventId, remoteDataStoreOperationListener);
    }
}