package org.openstack.android.summit.modules.event_detail.business_logic;

import org.openstack.android.summit.common.DTOs.EventDetailDTO;
import org.openstack.android.summit.common.DTOs.FeedbackDTO;
import org.openstack.android.summit.common.IBaseWireframe;
import org.openstack.android.summit.common.business_logic.IInteractorAsyncOperationListener;
import org.openstack.android.summit.common.business_logic.IScheduleableInteractor;

import java.util.List;

/**
 * Created by Claudio Redi on 1/21/2016.
 */
public interface IEventDetailInteractor extends IScheduleableInteractor {

    EventDetailDTO getEventDetail(int eventId);

    FeedbackDTO getMyFeedbackForEvent(int eventId);

    void getFeedbackForEvent(int eventId, int page, int objectsPerPage, IInteractorAsyncOperationListener<List<FeedbackDTO>> interactorAsyncOperationListener);

    void getAverageFeedbackForEvent(int eventId, IInteractorAsyncOperationListener<Double> interactorAsyncOperationListener);
}
