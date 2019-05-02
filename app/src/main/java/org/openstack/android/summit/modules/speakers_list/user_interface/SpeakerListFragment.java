package org.openstack.android.summit.modules.speakers_list.user_interface;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.fast_scroll_recycler_view.FastScrollRecyclerViewItemDecoration;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public class SpeakerListFragment extends BaseFragment<ISpeakerListPresenter> implements ISpeakerListView {

    private View view;
    private Unbinder unbinder;
    private SpeakerListAdapter speakerListAdapter;

    @BindView(R.id.list_speakers) RecyclerView speakerList;

    public SpeakerListFragment() {
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
        setTitle(getResources().getString(R.string.speakers));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_speaker_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        speakerList.setHasFixedSize(true);

        LinearLayoutManager layoutManager  = new LinearLayoutManager(getContext());

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        speakerList.setLayoutManager(layoutManager);

        speakerListAdapter  = new SpeakerListAdapter(presenter);

        speakerList.setAdapter(speakerListAdapter);

        speakerList.addItemDecoration(new FastScrollRecyclerViewItemDecoration(getContext()));

        presenter.onCreateView(savedInstanceState);

        return view;
    }

    @Override
    public void setSpeakers(List<PersonListItemDTO> speakers) {
        speakerListAdapter.clear();
        speakerListAdapter.addAll(speakers);
    }

    @Override
    public void setIndex(HashMap<String, Integer> mapIndex) {
        speakerListAdapter.setMapIndex(mapIndex);
    }
}
