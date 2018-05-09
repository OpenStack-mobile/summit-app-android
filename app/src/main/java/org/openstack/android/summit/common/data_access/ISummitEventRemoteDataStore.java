package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitEvent;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Claudio Redi on 2/19/2016.
 */
public interface ISummitEventRemoteDataStore {

    Observable<List<Feedback>> getFeedback(int eventId, int page, int objectsPerPage);

    Observable<Double> getAverageFeedback(int eventId);

    Observable<SummitEvent> getSummitEventById(int eventId);
}
