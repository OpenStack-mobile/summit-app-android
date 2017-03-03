package org.openstack.android.summit.common.user_interface;

/**
 * Created by smarcet on 3/2/17.
 */

public interface IScheduleableItem extends IScheduleableView, IFavoriteView {

    boolean isExternalRSVP();

    String getRSVPLink();

    void setExternalRSVP(boolean externalRSVP);

    void setRSVPLink(String link);
}
