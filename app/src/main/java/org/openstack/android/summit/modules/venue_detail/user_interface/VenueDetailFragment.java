package org.openstack.android.summit.modules.venue_detail.user_interface;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.linearlistview.LinearListView;
import com.viewpagerindicator.CirclePageIndicator;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.VenueListItemDTO;
import org.openstack.android.summit.common.DTOs.VenueRoomDTO;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.SimpleListItemView;
import org.openstack.android.summit.common.user_interface.SlidePagerAdapter;
import org.openstack.android.summit.common.user_interface.zoomable.ZoomableDraweeView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public class VenueDetailFragment extends BaseFragment<IVenueDetailPresenter> implements IVenueDetailView, OnMapReadyCallback {
    private VenueRoomsAdapter venueRoomsAdapter;
    private MapView map;
    private VenueListItemDTO venue;
    private SlidePagerAdapter pagerAdapter;

    public VenueDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        setTitle(getResources().getString(R.string.venue));
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
        map.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
        map.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_venue_detail, container, false);
        this.view = view;
        LinearListView venueList = (LinearListView)view.findViewById(R.id.venue_rooms_list);
        venueRoomsAdapter = new VenueRoomsAdapter(getContext());
        venueList.setAdapter(venueRoomsAdapter);

        map = (MapView)view.findViewById(R.id.venue_map);
        map.onCreate(savedInstanceState);
        map.getMapAsync(this);

        pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager());

        LinearLayout locationLayout = (LinearLayout)view.findViewById(R.id.venue_location_container);
        locationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.showToMapIfApplies();
            }
        });

        ViewPager pager = (ViewPager) view.findViewById(R.id.venue_gallery_pager);
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        CirclePageIndicator pageIndicator = (CirclePageIndicator) view.findViewById(R.id.venue_gallery_indicator);
        pageIndicator.setViewPager(pager);

        super.onCreateView(inflater, container, savedInstanceState);

        return view;
    }

    public void toggleMap(boolean visible) {
        map.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void toggleGallery(boolean visible) {
        LinearLayout gallery = (LinearLayout) view.findViewById(R.id.venue_gallery);
        gallery.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void toggleMapNavigation(boolean visible) {
        ImageView navigationImage = (ImageView) view.findViewById(R.id.venue_map_navigation_image);
        navigationImage.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setVenueName(String name) {
        TextView venueName = (TextView)view.findViewById(R.id.venue_name_textview);
        venueName.setText(name);
    }

    @Override
    public void setVenueRooms(List<VenueRoomDTO> rooms) {
        venueRoomsAdapter.clear();
        venueRoomsAdapter.addAll(rooms);
    }

    @Override
    public void setLocation(String location) {
        TextView venueLocation = (TextView)view.findViewById(R.id.venue_location_textview);
        venueLocation.setText(location);
    }

    public void setMarker(VenueListItemDTO venue) {
        this.venue = venue;
    }

    @Override
    public void setMaps(List<String> maps) {
        pagerAdapter.addAll(maps);
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (venue != null) {
            LatLng latLng;
            Marker marker;
            latLng = new LatLng(new Double(venue.getLat()), new Double(venue.getLng()));
            marker = googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(venue.getName()));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map));
            marker.showInfoWindow();

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15), 10, null);
        }
    }

    private class VenueRoomsAdapter extends ArrayAdapter<VenueRoomDTO> {

        public VenueRoomsAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_simple_list, parent, false);
            }

            final SimpleListItemView venueRoomListItem = new SimpleListItemView(convertView);

            presenter.buildItem(venueRoomListItem, position);

            // Return the completed view to render on screen
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount();
        };
    }
}
