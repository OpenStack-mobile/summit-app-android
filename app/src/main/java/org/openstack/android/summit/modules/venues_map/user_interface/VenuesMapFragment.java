package org.openstack.android.summit.modules.venues_map.user_interface;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.VenueListItemDTO;
import org.openstack.android.summit.common.entities.Venue;
import org.openstack.android.summit.common.user_interface.BaseFragment;

import java.util.List;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public class VenuesMapFragment extends BaseFragment<IVenuesMapPresenter> implements IVenuesMapView, OnMapReadyCallback {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_venues_map, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.venues_map);
        mapFragment.getMapAsync(this);

        return view;
    }

    public void addMarkers(List<VenueListItemDTO> venues) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
    }
}
