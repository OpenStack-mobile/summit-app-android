package org.openstack.android.summit.modules.level_list.user_interface;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.SimpleListItemView;

import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class LevelListFragment extends BaseFragment implements ILevelListView {

    @Inject
    ILevelListPresenter presenter;

    private LevelListAdapter levelListAdapter;

    public LevelListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        presenter.setView(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        setTitle(getResources().getString(R.string.events));
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_level_list, container, false);
        this.view = view;
        ListView levelList = (ListView)view.findViewById(R.id.list_levels);
        levelListAdapter = new LevelListAdapter(getContext());
        levelList.setAdapter(levelListAdapter);
        presenter.onCreate(savedInstanceState);
        return view;
    }

    public void setLevels(List<String> levels) {
        levelListAdapter.clear();
        levelListAdapter.addAll(levels);
    }

    public void reloadData() {
        levelListAdapter.notifyDataSetChanged();
    }

    private class LevelListAdapter extends ArrayAdapter<String> {

        public LevelListAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_simple_list, parent, false);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.showLevelEvents(position);
                }
            });

            final SimpleListItemView levelListItemView = new SimpleListItemView(convertView);

            presenter.buildItem(levelListItemView, position);

            // Return the completed view to render on screen
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount();
        };
    }
}
