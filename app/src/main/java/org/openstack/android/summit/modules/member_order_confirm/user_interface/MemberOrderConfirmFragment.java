package org.openstack.android.summit.modules.member_order_confirm.user_interface;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.openstack.android.summit.R;
import org.openstack.android.summit.R2;
import org.openstack.android.summit.common.DTOs.NonConfirmedSummitAttendeeDTO;
import org.openstack.android.summit.common.entities.NonConfirmedSummitAttendee;
import org.openstack.android.summit.common.user_interface.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Claudio Redi on 3/27/2016.
 */
public class MemberOrderConfirmFragment
        extends BaseFragment<IMemberOrderConfirmPresenter>
        implements IMemberOrderConfirmView {
    private static final NonConfirmedSummitAttendee[] NO_ATTENDEES = {};
    private String orderNumber;
    private SpinnerAdapter adapter;
    boolean attendeeSelected;
    private int externalAttendeeSelectedIndex;
    private Unbinder unbinder;

    @BindView(R2.id.member_order_confirm_number_text)
    EditText orderNumberTxt;

    @BindView(R2.id.btn_add_order_action)
    Button addOrderBtn;

    @BindView(R2.id.btn_cancel_order_action)
    Button cancelOrderBtn;

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
        view = inflater.inflate(R.layout.fragment_member_order_confirm, container, false);
        unbinder = ButterKnife.bind(this, view);

        orderNumberTxt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                confirmOrder();
            }
            return false;
        });

        adapter = new SpinnerAdapter(this.getContext());
        final MaterialBetterSpinner attendeesSpiner = (MaterialBetterSpinner) view.findViewById(R.id.member_order_confirm_number_attendees_spinner);
        attendeesSpiner.setAdapter(adapter);


        attendeesSpiner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                attendeeSelected = true;
                externalAttendeeSelectedIndex = position;
            }
        });

        addOrderBtn.setOnClickListener(v -> {
            if (!attendeeSelected) {
                confirmOrder();
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            else if (attendeeSelected){
                confirmOrderAfterSelectingAttendee();
            }
        });

        cancelOrderBtn.setOnClickListener(v -> {
            presenter.cancelOrder();
        });

        return view;
    }

    private void confirmOrderAfterSelectingAttendee() {
        final MaterialBetterSpinner attendeesSpinner = (MaterialBetterSpinner) view.findViewById(R.id.member_order_confirm_number_attendees_spinner);
        presenter.selectAttendeeFromOrderList(externalAttendeeSelectedIndex);
    }

    private void confirmOrder() {
        if(orderNumberTxt == null) return;
        String orderNumber = orderNumberTxt.getText().toString();
        if (!orderNumber.isEmpty()) {
            presenter.confirmOrder(orderNumber);
        }
    }

    public void setAttendees(List<NonConfirmedSummitAttendeeDTO> nonConfirmedSummitAttendeeList) {
        adapter.clear();
        adapter.addAll(nonConfirmedSummitAttendeeList);
        showMultipleMatchesControls(nonConfirmedSummitAttendeeList.size() > 0);
    }

    private void showMultipleMatchesControls(boolean show) {
        final MaterialBetterSpinner attendeesSpinner = (MaterialBetterSpinner) view.findViewById(R.id.member_order_confirm_number_attendees_spinner);
        attendeesSpinner.setVisibility(show ? View.VISIBLE : View.GONE);

        final TextView multipleMatchesText = (TextView) view.findViewById(R.id.member_order_confirm_multiple_mathes_text);
        multipleMatchesText.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        if(unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }

    private class SpinnerAdapter extends ArrayAdapter<NonConfirmedSummitAttendeeDTO> {

        public SpinnerAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
            }

            TextView textView = (TextView)convertView.findViewById(android.R.id.text1);
            textView.setText(getItem(position).getName());

            // Return the completed view to render on screen
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount();
        };
    }}
