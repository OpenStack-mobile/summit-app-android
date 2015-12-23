package org.openstack.android.summit.modules.events.user_interface;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.openstack.android.summit.modules.general_schedule.user_interface.GeneralScheduleFragment;

/**
 * Created by Claudio Redi on 12/20/2015.
 */
public class EventsPageAdapter extends FragmentPagerAdapter {
    public EventsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0: return "SCHEDULE";
            case 1: return "TRACKS";
            case 2: return "LEVELS";
            default: return "";
        }
    }

    @Override
    public Fragment getItem(int i) {

        return new GeneralScheduleFragment();
    }
}
