package org.openstack.android.summit.modules.venues.user_interface;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.SlidingTabLayout;
import org.openstack.android.summit.modules.venue_list.user_interface.VenueListFragment;
import org.openstack.android.summit.modules.venues_map.user_interface.VenuesMapFragment;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public class VenuesFragment extends BaseFragment<IVenuesPresenter> implements ViewPager.OnPageChangeListener, SlidingTabLayout.TabColorizer, IVenuesView {

    @Inject
    VenueListFragment venueListFragment;

    @Inject
    VenuesMapFragment venuesMapFragment;
    
    private int selectedTabIndex;
    private Menu menu;

    public VenuesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(getResources().getString(R.string.venues));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        presenter.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_venues_container, container, false);

        SlidingTabLayout tabs = (SlidingTabLayout) view.findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(this);
        tabs.setOnPageChangeListener(this);

        ViewPager venuesViewPager = (ViewPager) view.findViewById(R.id.venues_pager);
        VenuesPageAdapter venuesPageAdapter = new VenuesPageAdapter(getChildFragmentManager());
        venuesViewPager.setAdapter(venuesPageAdapter);
        venuesViewPager.setCurrentItem(selectedTabIndex);

        tabs.setViewPager(venuesViewPager);
        
        super.onCreateView(inflater, container, savedInstanceState);

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
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return getResources().getColor(R.color.white, null);
            } else {
                return getResources().getColor(R.color.white);
            }
        }
        catch (Exception ex){
            return 0;
        }
    }

    private class VenuesPageAdapter extends FragmentPagerAdapter {
        public VenuesPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "MAP";
                case 1:
                    return "DIRECTORY";
                default:
                    return "";
            }
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return venuesMapFragment;
                case 1:
                    return venueListFragment;
                default:
                    return null;
            }
        }
    }
}
