package org.openstack.android.summit.common.user_interface;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import org.openstack.android.summit.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Claudio Redi on 1/28/2016.
 */
public class FeedbackItemView implements IFeedbackItemView {

    private View view;

    @BindView(R.id.item_feedback_rate_1)
    ImageView rate1;

    @BindView(R.id.item_feedback_rate_2)
    ImageView rate2;

    @BindView(R.id.item_feedback_rate_3)
    ImageView rate3;

    @BindView(R.id.item_feedback_rate_4)
    ImageView rate4;

    @BindView(R.id.item_feedback_rate_5)
    ImageView rate5;

    @BindView(R.id.item_feedback_date)
    TextView dateTextView;

    @BindView(R.id.item_feedback_review)
    TextView reviewTextView;

    public FeedbackItemView(View view) {
        this.view = view;
        ButterKnife.bind(this, this.view);
    }

    @Override
    public void setDate(String date) {
        if(dateTextView == null) return;
        dateTextView.setText(date);
    }

    @Override
    public void setRate(int rate) {

        if (rate >= 1) {
            rate1.setImageResource(R.drawable.ic_star);
        }
        if (rate >= 2) {
            rate2.setImageResource(R.drawable.ic_star);
        }
        if (rate >= 3) {
            rate3.setImageResource(R.drawable.ic_star);
        }
        if (rate >= 4) {
            rate4.setImageResource(R.drawable.ic_star);
        }
        if (rate >= 5) {
            rate5.setImageResource(R.drawable.ic_star);
        }
    }

    @Override
    public void setReview(String review) {
        if(reviewTextView == null) return;
        reviewTextView.setText(review);
    }
}
