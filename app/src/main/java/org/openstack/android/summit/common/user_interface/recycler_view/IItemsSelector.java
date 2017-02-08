package org.openstack.android.summit.common.user_interface.recycler_view;

import java.util.List;

/**
 * Created by smarcet on 2/10/17.
 */

public interface IItemsSelector {

    void removeSelection();

    int getSelectedCount();

    boolean isItemChecked(int position);

    void toggleSelection(int position);

    List<Integer> getSelectedPositions();
}
