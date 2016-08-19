package org.openstack.android.summit.modules.member_order_confirm.user_interface;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.NonConfirmedSummitAttendeeDTO;
import org.openstack.android.summit.common.entities.NonConfirmedSummitAttendee;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Claudio Redi on 3/27/2016.
 */
public class MemberOrderConfirmFragment extends BaseFragment<IMemberOrderConfirmPresenter> implements IMemberOrderConfirmView {
    private static final NonConfirmedSummitAttendee[] NO_ATTENDEES = {};
    private String orderNumber;
    private SpinnerAdapter adapter;
    private Menu menu;
    boolean attendeeSelected;
    private int externalAttendeeSelectedIndex;

    public MemberOrderConfirmFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        setHasOptionsMenu(true);
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

        EditText searchText = (EditText)view.findViewById(R.id.member_order_confirm_number_text);
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    confirmOrder();
                }
                return false;
            }
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

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.order_confirm, menu);
        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send) {
            if (!attendeeSelected) {
                confirmOrder();
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            else if (attendeeSelected){
                confirmOrderAfterSelectingAttendee();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void confirmOrderAfterSelectingAttendee() {
        final MaterialBetterSpinner attendeesSpinner = (MaterialBetterSpinner) view.findViewById(R.id.member_order_confirm_number_attendees_spinner);
        presenter.selectAttendeeFromOrderList(externalAttendeeSelectedIndex);
    }

    private void confirmOrder() {
        TextView orderNumberText = (TextView)view.findViewById(R.id.member_order_confirm_number_text);
        String orderNumber = orderNumberText.getText().toString();
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
