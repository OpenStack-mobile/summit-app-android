package org.openstack.android.summit.common.user_interface.fast_scroll_recycler_view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

/**
 * Created by flaviusmester on 23/02/15.
 */
public class FastScrollRecyclerView extends RecyclerView {
    private Context ctx;

    private boolean setupThings = false;
    public static int indWidth = 25;
    public static int indHeight= 18;
    public float scaledWidth;
    public float scaledHeight;
    public String[] sections;
    public float sx;
    public float sy;
    public String section;
    public boolean showLetter = false;
    private Handler listHandler;

    public FastScrollRecyclerView(Context context) {
        super(context);
        ctx = context;
    }

    public FastScrollRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx = context;
    }

    public FastScrollRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ctx = context;
    }

    @Override
    public void onDraw(Canvas c) {
        if(!setupThings)
            setupThings();
        super.onDraw(c);
    }

    private void setupThings() {
        //create az text data
        Set<String> sectionSet = ((FastScrollRecyclerViewInterface)getAdapter()).getMapIndex().keySet();
        ArrayList<String> listSection = new ArrayList<>(sectionSet);
        Collections.sort(listSection);
        sections = new String[listSection.size()];
        int i=0;
        for(String s:listSection) {
            sections[i++] = s;
        }

        scaledWidth = indWidth * ctx.getResources().getDisplayMetrics().density;
        scaledHeight= indHeight* ctx.getResources().getDisplayMetrics().density;
        sx = this.getWidth() - this.getPaddingRight() - (float)(1.2*scaledWidth);
        sy = (float)((this.getHeight() - (scaledHeight * sections.length) )/2.0);
        setupThings = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (x < sx - scaledWidth || y < sy || y > (sy + scaledHeight*sections.length))
                    return super.onTouchEvent(event);
                else {
                    // We touched the index bar
                    float yy = y - this.getPaddingTop() - getPaddingBottom() - sy;
                    int currentPosition = (int) Math.floor(yy / scaledHeight);
                    if(currentPosition<0)currentPosition=0;
                    if(currentPosition>=sections.length)currentPosition=sections.length-1;
                    section = sections[currentPosition];
                    showLetter = true;
                    int positionInData = 0;
                    if( ((FastScrollRecyclerViewInterface)getAdapter()).getMapIndex().containsKey(section.toUpperCase()) )
                        positionInData = ((FastScrollRecyclerViewInterface)getAdapter()).getMapIndex().get(section.toUpperCase());
                    ((LinearLayoutManager)this.getLayoutManager()).scrollToPositionWithOffset(positionInData, 0);
                    FastScrollRecyclerView.this.invalidate();
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {

                if (!showLetter && (x < sx  - scaledWidth || y < sy || y > (sy + scaledHeight*sections.length)))
                    return super.onTouchEvent(event);
                else {
                    float yy = y - sy;
                    int currentPosition = (int) Math.floor(yy / scaledHeight);
                    if(currentPosition<0)currentPosition=0;
                    if(currentPosition>=sections.length)currentPosition=sections.length-1;
                    section = sections[currentPosition];
                    showLetter = true;
                    int positionInData = 0;
                    if(((FastScrollRecyclerViewInterface)getAdapter()).getMapIndex().containsKey(section.toUpperCase()) )
                        positionInData = ((FastScrollRecyclerViewInterface)getAdapter()).getMapIndex().get(section.toUpperCase());
                    this.scrollToPosition(positionInData);
                    FastScrollRecyclerView.this.invalidate();

                }
                break;

            }
            case MotionEvent.ACTION_UP: {
                listHandler = new ListHandler();
                listHandler.sendEmptyMessageDelayed(0, 100);
                if (x < sx - scaledWidth || y < sy || y > (sy + scaledHeight*sections.length))
                    return super.onTouchEvent(event);
                else
                    return true;
//                break;
            }
        }
        return true;
    }

    private class ListHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            showLetter = false;
            FastScrollRecyclerView.this.invalidate();
        }


    }
}
