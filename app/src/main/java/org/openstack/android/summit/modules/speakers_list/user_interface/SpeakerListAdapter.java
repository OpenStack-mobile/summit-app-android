package org.openstack.android.summit.modules.speakers_list.user_interface;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.user_interface.IPersonItemView;
import org.openstack.android.summit.common.user_interface.recycler_view.RecyclerViewArrayAdapter;
import org.openstack.android.summit.common.user_interface.fast_scroll_recycler_view.FastScrollRecyclerViewInterface;

import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by smarcet on 2/16/17.
 */

public class SpeakerListAdapter
        extends RecyclerViewArrayAdapter<PersonListItemDTO, SpeakerListAdapter.SpeakerItemViewHolder>
        implements FastScrollRecyclerViewInterface  {

    private static final double speakerListItemWeight = 0.90;

    private ISpeakerListPresenter presenter;
    private HashMap<String, Integer> mMapIndex;
    private Point screenSize = null;

    private Point getScreenSize(ViewGroup parent){
        if(screenSize == null) {
            WindowManager windowManager = (WindowManager) parent.getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display             = windowManager.getDefaultDisplay();
            screenSize                  = new Point();

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    display.getRealSize(screenSize);
                } else {
                    display.getSize(screenSize);
                }
            } catch (NoSuchMethodError err) {
                display.getSize(screenSize);
            }
        }
        return screenSize;
    }

    public SpeakerListAdapter(ISpeakerListPresenter presenter) {
        super();
        this.presenter = presenter;
    }

    @Override
    public SpeakerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_person_list,
                        parent,
                        false);

        LinearLayout container = (LinearLayout)itemView.findViewById(R.id.item_person_container);

        if(container != null){

            Point size = getScreenSize(parent);
            // set speaker list item width dynamically
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                    ((int)(size.x * speakerListItemWeight),
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            container.setLayoutParams(lp);
        }

        return new SpeakerItemViewHolder
                (
                        itemView,
                        position -> presenter.showSpeakerProfile(position)
                );
    }

    @Override
    public void onBindViewHolder(SpeakerListAdapter.SpeakerItemViewHolder holder, int position) {
        presenter.buildItem(holder, position);
    }

    @Override
    public HashMap<String,Integer> getMapIndex() {
        return this.mMapIndex;
    }

    public void setMapIndex(HashMap<String, Integer> mapIndex) {
        this.mMapIndex = mapIndex;

    }

    public static class SpeakerItemViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener, IPersonItemView {

        @BindView(R.id.item_person_list_name)
        TextView name;

        @BindView(R.id.item_person_list_title)
        TextView title;

        @BindView(R.id.item_person_list_pic)
        SimpleDraweeView picture;

        @BindView(R.id.item_person_moderator_indicator)
        TextView is_moderator;

        // callback
        private OnSpeakerSelected clickEventCallback;

        public interface OnSpeakerSelected {
            void action(int position);
        }

        public SpeakerItemViewHolder
        (
            View itemView,
            OnSpeakerSelected clickEventCallback
        )
        {

            super(itemView);
            this.clickEventCallback = clickEventCallback;
            // events handlers
            itemView.setOnClickListener(this);

            ButterKnife.bind(this, itemView);

        }

        @Override
        public void setName(String name) {
            this.name.setText(name);
        }

        @Override
        public void setTitle(String title) {
            this.title.setText(title);
        }

        @Override
        public void setPictureUri(Uri pictureUri) {
            this.picture.setImageURI(pictureUri);
        }

        public void setIsModerator(boolean isModerator) {
            this.is_moderator.setVisibility(isModerator ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            if (clickEventCallback != null) clickEventCallback.action(position);
        }

    }

}