package org.openstack.android.summit.modules.events.user_interface;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.SlidingTabLayout;
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
public class EventsFragment extends BaseFragment implements ViewPager.OnPageChangeListener, SlidingTabLayout.TabColorizer, IEventsView {

    @Inject
    IEventsPresenter presenter;

    @Inject
    GeneralScheduleFragment generalScheduleFragment;

    @Inject
    TrackListFragment trackListFragment;

    @Inject
    LevelListFragment levelListFragment;

    private int selectedTabIndex;
    private Menu menu;
    private boolean showActiveFilterIndicator;

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        presenter.setView(this);

        setTitle(getResources().getString(R.string.events));

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);

        MenuItem filterItem = menu.findItem(R.id.action_filter);
        Drawable newIcon = filterItem.getIcon();
        int color;
        if (showActiveFilterIndicator) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                color = view.getResources().getColor(R.color.activeFilters, null);
            }
            else {
                color = view.getResources().getColor(R.color.activeFilters);
            }
        }
        else {
            color = Color.WHITE;
        }

        newIcon.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        filterItem.setIcon(newIcon);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            presenter.showFilterView();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        presenter.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_events_container, container, false);

        SlidingTabLayout tabs = (SlidingTabLayout)view.findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(this);
        tabs.setOnPageChangeListener(this);

        ViewPager eventsViewPager = (ViewPager)view.findViewById(R.id.events_pager);
        EventsPageAdapter eventsPageAdapter = new EventsPageAdapter(getChildFragmentManager());
        eventsViewPager.setAdapter(eventsPageAdapter);
        eventsViewPager.setCurrentItem(selectedTabIndex);

        tabs.setViewPager(eventsViewPager);

        LinearLayout activeFiltersIndicator = (LinearLayout)view.findViewById(R.id.active_filters_indicator);
        activeFiltersIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.clearFilters();
            }
        });

        presenter.onCreate(savedInstanceState);

        return view;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        selectedTabIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public int getIndicatorColor(int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getResources().getColor(R.color.white, null);
        } else {
            return getResources().getColor(R.color.white);
        }
    }

    @Override
    public void setShowActiveFilterIndicator(boolean showActiveFilterIndicator) {
        LinearLayout activeFiltersIndicator = (LinearLayout)view.findViewById(R.id.active_filters_indicator);
        activeFiltersIndicator.setVisibility(showActiveFilterIndicator ? View.VISIBLE : View.GONE);
        this.showActiveFilterIndicator = showActiveFilterIndicator;
    }

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
