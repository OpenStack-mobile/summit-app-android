package org.openstack.android.summit.modules.speaker_presentations.user_interface;

import android.os.Bundle;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.common.user_interface.IScheduleItemViewBuilder;
import org.openstack.android.summit.common.user_interface.IScheduleablePresenter;
import org.openstack.android.summit.common.user_interface.SchedulePresenter;
import org.openstack.android.summit.modules.speaker_presentations.ISpeakerPresentationsWireframe;
import org.openstack.android.summit.modules.speaker_presentations.business_logic.ISpeakerPresentationsInteractor;
import org.openstack.android.summit.modules.speaker_presentations.business_logic.ISpeakerPresentationsInteractor;

import java.util.List;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public class SpeakerPresentationsPresenter  extends SchedulePresenter<ISpeakerPresentationsView, ISpeakerPresentationsInteractor, ISpeakerPresentationsWireframe> implements ISpeakerPresentationsPresenter {
    private int speakerId;

    public SpeakerPresentationsPresenter(ISpeakerPresentationsInteractor interactor, ISpeakerPresentationsWireframe wireframe, IScheduleablePresenter scheduleablePresenter, IScheduleItemViewBuilder scheduleItemViewBuilder, IScheduleFilter scheduleFilter) {
        super(interactor, wireframe, scheduleablePresenter, scheduleItemViewBuilder, scheduleFilter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        boolean isMyProfile;
        if (savedInstanceState != null) {
            isMyProfile = savedInstanceState.getBoolean(Constants.NAVIGATION_PARAMETER_IS_MY_PROFILE);
        }
        else {
            isMyProfile = wireframe.getParameter(Constants.NAVIGATION_PARAMETER_IS_MY_PROFILE, Boolean.class);
        }

        if (isMyProfile) {
            speakerId = interactor.getCurrentMemberSpeakerId();
        }
        else {
            if (savedInstanceState != null) {
                speakerId = savedInstanceState.getInt(Constants.NAVIGATION_PARAMETER_SPEAKER);
            }
            else {
                speakerId = wireframe.getParameter(Constants.NAVIGATION_PARAMETER_SPEAKER, Integer.class);
            }
        }

        view.setTitle("PROFILE");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected List<ScheduleItemDTO> getScheduleEvents(DateTime startDate, DateTime endDate, ISpeakerPresentationsInteractor interactor) {
        List<ScheduleItemDTO> events = interactor.getSpeakerPresentations(speakerId, startDate.toDate(), endDate.toDate());
        return events;
    }
}