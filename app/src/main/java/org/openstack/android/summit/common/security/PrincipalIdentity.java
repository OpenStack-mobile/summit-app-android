package org.openstack.android.summit.common.security;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.ISession;

/**
 * Created by smarcet on 1/31/17.
 */

public class PrincipalIdentity implements IPrincipalIdentity {

    private ISession session;
    private int currentMemberId = 0;

    public PrincipalIdentity(ISession session){
        this.session = session;
    }

    @Override
    public void setCurrentMemberId(int memberId) {
        this.currentMemberId = memberId;
        session.setInt(Constants.CURRENT_MEMBER_ID, memberId);
    }

    @Override
    public void clearCurrentMember() {
        this.currentMemberId = 0;
        session.setInt(Constants.CURRENT_MEMBER_ID, 0);
    }

    @Override
    public int getCurrentMemberId() {
        if(this.currentMemberId == 0){
            this.currentMemberId = session.getInt(Constants.CURRENT_MEMBER_ID);
        }
        return this.currentMemberId;
    }

}
