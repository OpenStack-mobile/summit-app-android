package org.openstack.android.summit.common.security;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.ISession;

/**
 * Created by smarcet on 1/31/17.
 */

public class PrincipalIdentity implements IPrincipalIdentity {

    private ISession session;

    public PrincipalIdentity(ISession session){
        this.session = session;
    }

    @Override
    public void setCurrentMemberId(int memberId) {
        session.setInt(Constants.CURRENT_MEMBER_ID, memberId);
    }

    @Override
    public void clearCurrentMember() {
        session.setInt(Constants.CURRENT_MEMBER_ID, 0);
    }

    @Override
    public int getCurrentMemberId() {
        return session.getInt(Constants.CURRENT_MEMBER_ID);
    }

}
