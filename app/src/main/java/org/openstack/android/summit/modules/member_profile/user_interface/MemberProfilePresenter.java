package org.openstack.android.summit.modules.member_profile.user_interface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.MemberDTO;
import org.openstack.android.summit.common.DTOs.PersonDTO;
import org.openstack.android.summit.common.security.MemberRole;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.modules.member_profile.IMemberProfileWireframe;
import org.openstack.android.summit.modules.member_profile.business_logic.IMemberProfileInteractor;

import java.util.List;

/**
 * Created by Claudio Redi on 1/26/2016.
 */
public class MemberProfilePresenter extends BasePresenter<IMemberProfileView, IMemberProfileInteractor, IMemberProfileWireframe> implements IMemberProfilePresenter {
    private int speakerId;
    private boolean isMyProfile;
    private List<MemberRole> roles;
    private MemberDTO myProfile;
    private PersonDTO speaker;
    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            wireframe.showEventsView(view);
        }
    };

    public MemberProfilePresenter(IMemberProfileInteractor interactor, IMemberProfileWireframe wireframe) {
        super(interactor, wireframe);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.LOGGED_IN_EVENT);
        intentFilter.addAction(Constants.LOGGED_OUT_EVENT);
        LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).registerReceiver(messageReceiver, intentFilter);

        if (savedInstanceState != null) {
            isMyProfile = savedInstanceState.getBoolean(Constants.NAVIGATION_PARAMETER_IS_MY_PROFILE);
        }
        else {
            isMyProfile = wireframe.getParameter(Constants.NAVIGATION_PARAMETER_IS_MY_PROFILE, Boolean.class);
        }

        if (isMyProfile) {
            myProfile = interactor.getCurrentMember();
        }
        else {
            if (savedInstanceState != null) {
                speakerId = savedInstanceState.getInt(Constants.NAVIGATION_PARAMETER_SPEAKER);
            }
            else {
                speakerId = wireframe.getParameter(Constants.NAVIGATION_PARAMETER_SPEAKER, Integer.class);
            }
            speaker = interactor.getPresentationSpeaker(speakerId);
        }

        view.setTitle("PROFILE");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(Constants.NAVIGATION_PARAMETER_IS_MY_PROFILE, isMyProfile);
        if (!isMyProfile) {
            outState.putInt(Constants.NAVIGATION_PARAMETER_SPEAKER, speakerId);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).unregisterReceiver(messageReceiver);
    }

    @Override
    public boolean getIsMyPofile() {
        return isMyProfile;
    }

    @Override
    public boolean getIsAttendee() {
        return isMyProfile ? true : false;
    }

    @Override
    public boolean getIsSpeaker() {
        return isMyProfile ? myProfile.getSpeakerRole() != null : true;
    }

    @Override
    public boolean showOrderConfirm() {
        return interactor.isLoggedInButNotRegisteredAttendee();
    }
}
