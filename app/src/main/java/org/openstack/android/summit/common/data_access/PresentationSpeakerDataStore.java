package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.utils.RealmFactory;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public class PresentationSpeakerDataStore extends GenericDataStore implements IPresentationSpeakerDataStore {

    @Override
    public List<PresentationSpeaker> getByFilterLocal(String searchTerm, int page, int objectsPerPage) {
         RealmQuery<PresentationSpeaker> query = RealmFactory.getSession().where(PresentationSpeaker.class);
        query.isNotNull("fullName");

        if (searchTerm != null && !searchTerm.isEmpty()) {
            query.contains("fullName", searchTerm, Case.INSENSITIVE);
        }

        RealmResults<PresentationSpeaker> results = query.findAll();
        results.sort(new String[] { "firstName", "lastName"}, new Sort[] { Sort.ASCENDING, Sort.ASCENDING });

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
}
