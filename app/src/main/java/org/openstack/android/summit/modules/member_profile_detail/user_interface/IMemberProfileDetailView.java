package org.openstack.android.summit.modules.member_profile_detail.user_interface;

import android.net.Uri;
import androidx.appcompat.app.AlertDialog;

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

    void showAddEventBriteOrderContainer(boolean show);

    void showEventBriteOrderAdded(boolean show);

    AlertDialog createNotAttendeeAlertDialog();

    void setShowMissingEventBriteOrderIndicator(boolean show);
}
