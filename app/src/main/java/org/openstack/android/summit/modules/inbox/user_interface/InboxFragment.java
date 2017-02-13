package org.openstack.android.summit.modules.inbox.user_interface;

import android.os.Build;
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
import org.openstack.android.summit.common.user_interface.SlidingTabLayout;
import org.openstack.android.summit.modules.push_notifications_inbox.user_interface.PushPushNotificationsListFragment;
import org.openstack.android.summit.modules.teams_notifications_inbox.user_interface.TeamsListFragment;

import javax.inject.Inject;

/**
 * Created by smarcet on 2/7/17.
 */

public class InboxFragment
        extends BaseFragment<IInboxPresenter>
        implements ViewPager.OnPageChangeListener, SlidingTabLayout.TabColorizer, IInboxView
{

    private int selectedTabIndex;

    @Inject
    PushPushNotificationsListFragment pushPushNotificationsListFragment;

    @Inject
    TeamsListFragment teamsListFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.inbox));
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(getResources().getString(R.string.inbox));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_inbox_container, container, false);

        SlidingTabLayout tabs = (SlidingTabLayout)view.findViewById(R.id.tabs);

        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(this);
        tabs.setOnPageChangeListener(this);

        ViewPager eventsViewPager = (ViewPager)view.findViewById(R.id.inbox_pager);
        InboxFragment.InboxPageAdapter inboxPageAdapter = new InboxFragment.InboxPageAdapter(getChildFragmentManager());
        eventsViewPager.setAdapter(inboxPageAdapter);
        eventsViewPager.setCurrentItem(selectedTabIndex);

        tabs.setViewPager(eventsViewPager);

        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        presenter.onSaveInstanceState(outState);
    }

    private class InboxPageAdapter extends FragmentPagerAdapter {
        public InboxPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return presenter.getTabsToShow();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "NOTIFICATIONS";
                case 1:
                    return "TEAMS";
                default:
                    return "";
            }
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return pushPushNotificationsListFragment;
                case 1:
                    return teamsListFragment;

            }
            return null;
        }
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
}
