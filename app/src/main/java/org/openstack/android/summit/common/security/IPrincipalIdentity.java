package org.openstack.android.summit.common.security;


/**
 * Created by smarcet on 1/31/17.
 */

public interface IPrincipalIdentity {

    void setCurrentMemberId(int memberId);

    void clearCurrentMember();

    int getCurrentMemberId();
}
