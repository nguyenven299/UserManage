package com.usermanage.view.ui.location;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.usermanage.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.usermanage.base.BaseFragment;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

public class LocationFragment extends BaseFragment implements OnMapReadyCallback, LocationListener {

    private MapView mMapView;
    private GoogleMap googleMap;
    private LocationManager mLocationManager;
    private Toolbar toolbar;
    private TextView textView;
    private View view;
    private Snackbar snackbar;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_location, container, false);
        mMapView = (MapView) root.findViewById(R.id.mapView);
        toolbar = root.findViewById(R.id.toolbar);
        toolbar = root.findViewById(R.id.toolbar);
        textView = root.findViewById(R.id.tv);
        view = root.findViewById(R.id.layoutMain);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mLocationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this::onMapReady);
        return root;
    }

    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;
        mMap.getCameraPosition();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        getCurrrentL();
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.getUiSettings().setZoomControlsEnabled(true);
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                moveMap(latLng);
            }
        });
    }

    public void getCurrrentL() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);

        } else {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1000, (LocationListener) this);
        }
    }

    private void moveMap(LatLng latLng) {
        LatLng sydney = latLng;
        String lLocation = new String();
        googleMap.clear();
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (null != listAddresses && listAddresses.size() > 0) {
                lLocation = listAddresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        googleMap.addMarker(new MarkerOptions().position(latLng).draggable(true).title(lLocation));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(16.0f).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onLocationChanged(@NonNull Location location) {
        LatLng requestedPosition = new LatLng(location.getLatitude(), location.getLongitude());
        float zoom = 16.0f;
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(requestedPosition, zoom));
        if (getContext() != null) {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                String lLocation = new String();
                if (null != listAddresses && listAddresses.size() > 0) {
                    lLocation = listAddresses.get(0).getAddressLine(0);
                    textView.setText("Vị trí hiện tại: " + lLocation);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return;
        }
    }

    @Override
    protected void onInternetStatusChange(boolean b) {
        snackbar = Snackbar.make(view, "Mất kết nối Internet", Snackbar.LENGTH_LONG);
        if (b) {
            snackbar.dismiss();
        } else {
            snackbar.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}