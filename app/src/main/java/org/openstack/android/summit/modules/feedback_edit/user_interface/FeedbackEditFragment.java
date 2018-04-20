package org.openstack.android.summit.modules.feedback_edit.user_interface;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.user_interface.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Claudio Redi on 2/17/2016.
 */
public class FeedbackEditFragment
        extends BaseFragment<IFeedbackEditPresenter>
        implements IFeedbackEditView {

    private int rate;
    private Menu menu;
    private Unbinder unbinder;

    @BindView(R.id.feedback_rate_1)
    ImageView starRateImage1;
    @BindView(R.id.feedback_rate_2)
    ImageView starRateImage2;
    @BindView(R.id.feedback_rate_3)
    ImageView starRateImage3;
    @BindView(R.id.feedback_rate_4)
    ImageView starRateImage4;
    @BindView(R.id.feedback_rate_5)
    ImageView starRateImage5;
    @BindView(R.id.feedback_create_button)
    Button feedbackCreateButton;
    @BindView(R.id.feedback_review_text)
    EditText reviewText;
    @BindView(R.id.event_name)
    TextView eventNameText;

    @Override
    public void setRate(int rate) {
        this.rate = rate;

        if(this.rate > 0){
            setStarColor(rate, 1, R.id.feedback_rate_1);
            setStarColor(rate, 2, R.id.feedback_rate_2);
            setStarColor(rate, 3, R.id.feedback_rate_3);
            setStarColor(rate, 4, R.id.feedback_rate_4);
            setStarColor(rate, 5, R.id.feedback_rate_5);
        }
    }

    @Override
    public void setReview(String review) { this.reviewText.setText(review); }

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
        presenter.onResume();
        setTitle(getResources().getString(R.string.feedback));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_feedback_edit, container, false);

        unbinder = ButterKnife.bind(this, view);

        View.OnClickListener onClickListener = v -> {
            rate = getSelectedRate(v);
            setStarColor(rate, 1, R.id.feedback_rate_1);
            setStarColor(rate, 2, R.id.feedback_rate_2);
            setStarColor(rate, 3, R.id.feedback_rate_3);
            setStarColor(rate, 4, R.id.feedback_rate_4);
            setStarColor(rate, 5, R.id.feedback_rate_5);
        };

        starRateImage1.setOnClickListener(onClickListener);
        starRateImage2.setOnClickListener(onClickListener);
        starRateImage3.setOnClickListener(onClickListener);
        starRateImage4.setOnClickListener(onClickListener);
        starRateImage5.setOnClickListener(onClickListener);

        feedbackCreateButton.setOnClickListener(v ->
                presenter.saveFeedback()
        );

        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.feedback, menu);
        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send) {
            presenter.saveFeedback();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            starRateImage.setImageResource(R.drawable.ic_star);
            return;
        }
        starRateImage.setImageResource(R.drawable.ic_star_border);
    }

    @Override
    public int getRate() {
        return rate;
    }

    @Override
    public String getReview() {
        if(reviewText == null) return "";
        return reviewText.getText().toString().trim();
    }

    @Override
    public void setEventName(String eventName) {
        if(eventNameText == null) return;
        eventNameText.setText(eventName);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        if(unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }
}


