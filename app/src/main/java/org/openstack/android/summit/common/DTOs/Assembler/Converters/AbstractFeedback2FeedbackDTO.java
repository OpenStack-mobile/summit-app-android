package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import android.text.format.DateUtils;
import android.util.Log;

import org.modelmapper.AbstractConverter;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.FeedbackDTO;
import org.openstack.android.summit.common.entities.Feedback;

/**
 * Created by Claudio Redi on 1/29/2016.
 */
public class AbstractFeedback2FeedbackDTO<S extends Feedback> extends AbstractConverter<S, FeedbackDTO> {
    @Override
    protected FeedbackDTO convert(S source) {
        FeedbackDTO feedbackDTO = new FeedbackDTO();
        try {
            feedbackDTO.setId(source.getId());
            feedbackDTO.setRate(source.getRate());
            feedbackDTO.setReview(source.getReview());
            feedbackDTO.setOwner(source.getOwner().getFullName());
            feedbackDTO.setEventName(source.getEvent().getName());
            feedbackDTO.setEventId(source.getEvent().getId());
            feedbackDTO.setTimeAgo(getTimeAgo(source));
            feedbackDTO.setDate(source.getDate());
        }
        catch (Exception e) {
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            throw e;
        }
        return feedbackDTO;
    }

    private String getTimeAgo(S source) {
        return DateUtils.getRelativeTimeSpanString(
                source.getDate().getTime(),
                System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE).toString();
    }
}
