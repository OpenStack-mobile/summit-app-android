package org.openstack.android.summit.common.user_interface;

/**
 * Created by Claudio Redi on 1/28/2016.
 */
public interface IFeedbackItemView {
    void setDate(String date);

    void setEventTitle(String eventTitle);

    void setRate(int rate);

    void setOwner(String owner);

    void setReview(String review);
}
