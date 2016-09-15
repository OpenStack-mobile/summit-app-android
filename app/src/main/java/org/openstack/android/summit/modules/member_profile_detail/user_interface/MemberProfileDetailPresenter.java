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
public class MemberProfileDetailPresenter
        extends BasePresenter<IMemberProfileDetailView, IMemberProfileDetailInteractor, IMemberProfileDetailWireframe>
        implements IMemberProfileDetailPresenter
{

    private Boolean isMyProfile;
    private Integer speakerId;
    private PersonDTO person;

    public MemberProfileDetailPresenter(IMemberProfileDetailInteractor interactor, IMemberProfileDetailWireframe wireframe) {
        super(interactor, wireframe);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isMyProfile == null) {
            isMyProfile = (savedInstanceState != null) ?
                    savedInstanceState.getBoolean(Constants.NAVIGATION_PARAMETER_IS_MY_PROFILE) :
                    wireframe.getParameter(Constants.NAVIGATION_PARAMETER_IS_MY_PROFILE, Boolean.class);
        }

        if (isMyProfile != null && isMyProfile) {
            MemberDTO myProfile = interactor.getCurrentMember();
            person = myProfile != null && myProfile.getSpeakerRole() != null ? myProfile.getSpeakerRole() : myProfile;
            speakerId = myProfile != null && myProfile.getSpeakerRole() != null ? myProfile.getSpeakerRole().getId() : 0;
        }
        else {
            speakerId = (savedInstanceState != null) ?
                    savedInstanceState.getInt(Constants.NAVIGATION_PARAMETER_SPEAKER) :
                    wireframe.getParameter(Constants.NAVIGATION_PARAMETER_SPEAKER, Integer.class);

            person   = interactor.getPresentationSpeaker(speakerId);
        }
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {

        if(person == null){
            view.setName("");
            view.setTitle("");
            view.setBio("");
            view.setTwitter("");
            view.setIrc("");
            view.showInfoMessage("Member profile not found!");
            return;
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
        if(isMyProfile != null) {
            outState.putBoolean(Constants.NAVIGATION_PARAMETER_IS_MY_PROFILE, isMyProfile);
        }
        if(speakerId != null){
            outState.putInt(Constants.NAVIGATION_PARAMETER_SPEAKER, speakerId);
        }
    }
}
