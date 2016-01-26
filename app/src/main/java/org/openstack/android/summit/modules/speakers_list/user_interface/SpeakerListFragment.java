package org.openstack.android.summit.modules.speakers_list.user_interface;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.InfiniteScrollListener;
import org.openstack.android.summit.common.user_interface.PersonItemView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public class SpeakerListFragment extends BaseFragment implements ISpeakerListView {

    @Inject
    protected ISpeakerListPresenter presenter;
    SpeakerListAdapter speakerListAdapter;

    public SpeakerListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        presenter.setView(this);

        getActivity().setTitle("SPEAKERS");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_speaker_list, container, false);
        this.view = view;
        ListView speakerList = (ListView)view.findViewById(R.id.list_speakers);
        speakerListAdapter = new SpeakerListAdapter(getContext());
        speakerList.setAdapter(speakerListAdapter);
        speakerList.setOnScrollListener(new InfiniteScrollListener(presenter.getObjectsPerPage()) {
            @Override
            public void loadMore(int page, int totalItemsCount) {
                presenter.loadData();
            }
        });
        presenter.onCreate(savedInstanceState);
        return view;
    }

    @Override
    public void setSpeakers(List<PersonListItemDTO> speakers) {
        speakerListAdapter.clear();
        speakerListAdapter.addAll(speakers);
        speakerListAdapter.notifyDataSetChanged();
    }

    private class SpeakerListAdapter extends ArrayAdapter<PersonListItemDTO> {

        public SpeakerListAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_person_list, parent, false);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //presenter.showEventDetail(position);
                }
            });

            final PersonItemView personItemView = new PersonItemView(convertView);

            presenter.buildItem(personItemView, position);

            // Return the completed view to render on screen
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount();
        };
    }
}
