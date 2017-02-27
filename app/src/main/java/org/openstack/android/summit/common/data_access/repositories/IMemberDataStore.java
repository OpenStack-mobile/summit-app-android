package org.openstack.android.summit.common.data_access.repositories;

import org.openstack.android.summit.common.data_access.IDataStoreOperationListener;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.NonConfirmedSummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;

import io.reactivex.Observable;

/**
 * Created by Claudio Redi on 12/16/2015.
 */
public interface IMemberDataStore extends IGenericDataStore<Member> {

    Observable<Integer> getLoggedInMember();

    Observable<Integer> addFeedback(Member member, Feedback feedback);

    Observable<Boolean> addEventToMyFavorites(Member me, SummitEvent summitEvent);

    Observable<Boolean> removeEventFromMyFavorites(Member me, SummitEvent summitEvent);

    void getAttendeesForTicketOrder(String orderNumber, final IDataStoreOperationListener<NonConfirmedSummitAttendee> dataStoreOperationListener);

    void addEventToMyFavoritesLocal(Member me, SummitEvent summitEvent);

    void removeEventFromMyFavoritesLocal(Member me, SummitEvent summitEvent);

    boolean isEventOnMyFavorites(Member me, SummitEvent summitEvent);

}
