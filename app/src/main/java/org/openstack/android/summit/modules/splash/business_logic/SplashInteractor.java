package org.openstack.android.summit.modules.splash.business_logic;

import android.app.Activity;
import android.content.Context;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.security.ISecurityManager;

/**
 * Created by smarcet on 2/6/17.
 */

public class SplashInteractor extends BaseInteractor implements ISplashInteractor {

    private ISession session;

    public SplashInteractor
    (
        ISummitDataStore summitDataStore,
        ISecurityManager securityManager,
        IDTOAssembler dtoAssembler,
        ISession session,
        ISummitSelector summitSelector,
        IReachability reachability
    )
    {
        super(securityManager, dtoAssembler, summitSelector, summitDataStore, reachability);
        this.session  = session;
    }


    @Override
    public void login(Context context) {
        securityManager.login((Activity)context);
    }


    private final static String BuildNumber = "build-number";

    @Override
    public void setInstalledBuildNumber(int buildNumber){
        session.setInt(BuildNumber, buildNumber);
    }

    @Override
    public int getInstalledBuildNumber(){
        return session.getInt(BuildNumber);
    }

    @Override
    public void upgradeStorage() {
        // clear data update state
        if(securityManager.isLoggedIn()){
            securityManager.logout(false);
        }
        summitDataStore.clearDataLocal();
    }
}
