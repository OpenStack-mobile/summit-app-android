package org.openstack.android.summit.common.business_logic;

import org.openstack.android.summit.common.DTOs.MemberDTO;
import org.openstack.android.summit.common.DTOs.SummitDTO;

/**
 * Created by Claudio Redi on 2/10/2016.
 */
public interface IBaseInteractor {

    boolean isDataLoaded();

    SummitDTO getActiveSummit();

    SummitDTO getLatestSummit();

    boolean isMemberLoggedIn();

    boolean isMemberLoggedInAndConfirmedAttendee();

    MemberDTO getCurrentMember();

    boolean isNetworkingAvailable();
}
