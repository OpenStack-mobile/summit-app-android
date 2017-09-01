package org.openstack.android.summit.common.user_interface.schedule_list;

import android.graphics.Color;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuBuilder.Callback;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.user_interface.IScheduleItemView;
import org.openstack.android.summit.common.user_interface.IScheduleListPresenter;
import org.openstack.android.summit.common.user_interface.recycler_view.RecyclerViewArrayAdapter;

/**
 * Created by smarcet on 2/16/17.
 */

public class ScheduleListAdapter
        extends
        RecyclerViewArrayAdapter<ScheduleItemDTO, ScheduleListAdapter.ScheduleItemViewHolder> {

    private IScheduleListPresenter presenter;

    public ScheduleListAdapter(IScheduleListPresenter presenter) {
        super();
        this.presenter = presenter;
    }

    @Override
    public ScheduleListAdapter.ScheduleItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_schedule,
                        parent,
                        false);

        return new ScheduleItemViewHolder
                (
                        itemView,
                        position -> presenter.showEventDetail(position),
                        (item, position) -> presenter.rateEvent(item, position),
                        (item, position) -> presenter.toggleScheduleStatus(item, position),
                        (item, position) -> presenter.toggleScheduleStatus(item, position),
                        (item, position) -> presenter.toggleFavoriteStatus(item, position),
                        (item, position) -> presenter.toggleFavoriteStatus(item, position),
                        (item, position) -> presenter.toggleRSVPStatus(item, position),
                        (item, position) -> presenter.shareEvent(item, position)
                );
    }

    @Override
    public void onBindViewHolder(ScheduleListAdapter.ScheduleItemViewHolder holder, int position) {
        presenter.buildItem(holder, position);
    }

    public static class ScheduleItemViewHolder
            extends RecyclerView.ViewHolder
            implements IScheduleItemView, View.OnClickListener {

        @Override
        public Boolean getFavorite() {
            return favorite;
        }

        @Override
        public void setFavorite(Boolean favorite) {
            this.favorite = favorite;
            favoriteEvent.setVisibility(this.favorite && !this.scheduled ? View.VISIBLE : View.GONE);
        }

        public interface OnSummitEventSelected {
            void action(int position);
        }

        public interface OnEventMenuAction {
            void action(IScheduleItemView item, int position);
        }

        private TextView name;
        private TextView time;
        private TextView sponsors;
        private TextView type;
        private TextView track;
        private LinearLayout locationContainer;
        private TextView location;
        private boolean scheduled;
        private boolean favorite;
        private TextView buttonViewOptions;
        private LinearLayout optionsContainer;
        private ImageView favoriteEvent;
        private ImageView goingEvent;
        // callback
        private OnSummitEventSelected clickEventCallback;
        private OnEventMenuAction eventNotGoingCallback;
        private OnEventMenuAction eventGoingCallback;
        private OnEventMenuAction eventFavoriteCallback;
        private OnEventMenuAction eventRemoveFavoriteCallback;
        private OnEventMenuAction eventRSVPCallback;
        private OnEventMenuAction eventShareCallback;
        private OnEventMenuAction eventRateCallback;
        private boolean toBeRecorded;
        private boolean showFavoritesMenuOption;
        private boolean showGoingMenuOption;
        private boolean showRSVPOption;
        private boolean showUnRSVPOption;
        private boolean externalRSVP;
        private boolean allowRate;
        private String rsvpLink;

        public ScheduleItemViewHolder
                (
                        View itemView,
                        OnSummitEventSelected clickEventCallback,
                        OnEventMenuAction eventRateCallback,
                        OnEventMenuAction eventNotGoingCallback,
                        OnEventMenuAction eventGoingCallback,
                        OnEventMenuAction eventFavoriteCallback,
                        OnEventMenuAction eventRemoveFavoriteCallback,
                        OnEventMenuAction eventRSVPCallback,
                        OnEventMenuAction eventShareCallback
                ) {

            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_schedule_textview_name);
            time = (TextView) itemView.findViewById(R.id.item_schedule_textview_time);
            sponsors = (TextView) itemView.findViewById(R.id.item_schedule_textview_sponsors);
            type = (TextView) itemView.findViewById(R.id.item_schedule_textview_event_type);
            track = (TextView) itemView.findViewById(R.id.item_schedule_textview_track);
            locationContainer = (LinearLayout) itemView.findViewById(R.id.item_schedule_place_container);
            location = (TextView) itemView.findViewById(R.id.item_schedule_textview_location);

            buttonViewOptions                = (TextView) itemView.findViewById(R.id.textViewOptions);
            favoriteEvent                    = (ImageView) itemView.findViewById(R.id.favorite_event);
            goingEvent                       = (ImageView) itemView.findViewById(R.id.going_event);
            optionsContainer                 = (LinearLayout) itemView.findViewById(R.id.options_container);
            this.eventRateCallback           = eventRateCallback;
            this.clickEventCallback          = clickEventCallback;
            this.eventNotGoingCallback       = eventNotGoingCallback;
            this.eventGoingCallback          = eventGoingCallback;
            this.eventFavoriteCallback       = eventFavoriteCallback;
            this.eventRemoveFavoriteCallback = eventRemoveFavoriteCallback;
            this.eventRSVPCallback           = eventRSVPCallback;
            this.eventShareCallback          = eventShareCallback;
            // events handlers
            itemView.setOnClickListener(this);
        }

        @Override
        public void setName(String name) {
            this.name.setText(name);
        }

        @Override
        public void setTime(String time) {
            this.time.setText(time);
        }

        @Override
        public void setToBeRecorded(boolean toBeRecorded) { this.toBeRecorded = toBeRecorded; }

        @Override
        public void setSponsors(String sponsors) {
            this.sponsors.setText(sponsors);
            this.sponsors.setVisibility(sponsors == null || sponsors.length() == 0 ? View.GONE : View.VISIBLE);
        }

        @Override
        public void setEventType(String type) {
            this.type.setText(type);
        }

        @Override
        public void setTrack(String track) {
            this.track.setText(track);
            this.track.setVisibility(track == null || track.length() == 0 ? View.INVISIBLE : View.VISIBLE);
        }

        @Override
        public void setColor(String color) {

            int colorValue  = (color == null || color.length() == 0) ?
                    itemView.getResources().getColor(R.color.openStackGray) :
                    Color.parseColor(color);

            this.track.setTextColor(colorValue);
        }

        @Override
        public void setLocation(String location) {
            this.location.setText(location);
            this.locationContainer.setVisibility(location == null || location.length() == 0 ? View.GONE : View.VISIBLE);
        }

        @Override
        public void showLocation(boolean show) {
            if (this.locationContainer != null)
                this.locationContainer.setVisibility(show ? View.VISIBLE : View.GONE);
        }

        @Override
        public void shouldShowFavoritesOption(boolean show) {
            this.showFavoritesMenuOption = show;
        }

        @Override
        public void shouldShowGoingToOption(boolean show) {
            this.showGoingMenuOption = show;
        }

        @Override
        public void shouldShowRSVPToOption(boolean show) {
            this.showRSVPOption = show;
        }

        @Override
        public void shouldShowUnRSVPToOption(boolean show) {
            this.showUnRSVPOption = show;
        }

        @Override
        @SuppressWarnings({"all"})
        public void setContextualMenu() {
          if (buttonViewOptions == null) return;

                    optionsContainer.setVisibility(View.VISIBLE);
                    optionsContainer.setOnClickListener(v -> {

                        MenuBuilder menuBuilder     = new MenuBuilder(buttonViewOptions.getContext());
                        MenuInflater inflater       = new MenuInflater(buttonViewOptions.getContext());
                        inflater.inflate(R.menu.scheduled_item_options, menuBuilder);
                        MenuPopupHelper optionsMenu = new MenuPopupHelper(buttonViewOptions.getContext(), menuBuilder, buttonViewOptions);
                        optionsMenu.setForceShowIcon(true);

                        if(showGoingMenuOption) {
                            menuBuilder.findItem(R.id.schedule_item_menu_save_going_action).setVisible(!scheduled);
                            menuBuilder.findItem(R.id.schedule_item_menu_remove_going_action).setVisible(scheduled);
                        }
                        else{
                            menuBuilder.findItem(R.id.schedule_item_menu_save_going_action).setVisible(false);
                            menuBuilder.findItem(R.id.schedule_item_menu_remove_going_action).setVisible(false);
                        }

                        if(showFavoritesMenuOption) {
                            menuBuilder.findItem(R.id.schedule_item_menu_remove_favorite_action).setVisible(favorite);
                            menuBuilder.findItem(R.id.schedule_item_menu_save_favorite_action).setVisible(!favorite && toBeRecorded);
                        }
                        else{
                            menuBuilder.findItem(R.id.schedule_item_menu_remove_favorite_action).setVisible(false);
                            menuBuilder.findItem(R.id.schedule_item_menu_save_favorite_action).setVisible(false);
                        }

                        if(showUnRSVPOption)
                            menuBuilder.findItem(R.id.schedule_item_menu_unrsvp_action).setVisible(scheduled);
                        else
                            menuBuilder.findItem(R.id.schedule_item_menu_unrsvp_action).setVisible(false);

                        if(showRSVPOption){
                            menuBuilder.findItem(R.id.schedule_item_menu_save_rsvp_action).setVisible(!scheduled);
                            menuBuilder.findItem(R.id.schedule_item_menu_save_going_action).setVisible(false);
                            menuBuilder.findItem(R.id.schedule_item_menu_remove_going_action).setVisible(false);
                        }
                        else{
                            menuBuilder.findItem(R.id.schedule_item_menu_save_rsvp_action).setVisible(false);
                        }

                        menuBuilder.findItem(R.id.schedule_item_menu_rate_action).setVisible(allowRate);

                        menuBuilder.setCallback(new Callback() {
                            @Override
                            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                                switch (item.getItemId()) {

                                    case R.id.schedule_item_menu_remove_going_action: {
                                        if (eventNotGoingCallback != null) {
                                            eventNotGoingCallback.action(ScheduleItemViewHolder.this, getAdapterPosition());
                                            return true;
                                        }
                                    }
                                    break;
                                    case R.id.schedule_item_menu_save_going_action: {
                                        if (eventGoingCallback != null) {
                                            eventGoingCallback.action(ScheduleItemViewHolder.this, getAdapterPosition());
                                            return true;
                                        }
                                    }
                                    case R.id.schedule_item_menu_unrsvp_action:
                                    case R.id.schedule_item_menu_save_rsvp_action: {
                                        if (eventRSVPCallback != null) {
                                            eventRSVPCallback.action(ScheduleItemViewHolder.this, getAdapterPosition());
                                            return true;
                                        }
                                    }
                                    break;
                                    case R.id.schedule_item_menu_save_favorite_action: {
                                        if (eventFavoriteCallback != null) {
                                            eventFavoriteCallback.action(ScheduleItemViewHolder.this, getAdapterPosition());
                                            return true;
                                        }
                                    }
                                    break;
                                    case R.id.schedule_item_menu_remove_favorite_action: {
                                        if (eventRemoveFavoriteCallback != null) {
                                            eventRemoveFavoriteCallback.action(ScheduleItemViewHolder.this, getAdapterPosition());
                                            return true;
                                        }
                                    }
                                    break;
                                    case R.id.schedule_item_menu_rate_action: {
                                        if (eventRateCallback != null) {
                                            eventRateCallback.action(ScheduleItemViewHolder.this, getAdapterPosition());
                                            return true;
                                        }
                                    }
                                    break;
                                    case R.id.schedule_item_menu_share_action:{
                                        if (eventShareCallback != null) {
                                            eventShareCallback.action(ScheduleItemViewHolder.this, getAdapterPosition());
                                            return true;
                                        }
                                    }
                                    case R.id.schedule_item_menu_cancel_action: {
                                        optionsMenu.dismiss();
                                        return true;
                                    }
                                }
                                return false;
                            }

                            @Override
                            public void onMenuModeChange(MenuBuilder menu) {}
                        });

                        //displaying the popup
                        optionsMenu.show();
                    });
        }

        @Override
        public void shouldShowAllowRate(boolean allowRate) {
            this.allowRate = allowRate;
        }

        @Override
        public boolean isExternalRSVP() {
            return this.externalRSVP;
        }

        @Override
        public String getRSVPLink() {
            return this.rsvpLink;
        }

        @Override
        public void setExternalRSVP(boolean externalRSVP) {
            this.externalRSVP = externalRSVP;
        }

        @Override
        public void setRSVPLink(String link) {
            this.rsvpLink = link;
        }

        @Override
        public Boolean getScheduled() {
            return scheduled;
        }

        @Override
        public void setScheduled(Boolean scheduled) {
            this.scheduled = scheduled;
            goingEvent.setVisibility(this.scheduled ? View.VISIBLE : View.GONE);
            favoriteEvent.setVisibility(View.GONE);
            this.setFavorite(this.favorite);
        }

        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            if(position == RecyclerView.NO_POSITION) return;
            if (clickEventCallback != null) clickEventCallback.action(position);
        }

    }

}