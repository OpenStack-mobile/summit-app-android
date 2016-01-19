package org.openstack.android.summit.modules.level_schedule.user_interface;

import org.joda.time.DateTime;
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
public class LevelSchedulePresenter extends SchedulePresenter<LevelScheduleFragment, ILevelScheduleInteractor, ILevelScheduleWireframe> implements ILevelSchedulePresenter {
    private String level;

    public LevelSchedulePresenter(ILevelScheduleInteractor interactor, ILevelScheduleWireframe wireframe, IScheduleablePresenter scheduleablePresenter, IScheduleItemViewBuilder scheduleItemViewBuilder) {
        super(interactor, wireframe, scheduleablePresenter, scheduleItemViewBuilder);
    }

    @Override
    protected List<ScheduleItemDTO> getScheduleEvents(DateTime startDate, DateTime endDate, ILevelScheduleInteractor interactor) {
        ArrayList<String> levels = new ArrayList<>();
        levels.add(level);
        List<ScheduleItemDTO> summitEvents = interactor.getScheduleEvents(
                startDate.toDate(), endDate.toDate(), null, null, null, null, levels);

        return summitEvents;
    }

    @Override
    public void setLevel(String level) {
        this.level = level;
    }
}
