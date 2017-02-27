package org.openstack.android.summit.common.user_interface;

import android.net.Uri;

/**
 * Created by Claudio Redi on 1/28/2016.
 */
public interface IFeedbackItemView {
    void setDate(String date);

    void setRate(int rate);

    void setOwner(String owner);

    void setOwnerPictureUri(Uri ownerPicUri);

    void setReview(String review);
}
