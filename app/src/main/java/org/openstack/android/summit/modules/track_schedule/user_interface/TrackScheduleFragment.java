package org.openstack.android.summit.modules.track_schedule.user_interface;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.user_interface.ScheduleFragment;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public class TrackScheduleFragment extends ScheduleFragment<ITrackSchedulePresenter> implements ITrackScheduleView {
    private boolean showActiveFilterIndicator;
    private Menu menu;

    public TrackScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void setTrack(String track) {
        TextView nameTextView = (TextView)view.findViewById(R.id.track_schedule_name);
        nameTextView.setText(track);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(getResources().getString(R.string.track));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_track_schedule, container, false);

        LinearLayout activeFiltersIndicator = (LinearLayout)view.findViewById(R.id.active_filters_indicator);
        activeFiltersIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.clearFilters();
            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);

        this.menu = menu;
        setupFilterItem();
    }

    private void setupFilterItem() {
        MenuItem filterItem = menu.findItem(R.id.action_filter);
        Drawable newIcon = filterItem.getIcon();
        int color;
        if (showActiveFilterIndicator) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                color = view.getResources().getColor(R.color.activeFilters, null);
            }
            else {
                color = view.getResources().getColor(R.color.activeFilters);
            }
        }
        else {
            color = Color.WHITE;
        }

        newIcon.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        filterItem.setIcon(newIcon);
    }

    @Override
    public void setShowActiveFilterIndicator(boolean showActiveFilterIndicator) {
        LinearLayout activeFiltersIndicator = (LinearLayout)view.findViewById(R.id.active_filters_indicator);
        activeFiltersIndicator.setVisibility(showActiveFilterIndicator ? View.VISIBLE : View.GONE);
        this.showActiveFilterIndicator = showActiveFilterIndicator;

        if (menu != null && menu.findItem(R.id.action_filter) != null) {
            setupFilterItem();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            presenter.showFilterView();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}