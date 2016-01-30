package org.openstack.android.summit.modules.member_profile_detail.user_interface;

import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.modules.member_profile.user_interface.IMemberProfilePresenter;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/26/2016.
 */
public class MemberProfileDetailFragment extends BaseFragment implements IMemberProfileDetailView {

    @Inject
    IMemberProfileDetailPresenter presenter;

    public MemberProfileDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        presenter.setView(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_profile_detail, container, false);
        this.view = view;
        presenter.onCreate(savedInstanceState);
        return view;
    }

    @Override
    public void setName(String name) {
        TextView nameTextView = (TextView) view.findViewById(R.id.profile_detail_name);
        nameTextView.setText(name);
    }

    @Override
    public void setTitle(String title) {
        TextView titleTextView = (TextView) view.findViewById(R.id.profile_detail_title);
        titleTextView.setText(title);
    }

    @Override
    public void setPictureUri(Uri pictureUri) {
        SimpleDraweeView draweeView = (SimpleDraweeView) view.findViewById(R.id.profile_detail_pic);
        draweeView.setImageURI(pictureUri);
    }

    @Override
    public void setBio(String bio) {
        TextView bioTextView = (TextView) view.findViewById(R.id.profile_detail_bio);
        bioTextView.setText(bio != null && !bio.isEmpty() ? Html.fromHtml(bio) : "");
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
        setTextViewOrHide(R.id.profile_detail_twitter, twitter);
    }

    @Override
    public void setIrc(String irc) {
        setTextViewOrHide(R.id.profile_detail_irc, irc);
    }

    private void setTextViewOrHide(int id, String text) {
        TextView titleTextView = (TextView) view.findViewById(id);
        titleTextView.setText(text);
        ((LinearLayout)titleTextView.getParent()).setVisibility(text != null && !text.isEmpty() ? View.VISIBLE : View.GONE);
    }
}