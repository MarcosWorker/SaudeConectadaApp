package br.com.alphadev.saudeconectadaapp.flow;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.alphadev.saudeconectadaapp.R;

public class RedeActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Intent intent;
    Bundle bundle;
    LatLng local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rede);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        intent = getIntent();
        bundle = intent.getExtras();



        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        local = new LatLng(bundle.getDouble("latitude"), bundle.getDouble("longitude"));
        mMap.addMarker(new MarkerOptions().position(local).title("Unidade de atendimento"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(local));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15),2000,null);

    }
}
