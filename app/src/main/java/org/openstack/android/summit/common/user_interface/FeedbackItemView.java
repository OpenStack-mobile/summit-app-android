package org.openstack.android.summit.common.user_interface;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.openstack.android.summit.R;

/**
 * Created by Claudio Redi on 1/28/2016.
 */
public class FeedbackItemView implements IFeedbackItemView {
    private View view;

    public FeedbackItemView(View view) {
        this.view = view;
    }

    @Override
    public void setDate(String date) {
        TextView dateTextView = (TextView) view.findViewById(R.id.item_feedback_date);
        dateTextView.setText(date);
    }

    @Override
    public void setEventTitle(String eventTitle) {
        TextView eventTitleTextView = (TextView) view.findViewById(R.id.item_feedback_event_title);
        eventTitleTextView.setText(eventTitle);
    }

    @Override
    public void setRate(int rate) {
        ImageView rate1 = (ImageView)view.findViewById(R.id.item_feedback_rate_1);
        ImageView rate2 = (ImageView)view.findViewById(R.id.item_feedback_rate_2);
        ImageView rate3 = (ImageView)view.findViewById(R.id.item_feedback_rate_3);
        ImageView rate4 = (ImageView)view.findViewById(R.id.item_feedback_rate_4);
        ImageView rate5 = (ImageView)view.findViewById(R.id.item_feedback_rate_5);

        if (rate >= 1) {
            rate1.setImageResource(R.drawable.star_color);
        }
        if (rate >= 2) {
            rate2.setImageResource(R.drawable.star_color);
        }
        if (rate >= 3) {
            rate3.setImageResource(R.drawable.star_color);
        }
        if (rate >= 4) {
            rate4.setImageResource(R.drawable.star_color);
        }
        if (rate >= 5) {
            rate5.setImageResource(R.drawable.star_color);
        }
    }

    @Override
    public void setOwner(String owner) {
        TextView ownerTextView = (TextView) view.findViewById(R.id.item_feedback_owner);
        ownerTextView.setText(owner);
    }

    @Override
    public void setReview(String review) {
        TextView reviewTextView = (TextView) view.findViewById(R.id.item_feedback_review);
        reviewTextView.setText(review);
    }
}
