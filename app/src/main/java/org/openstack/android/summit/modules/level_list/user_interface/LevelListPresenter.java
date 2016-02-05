package org.openstack.android.summit.modules.level_list.user_interface;

import android.os.Bundle;

import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.common.user_interface.ISimpleListItemView;
import org.openstack.android.summit.modules.general_schedule_filter.user_interface.FilterSectionType;
import org.openstack.android.summit.modules.level_list.ILevelListWireframe;
import org.openstack.android.summit.modules.level_list.business_logic.ILevelListInteractor;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/11/2016.
 */
public class LevelListPresenter extends BasePresenter<LevelListFragment, ILevelListInteractor, ILevelListWireframe> implements ILevelListPresenter {
    private IScheduleFilter scheduleFilter;

    @Inject
    public LevelListPresenter(ILevelListInteractor interactor, ILevelListWireframe wireframe, IScheduleFilter scheduleFilter) {
        super(interactor, wireframe);
        this.scheduleFilter = scheduleFilter;
    }

    private List<String> levels;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        List<String> filtersOnLevels = (List<String>)(List<?>) scheduleFilter.getSelections().get(FilterSectionType.Level);
        levels = filtersOnLevels != null && filtersOnLevels.size() > 0 ? filtersOnLevels : interactor.getLevels();
        view.setLevels(levels);
        view.reloadData();
    }

    @Override
    public void showLevelEvents(int position) {
        String level = levels.get(position);
        wireframe.showLevelSchedule(level, view.getActivity());
    }

    @Override
    public void buildItem(ISimpleListItemView levelListItemView, int position) {
        String name = levels.get(position);
        levelListItemView.setName(name);
    }
}