package org.openstack.android.summit.modules.teams_notifications_inbox.user_interface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.TeamDTO;
import org.openstack.android.summit.common.user_interface.BaseFragment;

import java.util.List;

/**
 * Created by smarcet on 2/13/17.
 */

public class TeamsListFragment
        extends BaseFragment<ITeamsListPresenter>
        implements ITeamsListView
{

    public TeamsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
        presenter.loadData();
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teams_list, container, false);
        this.view = view;
        super.onCreateView(inflater, container, savedInstanceState);

        return view;
    }

    @Override
    public void setData(List<TeamDTO> list) {

    }
}
