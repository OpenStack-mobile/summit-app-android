package org.openstack.android.summit.modules.general_schedule.user_interface;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.user_interface.ScheduleFragment;
import org.openstack.android.summit.modules.events.user_interface.IEventsView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GeneralScheduleFragment
        extends ScheduleFragment<IGeneralSchedulePresenter>
        implements IGeneralScheduleView , IEventsView {

    private Menu menu;
    private boolean showActiveFilterIndicator;

    public GeneralScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        this.menu.clear();
        inflater.inflate(R.menu.main, this.menu);
        setFilterIcon();
    }

    private void setFilterIcon(){
        if(menu == null) return;
        MenuItem filterItem = menu.findItem(R.id.action_filter);
        if(filterItem == null) return;
        Drawable newIcon    = filterItem.getIcon();
        int color;
        if (showActiveFilterIndicator) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                color = view.getResources().getColor(R.color.openStackYellow, null);
            }
            else {
                color = view.getResources().getColor(R.color.openStackYellow);
            }
        }
        else {
            color = Color.WHITE;
        }

        newIcon.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        filterItem.setIcon(newIcon);
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

    @Override
    public void onResume() {
        setTitle(getResources().getString(R.string.events));
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "GeneralScheduleFragment.onCreateView");
        view                                = inflater.inflate(R.layout.fragment_general_schedule, container, false);
        LinearLayout activeFiltersIndicator = (LinearLayout)view.findViewById(R.id.active_filters_indicator);

        activeFiltersIndicator.setOnClickListener(v -> presenter.clearFilters());

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void reloadSchedule() {
        LinearLayout eventListContainer = (LinearLayout)view.findViewById(R.id.general_schedule_list_container);
        eventListContainer.setVisibility(View.VISIBLE);
        super.reloadSchedule();
    }

    @Override
    public void toggleEventList(boolean show) {
        LinearLayout eventListContainer = (LinearLayout)view.findViewById(R.id.general_schedule_list_container);
        eventListContainer.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setShowActiveFilterIndicator(boolean showActiveFilterIndicator) {
        LinearLayout activeFiltersIndicator = (LinearLayout)view.findViewById(R.id.active_filters_indicator);
        if(showActiveFilterIndicator){
            activeFiltersIndicator.setVisibility(View.VISIBLE);
            activeFiltersIndicator.setAlpha(0.0f);
            // Start the animation
            activeFiltersIndicator.animate()
                    .translationX(0)
                    .setDuration(500)
                    .alpha(1.0f);
        }
        else{
            activeFiltersIndicator.animate()
                    .translationXBy(view.getWidth())
                    .setDuration(500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            activeFiltersIndicator.setVisibility(View.GONE);
                        }
                    });
        }

        this.showActiveFilterIndicator = showActiveFilterIndicator;
        setFilterIcon();
    }

}
