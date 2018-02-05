package org.openstack.android.summit.modules.general_schedule_filter.user_interface;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.TrackDTO;

import java.util.List;

/**
 * Created by smarcet on 2/2/18.
 */

public class GeneralScheduleFilterItemNavigationView
        extends GeneralScheduleFilterItemView
    implements IGeneralScheduleFilterItemNavigationView
{

    public GeneralScheduleFilterItemNavigationView(View view) {
        super(view);
    }

    @Override
    public void setIsSelected(boolean isSelected) {
    }

    public void setItemCallback(OnSelectedItem itemAction){
        this.itemAction = itemAction;
        ImageView moreImageView = (ImageView) view.findViewById(R.id.item_filter_more);
        moreImageView.setOnClickListener((view) -> itemAction.onAction(true));
    }

    @Override
    public void setCircleColor(int color) {
        View circleFull  = view.findViewById(R.id.item_filter_full_circle);
        View circleEmpty = view.findViewById(R.id.item_filter_empty_circle);
        circleFull.getBackground().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
        GradientDrawable background =  (GradientDrawable)circleEmpty.getBackground();
        background.setStroke(3, color);
    }

    @Override
    public void setShowCircle(boolean showCircle) {
        View circleFull = view.findViewById(R.id.item_filter_full_circle);
        View circleEmpty = view.findViewById(R.id.item_filter_empty_circle);
        circleFull.setVisibility(showCircle ? View.VISIBLE : View.GONE);
        circleEmpty.setVisibility(!showCircle ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setSelectedSubItemsText(List<String> selectedSubItems){
        TextView subItems = (TextView)view.findViewById(R.id.item_selected_sub_items);
        subItems.setVisibility(!selectedSubItems.isEmpty() ? View.VISIBLE : View.GONE);
        String subItemsText = "";
        for(String item: selectedSubItems){
            if(!subItemsText.isEmpty()) subItemsText +=", ";
            subItemsText += item;
        }
        subItems.setText(subItemsText);
    }
}
