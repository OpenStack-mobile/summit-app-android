package org.openstack.android.summit.modules.member_profile_detail.user_interface;

import android.net.Uri;
import android.os.Bundle;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.MemberDTO;
import org.openstack.android.summit.common.DTOs.PersonDTO;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.modules.member_profile_detail.IMemberProfileDetailWireframe;
import org.openstack.android.summit.modules.member_profile_detail.business_logic.IMemberProfileDetailInteractor;

/**
 * Created by Claudio Redi on 1/26/2016.
 */
public class MemberProfileDetailPresenter extends BasePresenter<IMemberProfileDetailView, IMemberProfileDetailInteractor, IMemberProfileDetailWireframe> implements IMemberProfileDetailPresenter {
    private Boolean isMyProfile;
    private int speakerId;
    private PersonDTO person;

    public MemberProfileDetailPresenter(IMemberProfileDetailInteractor interactor, IMemberProfileDetailWireframe wireframe) {
        super(interactor, wireframe);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        if (isMyProfile == null) {
            if (savedInstanceState != null) {
                isMyProfile = savedInstanceState.getBoolean(Constants.NAVIGATION_PARAMETER_IS_MY_PROFILE);
            }
            else {
                isMyProfile = wireframe.getParameter(Constants.NAVIGATION_PARAMETER_IS_MY_PROFILE, Boolean.class);
            }
        }

        if (isMyProfile != null && isMyProfile) {
            MemberDTO myProfile = interactor.getCurrentMember();
            person = myProfile.getSpeakerRole() != null ? myProfile.getSpeakerRole() : myProfile.getAttendeeRole();
        }
        else {
            if (savedInstanceState != null) {
                speakerId = savedInstanceState.getInt(Constants.NAVIGATION_PARAMETER_SPEAKER);
            }
            else {
                speakerId = wireframe.getParameter(Constants.NAVIGATION_PARAMETER_SPEAKER, Integer.class);
            }
            person = interactor.getPresentationSpeaker(speakerId);
        }

        view.setName(person.getName());
        view.setTitle(person.getTitle());
        view.setBio(person.getBio());
        view.setTwitter(person.getTwitter());
        view.setIrc(person.getIrc());
        Uri uri = person.getPictureUrl() != null && !person.getPictureUrl().isEmpty()
            ? Uri.parse(person.getPictureUrl().replace("https", "http"))
            : null;
        view.setPictureUri(uri);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(Constants.NAVIGATION_PARAMETER_IS_MY_PROFILE, isMyProfile);
        if (!isMyProfile) {
            outState.putInt(Constants.NAVIGATION_PARAMETER_SPEAKER, speakerId);
        }
    }
}
