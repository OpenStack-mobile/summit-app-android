package org.openstack.android.summit.common.user_interface;

import android.view.View;
import android.widget.TextView;

import org.openstack.android.summit.R;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public class SimpleListItemView implements ISimpleListItemView {
    private View view;

    public SimpleListItemView(View view) {
        this.view = view;
    }

    @Override
    public String getName() {
        TextView nameTextView = (TextView) view.findViewById(R.id.item_simple_list_name);
        return nameTextView.getText().toString();
    }

    @Override
    public void setName(String name) {
        TextView nameTextView = (TextView) view.findViewById(R.id.item_simple_list_name);
        nameTextView.setText(name);
    }
}
