package org.openstack.android.summit.modules.member_order_confirm.user_interface;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.user_interface.BaseFragment;

/**
 * Created by Claudio Redi on 3/27/2016.
 */
public class MemberOrderConfirmFragment extends BaseFragment<IMemberOrderConfirmPresenter> implements IMemberOrderConfirmView {

    public MemberOrderConfirmFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(getResources().getString(R.string.profile));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        presenter.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_member_order_confirm, container, false);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
