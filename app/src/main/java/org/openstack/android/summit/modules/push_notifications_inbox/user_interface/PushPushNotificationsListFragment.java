package org.openstack.android.summit.modules.push_notifications_inbox.user_interface;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.PushNotificationListItemDTO;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.recycler_view.DividerItemDecoration;
import org.openstack.android.summit.common.user_interface.recycler_view.EndlessRecyclerViewScrollListener;
import org.openstack.android.summit.common.user_interface.recycler_view.RecyclerViewArrayAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    LinearLayoutManager layoutManager;
    private SearchView searchView;
    private SearchView.OnQueryTextListener queryTextListener;
    private RecyclerView notificationsList;
    private TextView emptyMessage;
    private EndlessRecyclerViewScrollListener scrollListener;


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

    protected ActionMode startSupportActionMode(ActionMode.Callback callback){
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        return activity.startSupportActionMode(callback);
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
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view         = inflater.inflate(R.layout.fragment_notifications_list, container, false);
        this.view         = view;
        notificationsList = (RecyclerView)view.findViewById(R.id.list_notifications);

        layoutManager     = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        notificationsList.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
        notificationsList.addItemDecoration(itemDecoration);
        listAdapter       = new PushNotificationListAdapter();
        emptyMessage      = (TextView)view.findViewById(R.id.list_notifications_empty_message);

        notificationsList.setAdapter(listAdapter);
        notificationsList.setItemAnimator(new DefaultItemAnimator());

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                presenter.loadData();
            }
        };

        notificationsList.addOnScrollListener(scrollListener);
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
        scrollListener.resetState();
    }

    private void updateState() {
        if(emptyMessage == null || notificationsList == null || listAdapter == null) return;
        boolean show = listAdapter.isEmpty();
        emptyMessage.setVisibility(show ? View.VISIBLE : View.GONE);
        notificationsList.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private class PushNotificationListAdapter
            extends
            RecyclerViewArrayAdapter<PushNotificationListItemDTO,
                    PushNotificationListAdapter.PushNotificationViewHolder>
    {
        public PushNotificationListAdapter(){
            super();
            removedAllItemsCallback = new PushNotificationListAdapter.RemovedAllItemsCallback(){

                @Override
                public void removedAll() {
                    updateState();
                }
            };

            removedItemCallback = new PushNotificationListAdapter.RemovedItemCallback<PushNotificationListItemDTO>(){

                @Override
                public void removed(PushNotificationListItemDTO item) {
                    presenter.onRemovePushNotification(item);
                }
            };
        }

        @Override
        public PushNotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.item_push_notification_list,
                            parent,
                            false);
            return new PushNotificationListAdapter.PushNotificationViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(PushNotificationViewHolder holder, int position) {
            presenter.buildItem(holder, position);
            holder.itemView.setActivated(isItemChecked(position));
        }

        public class PushNotificationViewHolder
                extends RecyclerView.ViewHolder
                implements IPushNotificationItemView, View.OnClickListener,View.OnLongClickListener
        {

            TextView subject;
            TextView receivedDate;
            TextView body;

            public PushNotificationViewHolder(View itemView) {
                super(itemView);
                subject      = (TextView) itemView.findViewById(R.id.item_push_notification_subject);
                receivedDate = (TextView) itemView.findViewById(R.id.item_push_notification_received_date);
                body         = (TextView) itemView.findViewById(R.id.item_push_notification_body);

                // events handlers
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
                itemView.setLongClickable(true);
            }

            @Override
            public void setSubject(String subject) {
                this.subject.setText(subject);
            }

            @Override
            public void setBody(String body) {
                this.body.setText(body);
            }

            @Override
            public void setOpened(boolean isOpened) {
                if(isOpened){
                    this.subject.setTypeface(null, Typeface.NORMAL);
                    return;
                }
                this.subject.setTypeface(null, Typeface.BOLD);
            }

            @Override
            public void setReceivedDate(Date receivedDate) {
                DateTime nowBegin = new DateTime().withTime(0,0,0,0);
                DateTime nowEnd   = new DateTime().withTime(23,59,59,0);
                DateFormat df     = receivedDate.after(nowBegin.toDate()) && receivedDate.before(nowEnd.toDate()) ?
                        new SimpleDateFormat("hh:mm a") :
                        new SimpleDateFormat("E d");
                this.receivedDate.setText(df.format(receivedDate));
            }

            @Override
            public void onClick(View v) {
                if(isOnSelectionMode())
                {
                   finishSelectionMode();
                }
                presenter.showNotification(getAdapterPosition());
            }

            @Override
            public boolean onLongClick(View v) {
                if(!isOnSelectionMode()) {
                    setActionMode(startSupportActionMode(deleteCallback));
                }
                toggleSelection(getAdapterPosition());
                actionMode.setTitle(getSelectedCount() + " Selected");
                return true;
            }
        }

    }

}
