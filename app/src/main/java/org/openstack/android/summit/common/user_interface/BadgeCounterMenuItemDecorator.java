package org.openstack.android.summit.common.user_interface;

import android.support.v4.view.MenuItemCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * Created by sebastian on 8/20/2016.
 */
public class BadgeCounterMenuItemDecorator {

    private MenuItem item;
    private int counterElementId;

    public BadgeCounterMenuItemDecorator(MenuItem item, int counterElementId){
        this.item             = item;
        this.counterElementId = counterElementId;
    }

    public void updateCounter(String value){
        View actionView     = MenuItemCompat.getActionView(item);
        if(actionView == null) return;
        TextView txtCounter = (TextView) actionView.findViewById(counterElementId);
        if(txtCounter == null) return;
        showCounter();
        txtCounter.setText(value);
    }

    public void hideCounter(){
        View actionView     = MenuItemCompat.getActionView(item);
        if(actionView == null) return;
        TextView txtCounter = (TextView) actionView.findViewById(counterElementId);
        if(txtCounter == null) return;
        txtCounter.setVisibility(View.GONE);
    }

    public void showCounter(){
        View actionView     = MenuItemCompat.getActionView(item);
        if(actionView == null) return;
        TextView txtCounter = (TextView) actionView.findViewById(counterElementId);
        if(txtCounter == null) return;
        txtCounter.setVisibility(View.VISIBLE);
    }
}
