package org.openstack.android.summit.modules.member_profile.user_interface;

import android.os.Bundle;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.MemberDTO;
import org.openstack.android.summit.common.DTOs.PersonDTO;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.modules.member_profile.IMemberProfileWireframe;
import org.openstack.android.summit.modules.member_profile.business_logic.IMemberProfileInteractor;

/**
 * Created by Claudio Redi on 1/26/2016.
 */
public class MemberProfilePresenter extends BasePresenter<IMemberProfileView, IMemberProfileInteractor, IMemberProfileWireframe> implements IMemberProfilePresenter {
    private int speakerId;
    private boolean isMyProfile;
    private MemberDTO myProfile;
    private PersonDTO speaker;

    public MemberProfilePresenter(IMemberProfileInteractor interactor, IMemberProfileWireframe wireframe) {
        super(interactor, wireframe);
    }

    @Override
    public void onResume() {
        super.onResume();
        String title = myProfile != null ?
                view.getResources().getString(R.string.nav_item_my_summit):
                view.getResources().getString(R.string.nav_item_profile);

        view.setTitle(title);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(Constants.NAVIGATION_PARAMETER_IS_MY_PROFILE, isMyProfile);
        if (!isMyProfile) {
            outState.putInt(Constants.NAVIGATION_PARAMETER_SPEAKER, speakerId);
        }
    }

    @Override
    public boolean getIsMyPofile() {
        return isMyProfile;
    }

    @Override
    public boolean getIsAttendee() {
        return isMyProfile && myProfile != null &&  myProfile.getAttendeeRole() != null ? true: false;
    }

    @Override
    public boolean getIsSpeaker() {
        return isMyProfile  && myProfile != null && myProfile.getSpeakerRole() != null?  true : false;
    }

    @Override
    public boolean getIsMember() {
        return isMyProfile && myProfile != null ? true : false;
    }

    @Override
    public boolean showOrderConfirm() {
        return !interactor.isLoggedInAndConfirmedAttendee();
    }
}
