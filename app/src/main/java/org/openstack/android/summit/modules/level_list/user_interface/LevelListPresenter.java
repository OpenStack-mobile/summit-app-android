package org.openstack.android.summit.modules.level_list.user_interface;

import android.os.Bundle;

import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.common.user_interface.IPresenter;
import org.openstack.android.summit.common.user_interface.ISimpleListItemView;
import org.openstack.android.summit.common.user_interface.ScheduleItemView;
import org.openstack.android.summit.modules.level_list.ILevelListWireframe;
import org.openstack.android.summit.modules.level_list.business_logic.ILevelListInteractor;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/11/2016.
 */
public class LevelListPresenter extends BasePresenter implements ILevelListPresenter {

    @Inject
    public LevelListPresenter(ILevelListInteractor interactor, ILevelListWireframe wireframe) {
        this.interactor = interactor;
        this.wireframe = wireframe;
    }

    private ILevelListWireframe wireframe;
    private ILevelListInteractor interactor;
    private LevelListFragment view;
    private List<String> levels;

    @Override
    public void setView(LevelListFragment view) {
        this.view = view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        levels = interactor.getLevels();
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