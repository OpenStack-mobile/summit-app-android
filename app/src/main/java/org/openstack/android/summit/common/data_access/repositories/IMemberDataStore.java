package org.openstack.android.summit.common.data_access.repositories;

import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.NonConfirmedSummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;
import java.util.List;
import io.reactivex.Observable;

/**
 * Created by Claudio Redi on 12/16/2015.
 */
public interface IMemberDataStore extends IGenericDataStore<Member> {

    Observable<Integer> getLoggedInMember();

    Observable<String> addFeedback(Member member, Feedback feedback);

    Observable<Boolean> updateFeedback(Member member, Feedback feedback);

    Observable<Boolean> addEventToMyFavorites(Member me, SummitEvent summitEvent);

    Observable<Boolean> removeEventFromMyFavorites(Member me, SummitEvent summitEvent);

    Observable<List<NonConfirmedSummitAttendee>> getAttendeesForTicketOrder(String orderNumber);

    boolean addEventToMyFavoritesLocal(Member me, SummitEvent summitEvent);

    boolean removeEventFromMyFavoritesLocal(Member me, SummitEvent summitEvent);

    boolean isEventOnMyFavorites(int memberId, int eventId);

    Observable<Boolean> addEventToMemberSchedule(Member me, SummitEvent summitEvent);

    boolean addEventToMemberScheduleLocal(Member me, SummitEvent summitEvent);

    Observable<Boolean> removeEventFromMemberSchedule(Member me, SummitEvent summitEvent);

    Observable<Boolean> deleteRSVP(Member me, SummitEvent summitEvent);

    boolean removeEventFromMemberScheduleLocal(Member me, SummitEvent summitEvent);

    boolean isEventScheduledByMember(int memberId, int eventId);

}
