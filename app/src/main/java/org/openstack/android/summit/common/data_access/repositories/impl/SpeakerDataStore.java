package org.openstack.android.summit.common.data_access.repositories.impl;

import org.openstack.android.summit.common.data_access.repositories.ISpeakerDataStore;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.entities.Speaker;
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
public class SpeakerDataStore extends GenericDataStore<Speaker> implements ISpeakerDataStore {

    public SpeakerDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy, IDeleteStrategy deleteStrategy) {
        super(Speaker.class, saveOrUpdateStrategy, deleteStrategy);
    }

    @Override
    public List<Speaker> getByFilter(int summitId, String searchTerm, int page, int objectsPerPage) {

        ArrayList<Speaker> speakers = new ArrayList<>();
        Summit summit                                       = RealmFactory.getSession().where(Summit.class).equalTo("id", summitId).findFirst();
        if(summit == null) return speakers;

        RealmQuery<Speaker> query               = summit.getSpeakers().where().isNotNull("fullName");

        if (searchTerm != null && !searchTerm.isEmpty()) {
            query
                    .beginGroup()
                            .contains("fullName", searchTerm, Case.INSENSITIVE)
                        .or()
                            .contains("affiliations.name", searchTerm, Case.INSENSITIVE)
                    .endGroup();
        }

        RealmResults<Speaker> results = query.findAll();
        results                                   = results.sort(new String[] { "firstName", "lastName"}, new Sort[] { Sort.ASCENDING, Sort.ASCENDING });


        int startRecord = (page-1) * objectsPerPage;
        int endRecord = (startRecord + (objectsPerPage - 1)) <= results.size()
                ? startRecord + (objectsPerPage - 1)
                : results.size() - 1;

        int size = results.size();
        if (startRecord <= endRecord) {
            int index = startRecord;
            while (index  <= endRecord && index < size) {
                speakers.add(results.get(index));
                index++;
            }
        }

        return speakers;
    }

    public List<Speaker> getAllByFilter(int summitId, String searchTerm) {

        Summit summit = RealmFactory.getSession().where(Summit.class).equalTo("id", summitId).findFirst();

        if(summit == null) return new ArrayList<>();

        RealmQuery<Speaker> query = summit.getSpeakers().where().isNotNull("fullName");

        if (searchTerm != null && !searchTerm.isEmpty()) {
            query.contains("fullName", searchTerm, Case.INSENSITIVE);
        }

        RealmResults<Speaker> results = query.findAll();
        results                                   = results.sort(new String[] { "firstName", "lastName"}, new Sort[] { Sort.ASCENDING, Sort.ASCENDING });

        ArrayList<Speaker> speakers = new ArrayList<>();

        int index = 0;
        int size = results.size();
        while (index < size) {
            speakers.add(results.get(index));
            index++;
        }

        return speakers;
    }

}
