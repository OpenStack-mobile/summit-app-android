package org.openstack.android.summit.modules.push_notifications_inbox.user_interface;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.PushNotificationListItemDTO;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.InfiniteScrollListener;
import java.util.List;

/**
 * Created by sebastian on 8/19/2016.
 */
public class PushPushNotificationsListFragment
        extends BaseFragment<IPushNotificationsListPresenter>
        implements IPushNotificationsListView {

    private static final String SEARCH_KEY = "search-key-push-notification-list";
    private String searchQuery             = "";

    private PushNotificationListAdapter listAdapter;
    private SearchView searchView;
    private SearchView.OnQueryTextListener queryTextListener;


    public PushPushNotificationsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // if you saved something on outState you can recover them here
        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(SEARCH_KEY);
        }
    }

    // This is called before the activity is destroyed
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(searchView != null){
            searchQuery = searchView.getQuery().toString();
            outState.putString(SEARCH_KEY, searchQuery);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.push_notifications_search, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem         = menu.findItem(R.id.menu_item_push_notification_search);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }

        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextChange(String newText) {
                    searchQuery = newText;
                    presenter.loadDataByTerm(searchQuery);
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchQuery = query;
                    presenter.loadDataByTerm(searchQuery);
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);

            //focus the SearchView
            if (searchQuery != null && !searchQuery.isEmpty()) {
                searchView.setIconified(false);
                //MenuItemCompat.expandActionView(searchItem);
                searchView.setQuery(searchQuery, true);
                searchView.clearFocus();
                presenter.loadDataByTerm(searchQuery);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_push_notification_search:
                // Not implemented here
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
        setTitle(getResources().getString(R.string.notifications));
    }

    private ListView notificationsList;
    private TextView emptyMessage;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view         = inflater.inflate(R.layout.fragment_notifications_list, container, false);
        this.view         = view;
        notificationsList = (ListView)view.findViewById(R.id.list_notifications);
        listAdapter       = new PushNotificationListAdapter(getContext());
        emptyMessage      = (TextView)view.findViewById(R.id.list_notifications_empty_message);

        notificationsList.setAdapter(listAdapter);
        notificationsList.setOnScrollListener(new InfiniteScrollListener(presenter.getObjectsPerPage()) {
            @Override
            public void loadMore(int page, int totalItemsCount) {
                presenter.loadData();
            }
        });

        notificationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            presenter.showNotification(position);
            }
        });

        notificationsList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        // Capture ListView item click
        notificationsList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                // Capture total checked items
                final int checkedCount = notificationsList.getCheckedItemCount();
                // Set the CAB title according to total checked items
                mode.setTitle(checkedCount + " Selected");
                // Calls toggleSelection method from ListViewAdapter Class
                listAdapter.toggleSelection(position);
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_push_notification_delete:
                        // Calls getSelectedIds method from ListViewAdapter Class
                        SparseBooleanArray selected = listAdapter.getSelectedIds();
                        // Captures all selected ids with a loop
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                PushNotificationListItemDTO selectedItem = listAdapter.getItem(selected.keyAt(i));
                                // Remove selected items following the ids
                                presenter.onRemovePushNotification(selectedItem);
                                listAdapter.remove(selectedItem);
                                updateState();
                            }
                        }
                        // Close CAB
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.push_notifications, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                listAdapter.removeSelection();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return false;
            }
        });

        super.onCreateView(inflater, container, savedInstanceState);

        return view;
    }

    @Override
    public void setNotifications(List<PushNotificationListItemDTO> notifications) {
        if(listAdapter == null) return;
        listAdapter.clear();
        listAdapter.addAll(notifications);
        updateState();
    }

    @Override
    public void refresh()
    {
        if(listAdapter == null) return;
        listAdapter.notifyDataSetChanged();
    }

    private void updateState() {
        if(emptyMessage == null || notificationsList == null || listAdapter == null) return;
        boolean show = listAdapter.isEmpty();
        emptyMessage.setVisibility(show ? View.VISIBLE : View.GONE);
        notificationsList.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private class PushNotificationListAdapter extends ArrayAdapter<PushNotificationListItemDTO> {

        private SparseBooleanArray selectedItems;

        public PushNotificationListAdapter(Context context) {
            super(context, 0);
            selectedItems = new SparseBooleanArray();
        }

        public void removeSelection() {
            selectedItems = new SparseBooleanArray();
            notifyDataSetChanged();
        }

        public void toggleSelection(int position) {
            selectView(position, !selectedItems.get(position));
        }

        public void selectView(int position, boolean value) {
            if (value)
                selectedItems.put(position, value);
            else
                selectedItems.delete(position);
            notifyDataSetChanged();
        }

        public int getSelectedCount() {
            return selectedItems.size();
        }

        public SparseBooleanArray getSelectedIds() {
            return selectedItems;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {

            // Check if an existing view is being reused, otherwise inflate the view
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_push_notification_list, parent, false);
            }

            final PushNotificationItemView itemView = new PushNotificationItemView(view);

            presenter.buildItem(itemView, position);

            if (selectedItems.get(position)) {
                view.setSelected(true);
                view.setPressed(true);
                view.setBackgroundColor(getResources().getColor(R.color.choice_mode_multiple_pressed_color));
            }
            else
            {
                view.setSelected(false);
                view.setPressed(false);
                view.setBackgroundColor(getResources().getColor(R.color.white));
            }

            return view;
        }

        @Override
        public int getCount() {
            return super.getCount();
        };
    }
}
