package org.openstack.android.summit.modules.level_schedule.user_interface;

import android.os.Bundle;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.user_interface.IScheduleItemViewBuilder;
import org.openstack.android.summit.common.user_interface.IScheduleablePresenter;
import org.openstack.android.summit.common.user_interface.SchedulePresenter;
import org.openstack.android.summit.modules.level_schedule.ILevelScheduleWireframe;
import org.openstack.android.summit.modules.level_schedule.business_logic.ILevelScheduleInteractor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 1/11/2016.
 */
public class LevelSchedulePresenter extends SchedulePresenter<ILevelScheduleView, ILevelScheduleInteractor, ILevelScheduleWireframe> implements ILevelSchedulePresenter {
    private String level;

    public LevelSchedulePresenter(ILevelScheduleInteractor interactor, ILevelScheduleWireframe wireframe, IScheduleablePresenter scheduleablePresenter, IScheduleItemViewBuilder scheduleItemViewBuilder) {
        super(interactor, wireframe, scheduleablePresenter, scheduleItemViewBuilder);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            level = savedInstanceState.getString(Constants.NAVIGATION_PARAMETER_LEVEL);
        }
        else {
            level = wireframe.getParameter(Constants.NAVIGATION_PARAMETER_LEVEL, String.class);
        }
        view.setTitle(level.toUpperCase() + " LEVEL");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.NAVIGATION_PARAMETER_LEVEL, level);
    }

    @Override
    protected List<ScheduleItemDTO> getScheduleEvents(DateTime startDate, DateTime endDate, ILevelScheduleInteractor interactor) {
        ArrayList<String> levels = new ArrayList<>();
        levels.add(level);
        List<ScheduleItemDTO> summitEvents = interactor.getScheduleEvents(
                startDate.toDate(), endDate.toDate(), null, null, null, null, levels);

        return summitEvents;
    }
}
