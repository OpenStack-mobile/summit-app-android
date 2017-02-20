package org.openstack.android.summit.common.user_interface.schedule_list;

import android.graphics.Color;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.user_interface.IScheduleItemView;
import org.openstack.android.summit.common.user_interface.IScheduleListPresenter;
import org.openstack.android.summit.common.user_interface.ISchedulePresenter;
import org.openstack.android.summit.common.user_interface.recycler_view.RecyclerViewArrayAdapter;

import java.lang.reflect.Field;

/**
 * Created by smarcet on 2/16/17.
 */

public class ScheduleListAdapter
        extends
        RecyclerViewArrayAdapter<ScheduleItemDTO, ScheduleListAdapter.ScheduleItemViewHolder>
{

    private IScheduleListPresenter presenter;

    public ScheduleListAdapter(IScheduleListPresenter presenter){
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
                (item, position) -> presenter.toggleScheduleStatus(item, position),
                (item, position) -> presenter.toggleScheduleStatus(item, position),
                (item, position) -> presenter.toggleFavoriteStatus(item, position),
                (item, position) -> presenter.toggleFavoriteStatus(item, position)
        );
    }

    @Override
    public void onBindViewHolder(ScheduleListAdapter.ScheduleItemViewHolder holder, int position) {
        presenter.buildItem(holder, position);
    }

    public static class ScheduleItemViewHolder
            extends RecyclerView.ViewHolder
            implements IScheduleItemView,  View.OnClickListener  {

        @Override
        public Boolean getFavorite() {
            return favorite;
        }

        @Override
        public void setFavorite(Boolean favorite) {
            this.favorite = favorite;
            favoriteEvent.setVisibility(this.favorite  && !this.scheduled ? View.VISIBLE : View.GONE);
        }

        public interface OnSummitEventSelected{
            void action(int position);
        }

        public interface OnSummitEventNotGoing{
            void action(IScheduleItemView item, int position);
        }

        public interface OnSummitEventGoing{
            void action(IScheduleItemView item, int position);
        }

        public interface OnSummitEventFavorite{
            void action(IScheduleItemView item, int position);
        }

        public interface OnSummitEventRemoveFavorite{
            void action(IScheduleItemView item, int position);
        }

        private TextView name;
        private TextView time;
        private TextView sponsors;
        private TextView type;
        private TextView track;
        private LinearLayout locationContainer;
        private TextView location;
        private View colorView;
        private boolean scheduled;
        private boolean favorite;
        private TextView buttonViewOptions;
        private ImageView favoriteEvent;
        private ImageView goingEvent;
        // callback
        private OnSummitEventSelected clickEventCallback;
        private OnSummitEventNotGoing eventNotGoingCallback;
        private OnSummitEventGoing eventGoingCallback;
        private OnSummitEventFavorite eventFavoriteCallback;
        private OnSummitEventRemoveFavorite eventRemoveFavoriteCallback;
        private boolean showFavoritesMenuOption;
        private boolean showGoingMenuOption;

        public ScheduleItemViewHolder
        (
            View itemView,
            OnSummitEventSelected clickEventCallback,
            OnSummitEventNotGoing eventNotGoingCallback,
            OnSummitEventGoing eventGoingCallback,
            OnSummitEventFavorite eventFavoriteCallback,
            OnSummitEventRemoveFavorite eventRemoveFavoriteCallback
        )
        {

            super(itemView);
            name                             = (TextView) itemView.findViewById(R.id.item_schedule_textview_name);
            time                             = (TextView) itemView.findViewById(R.id.item_schedule_textview_time);
            sponsors                         = (TextView) itemView.findViewById(R.id.item_schedule_textview_sponsors);
            type                             = (TextView) itemView.findViewById(R.id.item_schedule_textview_event_type);
            track                            = (TextView) itemView.findViewById(R.id.item_schedule_textview_track);
            locationContainer                = (LinearLayout)itemView.findViewById(R.id.item_schedule_place_container);
            location                         = (TextView) itemView.findViewById(R.id.item_schedule_textview_location);;
            colorView                        = itemView.findViewById(R.id.item_schedule_view_color);
            buttonViewOptions                = (TextView) itemView.findViewById(R.id.textViewOptions);
            favoriteEvent                    = (ImageView) itemView.findViewById(R.id.favorite_event);
            goingEvent                       = (ImageView) itemView.findViewById(R.id.going_event);
            this.clickEventCallback          = clickEventCallback;
            this.eventNotGoingCallback       = eventNotGoingCallback;
            this.eventGoingCallback          = eventGoingCallback;
            this.eventFavoriteCallback       = eventFavoriteCallback;
            this.eventRemoveFavoriteCallback = eventRemoveFavoriteCallback;
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

            if (color == null || color.length() == 0) {
                this.colorView.setVisibility(View.INVISIBLE);
                this.track.setTextColor(itemView.getResources().getColor(R.color.openStackGray));
            }
            else {
                this.colorView.setVisibility(View.VISIBLE);
                this.colorView.setBackgroundColor(Color.parseColor(color));
                this.track.setTextColor(Color.parseColor(color));
            }
        }

        @Override
        public void setLocation(String location) {
            this.location.setText(location);
            this.locationContainer.setVisibility(location == null || location.length() == 0 ? View.GONE : View.VISIBLE);
        }

        @Override
        public void showLocation(boolean show) {
            if(this.locationContainer != null)
                this.locationContainer.setVisibility(show ? View.VISIBLE: View.GONE);
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
           if(clickEventCallback != null) clickEventCallback.action(position);
        }

        @Override
        public void setContextualMenu(){
            if(buttonViewOptions == null) return;

            if(showFavoritesMenuOption || showGoingMenuOption) {
                buttonViewOptions.setVisibility(View.VISIBLE);
                buttonViewOptions.setOnClickListener(v -> {
                    PopupMenu popup = new PopupMenu(itemView.getContext(), buttonViewOptions);
                    popup.inflate(R.menu.scheduled_item_options);

                    Object menuHelper;
                    Class[] argTypes;
                    try {
                        Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
                        fMenuHelper.setAccessible(true);
                        menuHelper = fMenuHelper.get(popup);
                        argTypes = new Class[]{boolean.class};
                        menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
                    } catch (Exception e) {
                    }
                    if(showGoingMenuOption) {
                        popup.getMenu().findItem(R.id.schedule_item_menu_save_going_action).setVisible(!scheduled);
                        popup.getMenu().findItem(R.id.schedule_item_menu_remove_going_action).setVisible(scheduled);
                    }
                    else{
                        popup.getMenu().findItem(R.id.schedule_item_menu_save_going_action).setVisible(false);
                        popup.getMenu().findItem(R.id.schedule_item_menu_remove_going_action).setVisible(false);
                    }

                    if(showFavoritesMenuOption) {
                        popup.getMenu().findItem(R.id.schedule_item_menu_remove_favorite_action).setVisible(favorite);
                        popup.getMenu().findItem(R.id.schedule_item_menu_save_favorite_action).setVisible(!favorite);
                    }
                    else{
                        popup.getMenu().findItem(R.id.schedule_item_menu_remove_favorite_action).setVisible(false);
                        popup.getMenu().findItem(R.id.schedule_item_menu_save_favorite_action).setVisible(false);
                    }

                    //adding click listener
                    popup.setOnMenuItemClickListener(item -> {
                        switch (item.getItemId()) {
                            case R.id.schedule_item_menu_remove_going_action: {
                                if (this.eventNotGoingCallback != null)
                                    this.eventNotGoingCallback.action(this, this.getAdapterPosition());
                            }
                            break;
                            case R.id.schedule_item_menu_save_going_action: {
                                if (this.eventGoingCallback != null)
                                    this.eventGoingCallback.action(this, this.getAdapterPosition());
                            }
                            break;
                            case R.id.schedule_item_menu_save_favorite_action: {
                                if (this.eventFavoriteCallback != null)
                                    this.eventFavoriteCallback.action(this, this.getAdapterPosition());
                            }
                            break;
                            case R.id.schedule_item_menu_remove_favorite_action: {
                                if (this.eventRemoveFavoriteCallback != null)
                                    this.eventRemoveFavoriteCallback.action(this, this.getAdapterPosition());
                            }
                            break;
                            case R.id.schedule_item_menu_cancel_action: {
                                popup.dismiss();
                            }
                            break;
                        }
                        return false;
                    });
                    //displaying the popup
                    popup.show();
                });
            }
            else{
                // hide ... button
                buttonViewOptions.setVisibility(View.GONE);
            }
        }
    }

}