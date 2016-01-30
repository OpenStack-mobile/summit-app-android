package org.openstack.android.summit.modules.feedback_given_list.user_interface;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.FeedbackDTO;
import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.FeedbackItemView;
import org.openstack.android.summit.common.user_interface.IFeedbackItemView;
import org.openstack.android.summit.common.user_interface.InfiniteScrollListener;
import org.openstack.android.summit.common.user_interface.PersonItemView;
import org.openstack.android.summit.common.user_interface.SlidingTabLayout;
import org.openstack.android.summit.modules.speakers_list.user_interface.ISpeakerListPresenter;
import org.openstack.android.summit.modules.speakers_list.user_interface.ISpeakerListView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public class FeedbackGivenListFragment extends BaseFragment implements IFeedbackGivenListView {

    @Inject
    protected IFeedbackGivenListPresenter presenter;
    FeedbackGivenListAdapter feedbackGivenListAdapter;

    public FeedbackGivenListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        presenter.setView(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback_given_list, container, false);
        this.view = view;
        ListView speakerList = (ListView)view.findViewById(R.id.list_feedback);
        feedbackGivenListAdapter = new FeedbackGivenListAdapter(getContext());
        speakerList.setAdapter(feedbackGivenListAdapter);
        presenter.onCreate(savedInstanceState);
        return view;
    }

    @Override
    public void setFeedbackList(List<FeedbackDTO> feedbackList) {
        feedbackGivenListAdapter.clear();
        feedbackGivenListAdapter.addAll(feedbackList);
        feedbackGivenListAdapter.notifyDataSetChanged();
    }

    private class FeedbackGivenListAdapter extends ArrayAdapter<FeedbackDTO> {

        public FeedbackGivenListAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_feedback_list, parent, false);
            }

            final IFeedbackItemView feedbackItemView = new FeedbackItemView(convertView);

            presenter.buildItem(feedbackItemView, position);

            // Return the completed view to render on screen
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount();
        };
    }
}