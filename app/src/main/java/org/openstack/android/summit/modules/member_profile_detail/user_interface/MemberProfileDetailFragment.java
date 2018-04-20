package org.openstack.android.summit.modules.member_profile_detail.user_interface;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.HtmlTextView;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.tabs.FragmentLifecycle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Claudio Redi on 1/26/2016.
 */
public class MemberProfileDetailFragment
        extends BaseFragment<IMemberProfileDetailPresenter>
        implements IMemberProfileDetailView, FragmentLifecycle {

    protected Unbinder unbinder;

    @BindView(R.id.add_eventbrite_order_container)
    LinearLayout addEventbriteOrderContainer;

    @BindView(R.id.eventbrite_order_container)
    LinearLayout eventbriteOrderAddedContainer;

    @BindView(R.id.profile_detail_name)
    TextView nameTextView;

    @BindView(R.id.profile_detail_title)
    TextView titleTextView;

    @BindView(R.id.profile_detail_bio)
    HtmlTextView bioTextView;

    @BindView(R.id.profile_detail_pic)
    SimpleDraweeView draweeView;

    @BindView(R.id.member_info_container)
    LinearLayout memberInfoContainer;

    @BindView(R.id.missing_eventbrite_order_indicator)
    LinearLayout missingEventbriteOrderIndicator;

    public MemberProfileDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onViewStateRestored (savedInstanceState);
        presenter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_member_profile_detail, container, false);
        unbinder  = ButterKnife.bind(this, view);
        this.view = view;

        super.onCreateView(inflater, container, savedInstanceState);
        addEventbriteOrderContainer.setOnClickListener(v -> {
            presenter.onAddEventBriteOrderClicked();
        });
        return view;
    }

    @Override
    public AlertDialog createNotAttendeeAlertDialog(){
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.eventbrite_not_attendee_alert_dialog_title)
                .setMessage(R.string.eventbrite_not_attendee_alert_dialog_body)
                .setPositiveButton(R.string.eventbrite_not_attendee_alert_dialog_ok, (dialog, id) -> {
                    presenter.willAttendClicked();
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.eventbrite_not_attendee_alert_dialog_cancel, (dialog, id) -> {
                    presenter.willNotAttendClicked();
                    dialog.dismiss();
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void setName(String name) {
        if(nameTextView == null) return;
        nameTextView.setText(name);
    }

    @Override
    public void setTitle(String title) {
        if(titleTextView == null) return;
        titleTextView.setText(title);
    }

    @Override
    public void setPictureUri(Uri pictureUri) {
        if(draweeView == null) return;
        draweeView.setImageURI(pictureUri);
    }

    @Override
    public void setBio(String bio) {
        if(bioTextView == null) return;
        bioTextView.setText(bio != null && !bio.isEmpty() ? bio : "");
    }

    @Override
    public void setLocation(String location) {

        setTextViewOrHide(R.id.profile_detail_location, location);
    }

    @Override
    public void setMail(String mail) {
        setTextViewOrHide(R.id.profile_detail_mail, mail);
    }

    @Override
    public void setTwitter(String twitter) {
        if(twitter != null && !twitter.trim().isEmpty())
            memberInfoContainer.setVisibility(View.VISIBLE);

        setTextViewOrHide(R.id.profile_detail_twitter, twitter);
    }

    @Override
    public void setIrc(String irc) {
        if(irc != null && !irc.trim().isEmpty())
            memberInfoContainer.setVisibility(View.VISIBLE);
        setTextViewOrHide(R.id.profile_detail_irc, irc);
    }

    @Override
    public void showAddEventBriteOrderContainer(boolean show) {
        if(addEventbriteOrderContainer == null) return;
        addEventbriteOrderContainer.setVisibility(show ? view.VISIBLE : view.GONE);
    }

    @Override
    public void showEventBriteOrderAdded(boolean show) {
        if(eventbriteOrderAddedContainer == null) return;
        eventbriteOrderAddedContainer.setVisibility(show ? view.VISIBLE : view.GONE);
    }

    private void setTextViewOrHide(int id, String text) {
        TextView titleTextView = (TextView) view.findViewById(id);
        titleTextView.setText(text);
        ((LinearLayout)titleTextView.getParent()).setVisibility(text != null && !text.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        if(unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }

    @Override
    public void setShowMissingEventBriteOrderIndicator(boolean show) {
        if(missingEventbriteOrderIndicator == null) return;
        missingEventbriteOrderIndicator.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }
}