package org.openstack.android.summit.modules.feedback_edit.user_interface;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.linearlistview.LinearListView;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.user_interface.BaseFragment;

/**
 * Created by Claudio Redi on 2/17/2016.
 */
public class FeedbackEditFragment extends BaseFragment<IFeedbackEditPresenter> implements IFeedbackEditView {
    private int rate;
    public FeedbackEditFragment() {
        // Required empty public constructor
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
        setTitle(getResources().getString(R.string.feedback));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_feedback_edit, container, false);

        ImageView starRateImage1 = (ImageView)view.findViewById(R.id.feedback_rate_1);
        ImageView starRateImage2 = (ImageView)view.findViewById(R.id.feedback_rate_2);
        ImageView starRateImage3 = (ImageView)view.findViewById(R.id.feedback_rate_3);
        ImageView starRateImage4 = (ImageView)view.findViewById(R.id.feedback_rate_4);
        ImageView starRateImage5 = (ImageView)view.findViewById(R.id.feedback_rate_5);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rate = getSelectedRate(v);
                setStarColor(rate, 1, R.id.feedback_rate_1);
                setStarColor(rate, 2, R.id.feedback_rate_2);
                setStarColor(rate, 3, R.id.feedback_rate_3);
                setStarColor(rate, 4, R.id.feedback_rate_4);
                setStarColor(rate, 5, R.id.feedback_rate_5);
            }
        };

        starRateImage1.setOnClickListener(onClickListener);
        starRateImage2.setOnClickListener(onClickListener);
        starRateImage3.setOnClickListener(onClickListener);
        starRateImage4.setOnClickListener(onClickListener);
        starRateImage5.setOnClickListener(onClickListener);

        Button feedbackCreateButton = (Button)view.findViewById(R.id.feedback_create_button);
        feedbackCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.saveFeedback();
            }
        });

        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    private int getSelectedRate(View v) {
        int rate = 0;

        if (v.getId() == R.id.feedback_rate_1) { rate = 1; }
        else if (v.getId() == R.id.feedback_rate_2) { rate = 2; }
        else if (v.getId() == R.id.feedback_rate_3) { rate = 3; }
        else if (v.getId() == R.id.feedback_rate_4) { rate = 4; }
        else if (v.getId() == R.id.feedback_rate_5) { rate = 5; }

        return rate;
    }
    private void setStarColor(int selectedRate, int starRate, int controlId) {
        ImageView starRateImage = (ImageView)view.findViewById(controlId);
        if (starRate <= selectedRate) {
            starRateImage.setImageResource(R.drawable.star_color);
        }
        else {
            starRateImage.setImageResource(R.drawable.star_ligth_grey);
        }
    }

    public int getRate() {
        return rate;
    }

    public String getReview() {
        EditText reviewText = (EditText)view.findViewById(R.id.feedback_review_text);
        return reviewText.getText().toString();
    }
}


