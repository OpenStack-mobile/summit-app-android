package org.openstack.android.summit.modules.events.user_interface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.modules.general_schedule.user_interface.GeneralScheduleFragment;
import org.openstack.android.summit.modules.level_list.user_interface.LevelListFragment;
import org.openstack.android.summit.modules.track_list.user_interface.TrackListFragment;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsFragment extends BaseFragment {

    @Inject
    IEventsPresenter presenter;

    @Inject
    GeneralScheduleFragment generalScheduleFragment;

    @Inject
    TrackListFragment trackListFragment;

    @Inject
    LevelListFragment levelListFragment;

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        presenter.setView(this);
        presenter.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events_container, container, false);
        ViewPager eventsViewPager = (ViewPager)view.findViewById(R.id.events_pager);
        EventsPageAdapter eventsPageAdapter = new EventsPageAdapter(getActivity().getSupportFragmentManager());
        eventsViewPager.setAdapter(eventsPageAdapter);
        eventsViewPager.setCurrentItem(0);

        return view;
    }

    //@Override
    //public void

    private class EventsPageAdapter extends FragmentPagerAdapter {
        public EventsPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SCHEDULE";
                case 1:
                    return "TRACKS";
                case 2:
                    return "LEVELS";
                default:
                    return "";
            }
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return generalScheduleFragment;
                case 1:
                    return trackListFragment;
                case 2:
                    return levelListFragment;
                default:
                    return null;
            }
        }
    }
}
