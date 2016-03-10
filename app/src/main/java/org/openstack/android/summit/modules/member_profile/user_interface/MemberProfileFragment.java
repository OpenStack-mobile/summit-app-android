package org.openstack.android.summit.modules.member_profile.user_interface;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.SlidingTabLayout;
import org.openstack.android.summit.modules.feedback_given_list.user_interface.FeedbackGivenListFragment;
import org.openstack.android.summit.modules.general_schedule.user_interface.GeneralScheduleFragment;
import org.openstack.android.summit.modules.level_list.user_interface.LevelListFragment;
import org.openstack.android.summit.modules.member_profile_detail.user_interface.MemberProfileDetailFragment;
import org.openstack.android.summit.modules.personal_schedule.user_interface.PersonalScheduleFragment;
import org.openstack.android.summit.modules.speaker_presentations.user_interface.SpeakerPresentationsFragment;
import org.openstack.android.summit.modules.track_list.user_interface.TrackListFragment;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/26/2016.
 */
public class MemberProfileFragment extends BaseFragment<IMemberProfilePresenter> implements ViewPager.OnPageChangeListener, SlidingTabLayout.TabColorizer, IMemberProfileView {
    @Inject
    PersonalScheduleFragment personalScheduleFragment;

    @Inject
    MemberProfileDetailFragment memberProfileDetailFragment;

    @Inject
    FeedbackGivenListFragment feedbackGivenListFragment;

    @Inject
    SpeakerPresentationsFragment speakerPresentationsFragment;

    private int selectedTabIndex;

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
        setTitle(getResources().getString(R.string.profile));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        presenter.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_member_profile_container, container, false);

        SlidingTabLayout tabs = (SlidingTabLayout)view.findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(this);
        tabs.setOnPageChangeListener(this);

        ViewPager eventsViewPager = (ViewPager)view.findViewById(R.id.member_profile_pager);
        MemberProfilePageAdapter memberProfilePageAdapter = new MemberProfilePageAdapter(getChildFragmentManager());
        eventsViewPager.setAdapter(memberProfilePageAdapter);
        eventsViewPager.setCurrentItem(selectedTabIndex);

        tabs.setViewPager(eventsViewPager);

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
                else if (presenter.getIsAttendee()) {
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
                    return getPageTitleForMyProfileAsSpeaker(position);
                }
                else if (presenter.getIsAttendee()) {
                    return getPageTitleForMyProfileAsAttendee(position);
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
                    return "PROFILE";
                case 1:
                    return "SESSIONS";
                default:
                    return "";
            }
        }

        private CharSequence getPageTitleForMyProfileAsAttendee(int position) {
            switch (position) {
                case 0:
                    return "SCHEDULE";
                case 1:
                    return "PROFILE";
                case 2:
                    return "FEEDBACK";
                default:
                    return "";
            }
        }

        private CharSequence getPageTitleForMyProfileAsSpeaker(int position) {
            switch (position) {
                case 0:
                    return "SCHEDULE";
                case 1:
                    return "PROFILE";
                case 2:
                    return "FEEDBACK";
                case 3:
                    return "SESSIONS";
                default:
                    return "";
            }
        }

        @Override
        public Fragment getItem(int i) {
            if (presenter.getIsMyPofile()){
                if (presenter.getIsSpeaker()) {
                    return getItemForMyProfileAsSpeaker(i);
                }
                else if (presenter.getIsAttendee()) {
                    return getItemForMyProfileAsAttendee(i);
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

        private Fragment getItemForMyProfileAsAttendee(int i) {
            switch (i) {
                case 0:
                    return personalScheduleFragment;
                case 1:
                    return memberProfileDetailFragment;
                case 2:
                    return feedbackGivenListFragment;
                default:
                    return null;
            }
        }

        private Fragment getItemForMyProfileAsSpeaker(int i) {
            switch (i) {
                case 0:
                    return personalScheduleFragment;
                case 1:
                    return memberProfileDetailFragment;
                case 2:
                    return feedbackGivenListFragment;
                case 3:
                    return speakerPresentationsFragment;
                default:
                    return null;
            }
        }
    }
}
