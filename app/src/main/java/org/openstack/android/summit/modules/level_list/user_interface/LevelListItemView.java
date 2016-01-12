package org.openstack.android.summit.modules.level_list.user_interface;

import android.view.View;
import android.widget.TextView;

import org.openstack.android.summit.R;

/**
 * Created by Claudio Redi on 1/11/2016.
 */
public class LevelListItemView implements ILevelListItemView {
    private View view;

    public LevelListItemView(View view) {
        this.view = view;
    }

    @Override
    public String getName() {
        TextView nameTextView = (TextView) view.findViewById(R.id.item_levels_name);
        return nameTextView.getText().toString();
    }

    @Override
    public void setName(String name) {
        TextView nameTextView = (TextView) view.findViewById(R.id.item_levels_name);
        nameTextView.setText(name);
    }
}
