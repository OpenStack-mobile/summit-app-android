package org.openstack.android.summit.common.user_interface;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import org.openstack.android.summit.R;

/**
 * Created by Claudio Redi on 2/4/2016.
 */
public class FilterAwareView {
    private boolean showActiveFilterIndicator;

    public void setShowActiveFilterIndicator(boolean showActiveFilterIndicator) {
        this.showActiveFilterIndicator = showActiveFilterIndicator;
    }

    public void onCreateOptionsMenu(View view, Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);

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
}
