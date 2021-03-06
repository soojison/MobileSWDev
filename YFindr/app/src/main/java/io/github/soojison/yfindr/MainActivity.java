package io.github.soojison.yfindr;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ncapdevi.fragnav.FragNavController;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.soojison.yfindr.data.MyLatLng;
import io.github.soojison.yfindr.data.Pin;
import io.github.soojison.yfindr.fragment.MapFragment;
import io.github.soojison.yfindr.fragment.RecyclerFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MapFragment.OnLocationUpdatedListener {

    public static final String KEY_PIN = "pins";
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 202;

    // 2.5km = 2500m radius
    private static final int HUMAN_WALKABLE_DISTANCE = 2500;

    private FragNavController fragNavController;
    private final int TAB_FIRST = FragNavController.TAB1;
    private final int TAB_SECOND = FragNavController.TAB2;

    private DatabaseReference dbRef;
    private MenuItem searchButton;

    private HashMap<String, Pin> pinList;

    private HashMap<String, Pin> nearbyPins;
    private Pin closestPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = initializeToolbar();
        initializeNavDrawer(toolbar);
        initBottomBar();
        initializeFragmentSwitcher();
        dbRef = FirebaseDatabase.getInstance().getReference(KEY_PIN);
        pinList = new HashMap<>();
        nearbyPins = new HashMap<>();
        initPinListener();
    }


    private void initPinListener() {
        // not realtime enough for the recycler... but will do since people aren't interested in
        // getting the new locations right away in this situation
        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Pin newPin = dataSnapshot.getValue(Pin.class);
                pinList.put(dataSnapshot.getKey(), newPin);
                getNearbyPins();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Pin updatedPin = dataSnapshot.getValue(Pin.class);
                pinList.remove(dataSnapshot.getKey());
                pinList.put(dataSnapshot.getKey(), updatedPin);
                if(nearbyPins.containsKey(dataSnapshot.getKey())) {
                    nearbyPins.remove(dataSnapshot.getKey());
                    nearbyPins.put(dataSnapshot.getKey(), updatedPin);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                pinList.remove(dataSnapshot.getKey());
                if(nearbyPins.containsKey(dataSnapshot.getKey())) {
                    nearbyPins.remove(dataSnapshot.getKey());
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private Toolbar initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        return toolbar;
    }

    private void initBottomBar() {
        BottomNavigationView bottomBar = (BottomNavigationView) findViewById(R.id.bottomBar);
        bottomBar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void initializeFragmentSwitcher() {
        List<Fragment> fragments = new ArrayList<>(2);
        fragments.add(new MapFragment());
        fragments.add(new RecyclerFragment());
        fragNavController = new FragNavController(getSupportFragmentManager(), R.id.content, fragments);
        fragNavController.switchTab(TAB_FIRST);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.tab_map:
                    fragNavController.switchTab(TAB_FIRST);
                    if (searchButton != null) {
                        searchButton.setVisible(true);
                    }
                    return true;
                case R.id.tab_near_me:
                    fragNavController.switchTab(TAB_SECOND);
                    if (searchButton != null) {
                        searchButton.setVisible(false);
                    }
                    return true;
                case R.id.tab_emergency:
                    if (closestPin == null || closestPin.getLatLng() == null) {
                        SuperActivityToast.create(MainActivity.this, new Style(),
                                Style.TYPE_STANDARD)
                                .setText(getString(R.string.emergency_not_available))
                                .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                                .setAnimations(Style.ANIMATIONS_POP).show();
                    } else {
                        Toast.makeText(MainActivity.this, R.string.main_emergency_toast, Toast.LENGTH_SHORT).show();
                        Intent navigation = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(
                                R.string.google_maps_query,
                                closestPin.getLatLng().getLatitude(),
                                closestPin.getLatLng().getLongitude()
                        )));
                        startActivity(navigation);
                    }
            }
            return false;
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        searchButton = menu.findItem(R.id.action_search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            try {
                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                        .build(this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                Log.i("TAG_SEARCH", e.getLocalizedMessage());
            }
            return true;
        } else if (id == R.id.action_add) {
            startActivity(new Intent(this, AddActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_logout) {
            showLogoutDialog();
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this).setTitle(R.string.dialog_logout_title)
                .setMessage(R.string.dialog_logout_message)
                .setPositiveButton(R.string.dialog_logout_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.dialog_logout_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(R.drawable.ic_account_box)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
                if (fragment instanceof MapFragment) {
                    ((MapFragment) fragment).moveCamera(place.getLatLng());
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i("TAG_PLACE", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (fragNavController.getCurrentStack().size() > 1) {
            fragNavController.pop();
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @NonNull
    private void initializeNavDrawer(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView tvEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvEmail);
        tvEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        TextView tvUsername = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvUsername);
        tvUsername.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
    }


    public HashMap<String, Pin> getNearbyPins() {
        return nearbyPins;
    }

    private boolean isNearBy(MyLatLng currentLoc, MyLatLng pinLoc) {
        return currentLoc.getDistance(pinLoc) <= HUMAN_WALKABLE_DISTANCE;
    }

    private void findNearbyPins(Location location) {
        nearbyPins.clear();
        for (Map.Entry<String, Pin> current : pinList.entrySet()) {
            if (isNearBy(new MyLatLng(location.getLatitude(), location.getLongitude()),
                    current.getValue().getLatLng())) {
                nearbyPins.put(current.getKey(), current.getValue());
            }
        }
    }

    private void getClosestPin(Location location) {
        Pin closestSoFar = new Pin(); // eventually will get assigned to some real pin
        double distanceSoFar = Integer.MAX_VALUE;
        MyLatLng myLocation = new MyLatLng(location.getLatitude(), location.getLongitude());
        for (Map.Entry<String, Pin> current : pinList.entrySet()) {
            Pin currentPin = current.getValue();
            double currentDistance = currentPin.getLatLng().getDistance(myLocation);
            if (currentDistance < distanceSoFar) {
                closestSoFar = currentPin;
                distanceSoFar = currentDistance;
            }
        }
        closestPin = closestSoFar;
    }

    @Override
    public void onLocationUpdated(Location location) {
        findNearbyPins(location);
        getClosestPin(location);
    }
}


