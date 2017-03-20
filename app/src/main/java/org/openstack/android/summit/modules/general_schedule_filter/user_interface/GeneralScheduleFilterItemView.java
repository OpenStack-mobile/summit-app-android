package org.openstack.android.summit.modules.general_schedule_filter.user_interface;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.openstack.android.summit.R;

/**
 * Created by Claudio Redi on 2/3/2016.
 */
public class GeneralScheduleFilterItemView implements IGeneralScheduleFilterItemView {
    private View view;
    private OnSelectedItem itemAction;

    public interface OnSelectedItem{
        void onAction( boolean isChecked);
    }

    public void setItemCallback(OnSelectedItem itemAction){
        this.itemAction = itemAction;
        Switch checkedImageView = (Switch) view.findViewById(R.id.item_filter_checked);
        checkedImageView.setOnCheckedChangeListener((buttonView, isChecked) -> itemAction.onAction(isChecked));
    }

    public GeneralScheduleFilterItemView(View view) {
        this.view = view;
    }

    @Override
    public void setText(String text) {
        TextView textView = (TextView) view.findViewById(R.id.item_filter_text);
        textView.setText(text);
    }

    @Override
    public void setIsSelected(boolean isSelected) {
        Switch checkedImageView = (Switch) view.findViewById(R.id.item_filter_checked);
        checkedImageView.setChecked(isSelected);
    }

    @Override
    public void setCircleColor(int color) {
        View circle = view.findViewById(R.id.item_filter_circle);
        circle.getBackground().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
    }

    @Override
    public void setShowCircle(boolean showCircle) {
        View circle = view.findViewById(R.id.item_filter_circle);
        circle.setVisibility(showCircle ? View.VISIBLE : View.GONE);
    }
}
