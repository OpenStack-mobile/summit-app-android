package org.openstack.android.summit.modules.feedback_edit.business_logic;

import org.openstack.android.summit.common.DTOs.FeedbackDTO;
import org.openstack.android.summit.common.business_logic.IBaseInteractor;
import org.openstack.android.summit.common.entities.exceptions.ValidationException;

import io.reactivex.Observable;

/**
 * Created by Claudio Redi on 2/17/2016.
 */
public interface IFeedbackEditInteractor extends IBaseInteractor {

    Observable<FeedbackDTO> saveFeedback(int eventId, int rate, String review) throws ValidationException;
}
