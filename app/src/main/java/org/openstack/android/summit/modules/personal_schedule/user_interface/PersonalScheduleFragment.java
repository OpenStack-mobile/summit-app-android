package org.openstack.android.summit.modules.personal_schedule.user_interface;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.ScheduleFragment;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public class PersonalScheduleFragment extends ScheduleFragment<IPersonalSchedulePresenter> implements IPersonalScheduleView {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_schedule, container, false);
        this.view = view;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private static final int MOVE_DURATION     = 150;
    private static final int FADE_OUT_DURATION = 300;

    HashMap<Long, Integer> mItemIdTopMap = new HashMap<Long, Integer>();

    @Override
    public void removeItem(final int position) {
        int firstVisiblePosition = scheduleList.getFirstVisiblePosition();
        final View itemView      = scheduleList.getChildAt(position - firstVisiblePosition);
        if(itemView == null) return;
        scheduleList.setEnabled(false);
        itemView.animate().setDuration(FADE_OUT_DURATION).
                alpha(0).
                withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        // Restore animated values
                        itemView.setAlpha(1);
                        animateRemoval(scheduleList, scheduleListAdapter, itemView);
                    }
                });
    }

    /**
     * This method animates all other views in the ListView container (not including ignoreView)
     * into their final positions. It is called after ignoreView has been removed from the
     * adapter, but before layout has been run. The approach here is to figure out where
     * everything is now, then allow layout to run, then figure out where everything is after
     * layout, and then to run animations between all of those start/end positions.
     */
    private void animateRemoval(final ListView listview, final ScheduleListAdapter adapter, View viewToRemove) {
        int firstVisiblePosition = listview.getFirstVisiblePosition();

        for (int i = 0; i < listview.getChildCount(); ++i) {
            View child = listview.getChildAt(i);
            if (child != viewToRemove) {
                int position = firstVisiblePosition + i;
                long itemId = adapter.getItemId(position);
                mItemIdTopMap.put(itemId, child.getTop());
            }
        }
        // Delete the item from the adapter
        int position = listview.getPositionForView(viewToRemove);
        adapter.enableAnimations(false);
        adapter.remove(adapter.getItem(position));
        presenter.removeItem(position);

        final ViewTreeObserver observer = listview.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                observer.removeOnPreDrawListener(this);
                boolean firstAnimation = true;
                int firstVisiblePosition = listview.getFirstVisiblePosition();
                for (int i = 0; i < listview.getChildCount(); ++i) {
                    final View child = listview.getChildAt(i);
                    int position     = firstVisiblePosition + i;
                    long itemId      = adapter.getItemId(position);
                    Integer startTop = mItemIdTopMap.get(itemId);
                    int top          = child.getTop();
                    if (startTop != null) {
                        if (startTop != top) {
                            int delta = startTop - top;
                            child.setTranslationY(delta);
                            child.animate().setDuration(MOVE_DURATION).translationY(0);
                            if (firstAnimation) {
                                child.animate().withEndAction(new Runnable() {
                                    public void run() {
                                        listview.setEnabled(true);
                                    }
                                });
                                firstAnimation = false;
                            }
                        }
                    } else {
                        // Animate new views along with the others. The catch is that they did not
                        // exist in the start state, so we must calculate their starting position
                        // based on neighboring views.
                        int childHeight = child.getHeight() + listview.getDividerHeight();
                        startTop = top + (i > 0 ? childHeight : -childHeight);
                        int delta = startTop - top;
                        child.setTranslationY(delta);
                        child.animate().setDuration(MOVE_DURATION).translationY(0);
                        if (firstAnimation) {
                            child.animate().withEndAction(new Runnable() {
                                public void run() {
                                    listview.setEnabled(true);
                                }
                            });
                            firstAnimation = false;
                        }
                    }
                }
                mItemIdTopMap.clear();
                adapter.enableAnimations(true);
                showEmptyMessage(adapter.isEmpty());
                return true;
            }
        });
    }

    @Override
    public void enableListView(boolean enable) {
        scheduleList.setEnabled(enable);
    }
}
