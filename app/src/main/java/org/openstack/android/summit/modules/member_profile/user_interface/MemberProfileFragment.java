package org.openstack.android.summit.modules.member_profile.user_interface;

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
import org.openstack.android.summit.common.user_interface.tabs.FragmentLifecycle;
import org.openstack.android.summit.modules.favorites_schedule.user_interface.FavoritesScheduleFragment;
import org.openstack.android.summit.modules.member_profile_detail.user_interface.MemberProfileDetailFragment;
import org.openstack.android.summit.modules.personal_schedule.user_interface.PersonalScheduleFragment;
import org.openstack.android.summit.modules.speaker_presentations.user_interface.SpeakerPresentationsFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Claudio Redi on 1/26/2016.
 */
public class MemberProfileFragment
        extends BaseFragment<IMemberProfilePresenter>
        implements ViewPager.OnPageChangeListener,
        SlidingTabLayout.TabColorizer,
        IMemberProfileView {

    protected Unbinder unbinder;

    @Inject
    PersonalScheduleFragment personalScheduleFragment;

    @Inject
    FavoritesScheduleFragment favoritesScheduleFragment;

    @Inject
    MemberProfileDetailFragment memberProfileDetailFragment;

    @Inject
    SpeakerPresentationsFragment speakerPresentationsFragment;

    private MemberProfilePageAdapter memberProfilePageAdapter;

    @BindView(R.id.tabs)
    SlidingTabLayout tabs;

    @BindView(R.id.member_profile_pager)
    ViewPager eventsViewPager;

    private int selectedTabIndex = 0;

    public MemberProfileFragment() {
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
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        if(unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        presenter.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_member_profile_container, container, false);

        unbinder  = ButterKnife.bind(this, view);

        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(this);
        tabs.setOnPageChangeListener(this);

        memberProfilePageAdapter = new MemberProfilePageAdapter(getChildFragmentManager());
        eventsViewPager.setAdapter(memberProfilePageAdapter);

        tabs.setViewPager(eventsViewPager);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int newTabIndex) {

        FragmentLifecycle fragmentToShow = (FragmentLifecycle)memberProfilePageAdapter.getItem(newTabIndex);
        fragmentToShow.onResumeFragment();

        FragmentLifecycle fragmentToHide = (FragmentLifecycle)memberProfilePageAdapter.getItem(selectedTabIndex);
        fragmentToHide.onPauseFragment();

        selectedTabIndex = newTabIndex;

        presenter.onPageSelected(newTabIndex);
    }

    @Override
    public int getCurrentTabsCount(){
        if(memberProfilePageAdapter == null) return 0;
        return memberProfilePageAdapter.getCount() ;
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
            return -1;
        }
    }

    @Override
    public void setCurrentTabByIndex(int tabIndex) {
        if(eventsViewPager == null) return;
        selectedTabIndex = tabIndex;
        eventsViewPager.setCurrentItem(tabIndex);
    }

    private class MemberProfilePageAdapter extends FragmentPagerAdapter {

        public MemberProfilePageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            int count = 0;
            if (presenter.getIsMyPofile()){
                if (presenter.getIsSpeaker()) {
                    count = 4;
                }
                else if (presenter.getIsMember()) {
                    count = 3;
                }
            }
            else {
                count = 2;
            }
            return count;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (presenter.getIsMyPofile()){
                if (presenter.getIsSpeaker()) {
                    return getPageTitleForMyProfileAsSpeaker(position, presenter.getIsAttendee());
                }
                else if (presenter.getIsMember()){
                    return getPageTitleForMyProfileAsMember(position);
                }
            }
            else {
                return getPageTitleForOtherProfileAsSpeaker(position);
            }

            return "";
        }

        private CharSequence getPageTitleForOtherProfileAsSpeaker(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.my_summit_profile_tab_title);
                case 1:
                    return getResources().getString(R.string.my_summit_sessions_tab_title);
                default:
                    return "";
            }
        }

        private CharSequence getPageTitleForMyProfileAsMember(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.my_summit_schedule_tab_title);
                case 1:
                    return getResources().getString(R.string.my_summit_favorites_tab_title);
                case 2:
                    return getResources().getString(R.string.my_summit_profile_tab_title);
                default:
                    return "";
            }
        }

        private CharSequence getPageTitleForMyProfileAsSpeaker(int position, boolean isAttendee) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.my_summit_sessions_tab_title);
                case 1:
                    return getResources().getString(R.string.my_summit_schedule_tab_title);
                case 2:
                    return getResources().getString(R.string.my_summit_favorites_tab_title);
                case 3:
                    return getResources().getString(R.string.my_summit_profile_tab_title);
                default:
                    return "";
            }
        }

        @Override
        public Fragment getItem(int i) {
            if (presenter.getIsMyPofile()){
                if (presenter.getIsSpeaker()) {
                    return getItemForMyProfileAsSpeaker(i, presenter.getIsAttendee());
                }
                else if (presenter.getIsMember()){
                    return getItemForMyProfileAsMember(i);
                }
            }
            else {
                return getItemForOtherProfileAsSpeaker(i);
            }

            return null;
        }

        private Fragment getItemForOtherProfileAsSpeaker(int i) {
            switch (i) {
                case 0:
                    return memberProfileDetailFragment;
                case 1:
                    return speakerPresentationsFragment;
                default:
                    return null;
            }
        }

        private Fragment getItemForMyProfileAsMember(int i) {
            switch (i) {
                case 0:
                    return personalScheduleFragment;
                case 1:
                    return favoritesScheduleFragment;
                case 2:
                    return memberProfileDetailFragment;
                default:
                    return null;
            }
        }

        private Fragment getItemForMyProfileAsSpeaker(int i, boolean isAttendee) {
            switch (i) {
                case 0:
                    return speakerPresentationsFragment;
                case 1:
                    return personalScheduleFragment;
                case 2:
                    return favoritesScheduleFragment;
                case 3:
                    return memberProfileDetailFragment;
                default:
                    return null;
            }
        }
    }
}
