package org.openstack.android.summit.common.user_interface;

import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import org.openstack.android.summit.R;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public class PersonItemView implements IPersonItemView {
    private View view;

    public PersonItemView(View view) {
        this.view = view;
    }

    @Override
    public void setName(String name) {
        TextView nameTextView = view.findViewById(R.id.item_person_list_name);
        nameTextView.setText(name);
    }

    @Override
    public void setTitle(String title) {
        TextView titleTextView = view.findViewById(R.id.item_person_list_title);
        titleTextView.setText(title);
    }

    @Override
    public void setPictureUri(Uri pictureUri) {
        SimpleDraweeView draweeView = view.findViewById(R.id.item_person_list_pic);
        draweeView.setImageURI(pictureUri);
    }

    @Override
    public void setRoundWithOverlayColor(int color) {
        SimpleDraweeView draweeView = view.findViewById(R.id.item_person_list_pic);
        RoundingParams roundingParams = RoundingParams.asCircle();
        roundingParams.setOverlayColor(color);
        draweeView.getHierarchy().setRoundingParams(roundingParams);

    }

    public void setIsModerator(boolean isModerator) {
        TextView moderatorIndicatorTextView = view.findViewById(R.id.item_person_moderator_indicator);
        moderatorIndicatorTextView.setVisibility(isModerator ? View.VISIBLE : View.GONE);
    }
}
