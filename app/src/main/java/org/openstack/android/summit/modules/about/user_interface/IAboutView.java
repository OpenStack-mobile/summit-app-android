package org.openstack.android.summit.modules.about.user_interface;

import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by Claudio Redi on 4/1/2016.
 */
public interface IAboutView extends IBaseView {
    void setSummitNameAndDate(String nameAndDate);

    void setVersion(String version);
}
