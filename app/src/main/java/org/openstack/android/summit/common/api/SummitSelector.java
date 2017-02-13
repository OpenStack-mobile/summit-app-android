package org.openstack.android.summit.common.api;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.ISession;

/**
 * Created by smarcet on 11/21/16.
 */

public class SummitSelector implements ISummitSelector {

    private ISession session;

    public SummitSelector(ISession session){
        this.session = session;
    }

    public int getCurrentSummitId(){
        return session.getInt(Constants.CURRENT_SUMMIT_ID);
    }

    public void setCurrentSummitId(int summitId){
        session.setInt(Constants.CURRENT_SUMMIT_ID, summitId);
    }

}
