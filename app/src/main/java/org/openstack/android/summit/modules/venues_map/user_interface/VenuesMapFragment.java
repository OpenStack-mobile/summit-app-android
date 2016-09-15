package org.openstack.android.summit.modules.venues_map.user_interface;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.VenueListItemDTO;
import org.openstack.android.summit.common.user_interface.BaseFragment;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public class VenuesMapFragment extends BaseFragment<IVenuesMapPresenter> implements IVenuesMapView, OnMapReadyCallback {
    private List<VenueListItemDTO> venues;
    private MapView map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(map != null)
            map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
        if(map != null)
            map.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
        if(map != null){
            map.onDestroy();
            map = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
        if(map != null)
            map.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if(map != null)
            map.onLowMemory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_venues_map, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        map = (MapView) view.findViewById(R.id.venues_map);
        map.onCreate(savedInstanceState);
        map.getMapAsync(this);

        return view;
    }

    public void addMarkers(List<VenueListItemDTO> venues) {
        this.venues = venues;
    }

    private static final float zoomLevel = 18.0f;

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        LatLng latLng;
        Marker marker;
        final HashMap<String, Integer> markersDictionary = new HashMap<>();
        for (VenueListItemDTO venue : venues) {
            latLng = new LatLng(new Double(venue.getLat()), new Double(venue.getLng()));
            marker = googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(venue.getName())
            );
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map));
            markersDictionary.put(marker.getId(), venue.getId());

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    int venueId = markersDictionary.get(marker.getId());
                    presenter.showVenueDetail(venueId);
                    return true;
                }
            });

            builder.include(latLng);
        }
        LatLngBounds bounds       = builder.build();
        DisplayMetrics display    = getResources().getDisplayMetrics();
        int padding               = display.widthPixels / 20;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, display.widthPixels, display.heightPixels, padding);
        googleMap.moveCamera(cameraUpdate);
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(zoomLevel));
    }
}
