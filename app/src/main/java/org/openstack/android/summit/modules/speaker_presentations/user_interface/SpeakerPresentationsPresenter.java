package org.openstack.android.summit.modules.speaker_presentations.user_interface;

import android.os.Bundle;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.user_interface.IScheduleItemViewBuilder;
import org.openstack.android.summit.common.user_interface.IScheduleablePresenter;
import org.openstack.android.summit.common.user_interface.SchedulePresenter;
import org.openstack.android.summit.modules.speaker_presentations.ISpeakerPresentationsWireframe;
import org.openstack.android.summit.modules.speaker_presentations.business_logic.ISpeakerPresentationsInteractor;

import java.util.List;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public class SpeakerPresentationsPresenter
        extends SchedulePresenter<ISpeakerPresentationsView,
        ISpeakerPresentationsInteractor,
        ISpeakerPresentationsWireframe>
        implements ISpeakerPresentationsPresenter {

    private Integer speakerId;
    private Boolean isMyProfile;

    public SpeakerPresentationsPresenter(ISpeakerPresentationsInteractor interactor, ISpeakerPresentationsWireframe wireframe, IScheduleablePresenter scheduleablePresenter, IScheduleItemViewBuilder scheduleItemViewBuilder, IScheduleFilter scheduleFilter) {
        super(interactor, wireframe, scheduleablePresenter, scheduleItemViewBuilder, scheduleFilter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            isMyProfile = (savedInstanceState != null) ?
                    savedInstanceState.getBoolean(Constants.NAVIGATION_PARAMETER_IS_MY_PROFILE, false) :
                    wireframe.getParameter(Constants.NAVIGATION_PARAMETER_IS_MY_PROFILE, Boolean.class);
        }
        catch(Exception ex){
            isMyProfile = false;
        }

        try{
            if (isMyProfile != null && isMyProfile) {
                speakerId = interactor.getCurrentMemberSpeakerId();
            } else {
                speakerId = (savedInstanceState != null) ?
                        savedInstanceState.getInt(Constants.NAVIGATION_PARAMETER_SPEAKER, 0) :
                        wireframe.getParameter(Constants.NAVIGATION_PARAMETER_SPEAKER, Integer.class);
            }
        }
        catch(Exception ex){
            speakerId = 0;
        }
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected List<ScheduleItemDTO> getScheduleEvents(DateTime startDate, DateTime endDate, ISpeakerPresentationsInteractor interactor) {
        return interactor.getSpeakerPresentations(speakerId != null ? speakerId : 0, startDate, endDate);
    }

    @Override
    protected List<DateTime> getDatesWithoutEvents(DateTime startDate, DateTime endDate) {
        return interactor.getSpeakerPresentationScheduleDatesWithoutEvents
        (
            speakerId != null ? speakerId : 0,
            startDate,
            endDate
        );
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (isMyProfile != null) {
            outState.putBoolean(Constants.NAVIGATION_PARAMETER_IS_MY_PROFILE, isMyProfile);
        }
        if (speakerId != null) {
            outState.putInt(Constants.NAVIGATION_PARAMETER_SPEAKER, speakerId);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onResume() {
        this.shouldShowNow = false;
        super.onResume();
    }

}