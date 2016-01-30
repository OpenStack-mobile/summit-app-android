package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import android.text.format.DateUtils;

import org.modelmapper.AbstractConverter;
import org.openstack.android.summit.common.DTOs.FeedbackDTO;
import org.openstack.android.summit.common.entities.Feedback;

/**
 * Created by Claudio Redi on 1/29/2016.
 */
public class AbstractFeedback2FeedbackDTO<S extends Feedback> extends AbstractConverter<S, FeedbackDTO> {
    @Override
    protected FeedbackDTO convert(S source) {
        FeedbackDTO feedbackDTO = new FeedbackDTO();
        feedbackDTO.setId(source.getId());
        feedbackDTO.setRate(source.getRate());
        feedbackDTO.setReview(source.getReview());
        feedbackDTO.setOwner(source.getOwner().getFullName());
        feedbackDTO.setEventName(source.getEvent().getName());
        feedbackDTO.setEventId(source.getEvent().getId());
        feedbackDTO.setDate(getDate(source));
        return feedbackDTO;
    }

    private String getDate(S source) {
        return DateUtils.getRelativeTimeSpanString(
                source.getDate().getTime(),
                System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE).toString();
    }
}
