package org.openstack.android.summit.common.api;

/**
 * Created by smarcet on 12/7/16.
 */

public interface ISummitSelector {

    int getCurrentSummitId();

    void setCurrentSummitId(int summitId);

    void clearCurrentSummit();
}
