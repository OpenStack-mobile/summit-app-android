package org.openstack.android.summit.common.user_interface.recycler_view;

import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import org.openstack.android.summit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smarcet on 2/10/17.
 */

public abstract class RecyclerViewArrayAdapter<M extends Object, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH>
        implements IItemsSelector
{

    public interface RemovedItemCallback<M>{
         void removed(M item);
    }

    public interface RemovedAllItemsCallback{
       void removedAll();
    }

    protected List<M> items;

    protected ItemSelector selector;

    protected boolean onSelectionMode = false;

    protected DeleteMultiItemsActionModeCallback deleteCallback;

    protected ActionMode actionMode = null;

    protected RemovedItemCallback removedItemCallback ;

    protected RemovedAllItemsCallback removedAllItemsCallback;

    protected void setActionMode(ActionMode actionMode){
        this.actionMode = actionMode;
        this.startSelectionMode();
    }

    public RecyclerViewArrayAdapter(){
        selector       = new ItemSelector();
        deleteCallback = new DeleteMultiItemsActionModeCallback();
        this.items     = new ArrayList<>();
    }

    public RecyclerViewArrayAdapter(List<M> items){
        this();
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(M item, int position) {
        items.add(position, item);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public M getItem(int position) {
        return items.get(position);
    }

    public boolean isEmpty(){
        return getItemCount() == 0;
    }

    public void clear(){
        if(items == null) return;
        items.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<M> collection){
        int originalEnd = items.size();
        items.addAll(collection);
        notifyItemRangeInserted(originalEnd - 1, collection.size());
    }

    @Override
    public void removeSelection() {
        this.selector.removeSelection();
    }

    @Override
    public int getSelectedCount() {
        return this.selector.getSelectedCount();
    }

    @Override
    public boolean isItemChecked(int position) {
        return this.selector.isItemChecked(position);
    }

    @Override
    public void toggleSelection(int position) {
        this.selector.toggleSelection(position);
        notifyItemChanged(position);
    }

    @Override
    public List<Integer> getSelectedPositions(){
        return selector.getSelectedPositions();
    }

    private class DeleteMultiItemsActionModeCallback implements ActionMode.Callback
    {

        DeleteMultiItemsActionModeCallback(){

        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.push_notifications, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_item_push_notification_delete:
                    // Calls getSelectedIds method from ListViewAdapter Class
                    List<Integer> selected = selector.getSelectedPositions();
                    // Captures all selected ids with a loop
                    for (int i = (selected.size() - 1); i >= 0; i--) {
                        Integer position = selected.get(i);
                            if(removedItemCallback != null)
                                removedItemCallback.removed(getItem(position));

                            removeItem(position);
                    }
                    // Close CAB
                    mode.finish();
                    finishSelectionMode();

                    if(removedAllItemsCallback != null)
                        removedAllItemsCallback.removedAll();

                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            removeSelection();
            finishSelectionMode();
        }
    }

    public boolean isOnSelectionMode(){
        return this.onSelectionMode;
    }

    protected void setOnSelectionMode(boolean onSelectionMode){
        this.onSelectionMode = onSelectionMode;
    }

    public void finishSelectionMode(){
        setOnSelectionMode(false);
        if(actionMode != null) {
            actionMode.finish();
            actionMode = null;
        }
    }

    public void startSelectionMode(){
        setOnSelectionMode(true);
    }

}
