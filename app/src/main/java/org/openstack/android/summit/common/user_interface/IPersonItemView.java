package org.openstack.android.summit.common.user_interface;

import android.net.Uri;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public interface IPersonItemView {
    void setName(String name);

    void setTitle(String title);

    void setPictureUri(Uri pictureUri);
}

