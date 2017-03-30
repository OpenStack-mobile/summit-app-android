package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.NonConfirmedSummitAttendee;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Claudio Redi on 12/16/2015.
 */
public interface IMemberRemoteDataStore {

    Observable<Member> getMemberInfo();

    Observable<Integer> addFeedback(int eventId, int rate, String review);

    Observable<Boolean> updateFeedback(int eventId, int rate, String review);

    Observable<Boolean> addSummitEvent2Favorites(int summitId, int eventId);

    Observable<Boolean> removeSummitEventFromFavorites(int summitId, int eventId);

    Observable<List<NonConfirmedSummitAttendee>> getAttendeesForTicketOrder(String orderNumber);

    Observable<Boolean> selectAttendeeFromOrderList(String orderNumber, int externalAttendeeId);

}
