package org.openstack.android.summit.modules.member_profile_detail.user_interface;

import android.net.Uri;

import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by Claudio Redi on 1/28/2016.
 */
public interface IMemberProfileDetailView extends IBaseView {
    void setName(String name);

    void setTitle(String title);

    void setPictureUri(Uri pictureUri);

    void setBio(String bio);

    void setLocation(String location);

    void setMail(String mail);

    void setTwitter(String twitter);

    void setIrc(String irc);
}
