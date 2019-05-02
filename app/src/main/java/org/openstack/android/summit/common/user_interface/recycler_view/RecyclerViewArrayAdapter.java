package org.openstack.android.summit.common.user_interface.recycler_view;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smarcet on 2/16/17.
 */

abstract public class RecyclerViewArrayAdapter <M extends Object, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    protected List<M> items;

    public RecyclerViewArrayAdapter(){
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
        int count = items.size();
        items.clear();
        notifyItemRangeRemoved(0, count);
    }

    public void addAll(List<M> collection){
        int originalEnd = items.size();
        items.addAll(collection);
        notifyItemRangeInserted(originalEnd - 1, collection.size());
    }

}
