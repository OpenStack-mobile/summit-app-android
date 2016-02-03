package org.openstack.android.summit.modules.general_schedule_filter.user_interface;

import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.openstack.android.summit.R;

/**
 * Created by Claudio Redi on 2/3/2016.
 */
public class GeneralScheduleFilterItemView implements IGeneralScheduleFilterItemView {
    private View view;
    private String selectedColor;
    private String unselectedColor;

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
        ImageView checkedImageView = (ImageView) view.findViewById(R.id.item_filter_checked);
        checkedImageView.setVisibility(isSelected ? View.VISIBLE : View.GONE);
        TextView textView = (TextView) view.findViewById(R.id.item_filter_text);
        if (isSelected) {
            textView.setTypeface(null, Typeface.BOLD);
        }
        else {
            textView.setTypeface(null, Typeface.NORMAL);
        }
    }

    @Override
    public void setColor(int color) {
        TextView textView = (TextView) view.findViewById(R.id.item_filter_text);
        textView.setTextColor(color);
    }

    @Override
    public void setShowCircle(boolean showCircle) {
    }
}
