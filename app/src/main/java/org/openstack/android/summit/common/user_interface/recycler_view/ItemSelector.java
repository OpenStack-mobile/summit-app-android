package org.openstack.android.summit.common.user_interface.recycler_view;

import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smarcet on 2/10/17.
 */

public class ItemSelector implements IItemsSelector {

    public ItemSelector(){
        selectedItems = new SparseBooleanArray();
    }

    private SparseBooleanArray selectedItems;

    public void removeSelection() {
        selectedItems = new SparseBooleanArray();
    }

    @Override
    public int getSelectedCount() {
        return selectedItems.size();
    }

    @Override
    public boolean isItemChecked(int position) {
        return selectedItems.get(position, false);
    }

    @Override
    public void toggleSelection(int position) {
        selectView(position, !selectedItems.get(position));
    }

    private void selectView(int position, boolean value) {
        if (value)
            selectedItems.put(position, value);
        else
            selectedItems.delete(position);
    }

    @Override
    public List<Integer> getSelectedPositions(){
        List<Integer> res = new ArrayList<>();
        for(int i = 0; i < selectedItems.size(); i++) {
            int key = selectedItems.keyAt(i);
            // get the object by the key.
            if(selectedItems.get(key, false)){
                res.add(key);
            }
        }
        return res;
    }
}
