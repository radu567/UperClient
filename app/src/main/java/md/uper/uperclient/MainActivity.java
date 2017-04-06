package md.uper.uperclient;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import md.uper.uperclient.fragments.*;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    SupportMapFragment sMapFragment;

    Command fcommand;
    ActiveCommand factiveCommand;
    CalculPrice fcalculPrice;
    ClientInfo fclientInfo;
    CourseHistory fcourseHistory;
    Help fhelp;
    Settings fsettings;
    TermsAndConditions ftermsAndConditions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //lucru cu harta
        sMapFragment = SupportMapFragment.newInstance();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fcommand = new Command();
        factiveCommand = new ActiveCommand();
        fcalculPrice = new CalculPrice();
        fclientInfo = new ClientInfo();
        fcourseHistory = new CourseHistory();
        fhelp = new Help();
        fsettings = new Settings();
        ftermsAndConditions = new TermsAndConditions();

        // lucru cu harta
        sMapFragment.getMapAsync(this);
        android.support.v4.app.FragmentManager sFM = getSupportFragmentManager();
        if (!sMapFragment.isAdded()) {
            sFM.beginTransaction().add(R.id.map, sMapFragment).commit();
        } else {
            sFM.beginTransaction().show(sMapFragment).commit();
        }


    }

    @Override
    public void onMapReady(final GoogleMap sMapFragment) {
        LatLng sydney = new LatLng(-33.867, 151.206);
       // LatLng my_position = new LatLng(sMapFragment.getMyLocation().getLatitude(), sMapFragment.getMyLocation().getLongitude());
       //public void setMapToolbarEnabled (boolean enabled);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        sMapFragment.getUiSettings().setMapToolbarEnabled(false);
        sMapFragment.setMyLocationEnabled(true);
        sMapFragment.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        sMapFragment.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney)
                .draggable(true));

        sMapFragment.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_taxi))
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(-33.889, 151.202))
                .title("taxi jora")
                .snippet("Liber 24/24"));












        // tratarea atingerilor "pe harta"
        sMapFragment.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getApplicationContext(),
                        "Ai apasat marker!", Toast.LENGTH_LONG).show();
                return false;
            }
        });

        sMapFragment.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(getApplicationContext(),
                        "Ai apasat infoWindow!", Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        android.support.v4.app.FragmentManager sFM = getSupportFragmentManager();
        int id = item.getItemId();

        if (sMapFragment.isAdded())
            sFM.beginTransaction().hide(sMapFragment).commit();

        FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_command) {
            // Handle the camera action
            //ftrans.replace(R.id.container, fcommand);

            if (!sMapFragment.isAdded()){
                sFM.beginTransaction().add(R.id.map, sMapFragment).commit();
            } else {
                sFM.beginTransaction().show(sMapFragment).commit();
            }

        } else if (id == R.id.nav_activecommand) {
            ftrans.replace(R.id.container, factiveCommand);

        } else if (id == R.id.nav_calculprice) {
            ftrans.replace(R.id.container, fcalculPrice);

        } else if (id == R.id.nav_clientinfo) {
            ftrans.replace(R.id.container, fclientInfo);

        } else if (id == R.id.nav_coursehistory) {
            ftrans.replace(R.id.container, fcourseHistory);

        } else if (id == R.id.nav_help) {
            ftrans.replace(R.id.container, fhelp);

        } else if (id == R.id.nav_termsandconditions) {
            ftrans.replace(R.id.container, ftermsAndConditions);

        } ftrans.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
