package org.openstack.android.summit.common.data_access.repositories.impl;

import org.openstack.android.summit.common.data_access.repositories.IPresentationSpeakerDataStore;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.utils.RealmFactory;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public class PresentationSpeakerDataStore extends GenericDataStore<PresentationSpeaker> implements IPresentationSpeakerDataStore {

    public PresentationSpeakerDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy, IDeleteStrategy deleteStrategy) {
        super(PresentationSpeaker.class, saveOrUpdateStrategy, deleteStrategy);
    }

    @Override
    public List<PresentationSpeaker> getByFilter(int summitId, String searchTerm, int page, int objectsPerPage) {

        Summit summit = RealmFactory.getSession().where(Summit.class).equalTo("id", summitId).findFirst();
        RealmQuery<PresentationSpeaker> query = summit.getSpeakers().where().isNotNull("fullName");

        if (searchTerm != null && !searchTerm.isEmpty()) {
            query.contains("fullName", searchTerm, Case.INSENSITIVE);
        }

        RealmResults<PresentationSpeaker> results = query.findAll();
        results                                   = results.sort(new String[] { "firstName", "lastName"}, new Sort[] { Sort.ASCENDING, Sort.ASCENDING });

        ArrayList<PresentationSpeaker> presentationSpeakers = new ArrayList<>();
        int startRecord = (page-1) * objectsPerPage;
        int endRecord = (startRecord + (objectsPerPage - 1)) <= results.size()
                ? startRecord + (objectsPerPage - 1)
                : results.size() - 1;

        int size = results.size();
        if (startRecord <= endRecord) {
            int index = startRecord;
            while (index  <= endRecord && index < size) {
                presentationSpeakers.add(results.get(index));
                index++;
            }
        }

        return presentationSpeakers;
    }

    public List<PresentationSpeaker> getAllByFilter(int summitId, String searchTerm) {

        Summit summit = RealmFactory.getSession().where(Summit.class).equalTo("id", summitId).findFirst();
        RealmQuery<PresentationSpeaker> query = summit.getSpeakers().where().isNotNull("fullName");

        if (searchTerm != null && !searchTerm.isEmpty()) {
            query.contains("fullName", searchTerm, Case.INSENSITIVE);
        }

        RealmResults<PresentationSpeaker> results = query.findAll();
        results                                   = results.sort(new String[] { "firstName", "lastName"}, new Sort[] { Sort.ASCENDING, Sort.ASCENDING });

        ArrayList<PresentationSpeaker> presentationSpeakers = new ArrayList<>();

        int index = 0;
        int size = results.size();
        while (index < size) {
            presentationSpeakers.add(results.get(index));
            index++;
        }

        return presentationSpeakers;
    }

}
