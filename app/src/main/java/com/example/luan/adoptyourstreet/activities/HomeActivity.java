package com.example.luan.adoptyourstreet.activities;


import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luan.adoptyourstreet.R;
import com.example.luan.adoptyourstreet.Util.PermissionUtils;
import com.example.luan.adoptyourstreet.Util.Util;
import com.example.luan.adoptyourstreet.adapters.PrayerInfoWindowAdapter;
import com.example.luan.adoptyourstreet.firebase.FireBaseAuthCallback;
import com.example.luan.adoptyourstreet.firebase.FireBaseAuthManager;
import com.example.luan.adoptyourstreet.firebase.FireBaseDatabaseCallback;
import com.example.luan.adoptyourstreet.firebase.FireBaseDatabaseManager;
import com.example.luan.adoptyourstreet.firebase.FireBaseStorageManager;
import com.example.luan.adoptyourstreet.fragment.ActivityFragment;
import com.example.luan.adoptyourstreet.fragment.DialogCreateEvent;
import com.example.luan.adoptyourstreet.fragment.EventsFragment;
import com.example.luan.adoptyourstreet.fragment.GoalFragment;
import com.example.luan.adoptyourstreet.fragment.LoginFragment;
import com.example.luan.adoptyourstreet.fragment.PinPageFragment;
import com.example.luan.adoptyourstreet.fragment.SettingFragment;
import com.example.luan.adoptyourstreet.fragment.ShopFragment;
import com.example.luan.adoptyourstreet.fragment.SignUpResultFragment;
import com.example.luan.adoptyourstreet.models.DayActivitive;
import com.example.luan.adoptyourstreet.models.Prayer;

import com.example.luan.adoptyourstreet.models.PrayerUser;
import com.example.luan.adoptyourstreet.models.User;
import com.example.luan.adoptyourstreet.models.WeekActivitie;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseUser;

import android.location.LocationListener;

import org.angmarch.views.UtilCalendar.UtilCalendar;

import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnCameraChangeListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        FireBaseAuthCallback,
        FireBaseDatabaseCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    // action bar
    private ActionBar actionBar;
    private Toolbar toolbar;
    private TextView tvActionbarTitle;
    private int PosFragmentCurent;


    private GoogleMap mMap;
    private LocationManager locationManager;
    private PolylineOptions mCurrentPath;
    private Polyline mCurrentPolyline;
    private Location mLastPosition;

    /**
     * Prayer
     */
    List<Prayer> prayerList = new ArrayList<>();
    private Map<Marker, Prayer> showingMarkers = new HashMap<>();

    /**
     * UI Work
     */
    private Button mStartPray;
    private Button mStopPray;
    private TextView mTimerText;
    private TextView mDistanceText;

    private boolean bPraying = false;

    private ImageButton savePrayerAddButton = null;
    private ImageButton savePrayerRemoveButton = null;
    private ImageView prayerImageView = null;
    private Bitmap imageAdded = null;
    private InputStream imageAddInputStream = null;

    float mCurrentDistance = 0;

    long startTime = 0;
    long stopTime = 0;
    int duration = 0;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            int hours = minutes / 60;
            seconds = seconds % 60;
            mTimerText.setText(String.format(Locale.ENGLISH, "%d:%02d:%02d", hours, minutes, seconds));
            //     mDistanceText.setText(String.format(Locale.ENGLISH, "%.01f miles", mCurrentDistance));
            timerHandler.postDelayed(this, 60);

        }
    };

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1;


    // update Location
    protected static final String TAG = "location-updates-sample";

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    // Keys for storing activity state in the Bundle.
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";
    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;
    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;
    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;

    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    protected Boolean mRequestingLocationUpdates;

    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setActionBar();
        //   btnCreate=(Button) findViewById(R.id.btnCrateEvent);
        setMap();

        mRequestingLocationUpdates = false;
        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        // Kick off the process of building a GoogleApiClient and requesting the LocationServices
        // API.
        buildGoogleApiClient();
        PosFragmentCurent = 0;
        displayView(PosFragmentCurent);
        showPrayerOrPing((byte) 0);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    boolean checkClickPay = false;

    // show layout Prayer or PingUser
    private void showPrayerOrPing(byte pram) {
        LinearLayout ParentMap, ParentPrayPin;
        ParentMap = (LinearLayout) findViewById(R.id.parentMap);
        ParentPrayPin = (LinearLayout) findViewById(R.id.ParentPayPin);
        FrameLayout layoutPay, layoutPin;
        layoutPay = (FrameLayout) findViewById(R.id.layoutPrayer);
        layoutPin = (FrameLayout) findViewById(R.id.layoutPindetail);
        if (pram == 0) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 7);
            ParentMap.setLayoutParams(params);
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 3);
            ParentPrayPin.setLayoutParams(params);
            layoutPin.setVisibility(View.GONE);
            layoutPay.setVisibility(View.VISIBLE);
            checkClickPay = false;
        } else if (pram == 1) {
            checkClickPay = true;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 7);
            ParentMap.setLayoutParams(params);
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 3);
            ParentPrayPin.setLayoutParams(params);
            layoutPay.setVisibility(View.GONE);
            layoutPin.setVisibility(View.VISIBLE);
        }
        if (checkClickPay)
            ParentMap.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if ((event.getAction() == MotionEvent.ACTION_DOWN) && (checkClickPay)) {
                        showPrayerOrPing((byte) 0);
                        return true;
                    }
                    return false;
                }
            });
    }

    private void ShowUserPrayPindetail(Prayer pr, User user) {
        showPrayerOrPing((byte) 1);
        TextView tv = (TextView) findViewById(R.id.tvTilePindetail);
        TextView tvMilies = (TextView) findViewById(R.id.tvPingdetailDistance);
        double d = (double) Math.round(user.totalDistance.doubleValue() * 1000) / 1000;
        Log.d("Ping:", pr.distance.doubleValue() + "");
        tvMilies.setText(d + "");
        TextView tvTime = (TextView) findViewById(R.id.tvPingDetalTime);
        double time = (double) (Double.parseDouble(user.totalDuration + "") / 60 / 60);
        tvTime.setText(((double) Math.round(time * 1000) / 1000) + "");
        tv.setText(pr.name);
        TextView tvName = (TextView) findViewById(R.id.tvUserNamePingDetail);
        tvName.setText(user.name);

        FireBaseDatabaseManager db = FireBaseDatabaseManager.sharedInstance;
//        db.getUserReference-();
    }

    private void setMap() {
        FireBaseAuthManager.sharedInstance.init();
        FireBaseAuthManager.sharedInstance.setFireBaseAuthCallback(this);
        mStartPray = (Button) findViewById(R.id.startPray);
        mStopPray = (Button) findViewById(R.id.stopPray);

        mStartPray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartPrayClick(v);
            }
        });
        mStopPray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStopPrayClick(v);
            }
        });
        mStopPray.setVisibility(View.INVISIBLE);

        mTimerText = (TextView) findViewById(R.id.timerText);
        mDistanceText = (TextView) findViewById(R.id.currentDistance);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,this );
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

    // set ActionBar
    private void setActionBar() {
        ImageView iv = (ImageView) findViewById(R.id.imgIconBar);
        iv.setVisibility(View.GONE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        tvActionbarTitle = (TextView) findViewById(R.id.tvActionbar);
        tvActionbarTitle.setText("");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //
        View headerView = navigationView.getHeaderView(0);


        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayView(5);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

    }

    // action bar no
    private void setActionBarNoButtonCreate() {
        FrameLayout f = (FrameLayout) findViewById(R.id.freButtonCreate);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 2);
        f.setLayoutParams(params);
    }

    private void setActionBarHasButtonCreate() {
        FrameLayout f = (FrameLayout) findViewById(R.id.freButtonCreate);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 3);
        f.setLayoutParams(params);
    }

    private void setIconActioBar(int src) {
        ImageView iv = (ImageView) findViewById(R.id.imgIconBar);
        iv.setVisibility(View.VISIBLE);
        iv.setImageResource(src);
    }

    private void setTitleActionBar(String tit) {
        TextView tv = (TextView) findViewById(R.id.tvActionbar);
        tv.setText(tit);
    }

    private void demo() {
        Log.d("week:", listWeekActivitie.size() + "");
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            displayView(0);
            // Handle the camera action
        } else if (id == R.id.nav_activity) {

            displayView(1);
        } else if (id == R.id.nav_events) {
            displayView(2);
        } else if (id == R.id.nav_shop) {
            displayView(3);
        } else if (id == R.id.nav_settings) {
            displayView(4);
        } else if (id == R.id.nav_goal) {
            displayView(6);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        Button btnCreate;
        btnCreate = (Button) findViewById(R.id.btnCreateAppBar);
        btnCreate.setVisibility(View.GONE);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCreateEvent dialogCreateEvent = new DialogCreateEvent() {
                    @Override
                    public void onclickOpenShop() {
                        displayView(3);
                    }

                    @Override
                    public void onclickCreateVent() {
                        startActivity(new Intent(getApplicationContext(), CreateEventActivity.class));
                    }
                };
                dialogCreateEvent.showDialog(HomeActivity.this);
            }
        });

        // update the main content by replacing fragments
        if (PosFragmentCurent != position) {
            PosFragmentCurent = position;
            setActionBarNoButtonCreate();

            FrameLayout frameMap, frameFragment;
            frameMap = (FrameLayout) findViewById(R.id.FrameMap);

            frameFragment = (FrameLayout) findViewById(R.id.frame_container);
            frameFragment.setVisibility(View.GONE);
            frameMap.setVisibility(View.GONE);

            Fragment fragment = null;
            switch (position) {
                case 0:
                    //  fragment = new HomepageFragment();
                    frameMap.setVisibility(View.VISIBLE);
                    setIconActioBar(R.mipmap.ic_menuhome);
                    setTitleActionBar("Home");
                    setMap();
                    break;
                case 1:
                    frameFragment.setVisibility(View.VISIBLE);

                    fragment = new ActivityFragment(listWeekActivitie);
                    setIconActioBar(R.mipmap.ic_menuactivity);
                    setTitleActionBar("Activity");

                    break;
                case 2:
                  //  FireBaseDatabaseManager.sharedInstance.getCheckDateByTimeAndWeek(CurrentWeek.weekId, Util.getInstaceResetDate(Calendar.getInstance()));
                    setActionBarHasButtonCreate();
                    frameFragment.setVisibility(View.VISIBLE);
                    int data = 3;
                    fragment = new EventsFragment(data);
                    btnCreate.setVisibility(View.VISIBLE);
                    setIconActioBar(R.mipmap.ic_menu_event);
                    setTitleActionBar("Events");
                    break;

                case 3:
                    fragment = new ShopFragment();
                    frameFragment.setVisibility(View.VISIBLE);
                    setIconActioBar(R.mipmap.ic_menu_shop);
                    setTitleActionBar("Shop");
                    break;
                case 4:
                    fragment = new SettingFragment();
                    frameFragment.setVisibility(View.VISIBLE);
                    setIconActioBar(R.mipmap.ic_menu_setting);
                    setTitleActionBar("Settings");
                    break;
                case 5:
                    fragment = new LoginFragment() {
                        @Override
                        public void ResultSignUp() {
                            displayView(7);
                        }
                    };
                    frameFragment.setVisibility(View.VISIBLE);
                    setIconActioBar(R.mipmap.ic_user);
                    setTitleActionBar("User");
                    break;
                case 6:
                    fragment = new GoalFragment();
                    frameFragment.setVisibility(View.VISIBLE);
                    setIconActioBar(R.mipmap.ic_goal);
                    setTitleActionBar("Goal");
                    break;
                case 7:
                    fragment = new SignUpResultFragment();
                    frameFragment.setVisibility(View.VISIBLE);
                    setIconActioBar(R.mipmap.ic_user);
                    setTitleActionBar("User");
                default:
                    break;
            }

            if (fragment != null) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment).commit();

            } else {
                // error in creating fragment
                Log.e("MainActivity", "Error in creating fragment");
            }
        }
    }

    // click avata user as login
    private void clickInLogin(View v) {
        RelativeLayout bt = (RelativeLayout) findViewById(R.id.btnOnclikUser);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            }
        });
    }


    // function Map- -----------------------------
    @Override
    protected void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        mGoogleApiClient.connect();
        FireBaseAuthManager.sharedInstance.onStart();
        if (!FireBaseAuthManager.sharedInstance.isLoggedIn()) {
            FireBaseAuthManager.sharedInstance.signInAnonymously();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.

        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
        FireBaseAuthManager.sharedInstance.onTerm();
        FireBaseDatabaseManager.sharedInstance.removeEvent();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    /**
     * Location stuff
     */

    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
            return;
        }
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        mMap.setMyLocationEnabled(true);
        goToLastKnowPosition();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }
        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            enableMyLocation();
        } else {
            mPermissionDenied = true;
        }
    }

    private void goToLastKnowPosition() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (lastKnownLocation == null) {
            return;
        }
        mLastPosition = lastKnownLocation;

        LatLng latLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        mMap.animateCamera(cameraUpdate);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new PrayerInfoWindowAdapter(this, showingMarkers) {
            @Override
            public void renderViewPin(String tit, Prayer pr) {
                FireBaseDatabaseManager.sharedInstance.getUserFromPrayer(pr);
            }
        });

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnCameraChangeListener(this);
        enableMyLocation();
        mCurrentPath = new PolylineOptions();
        mCurrentPath.width(5.0f);
        mCurrentPath.color(Color.RED);
        mMap.addPolyline(mCurrentPath);
    }

    /**
     * Map callback event
     */

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        checkAllPrayer();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    /**
     * Location listener
     */
    double mPrayerDistance;

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 20);
        mMap.animateCamera(cameraUpdate);
        Location oldLocation = mLastPosition;
        if (bPraying) {
            float distance = location.distanceTo(oldLocation);
            if (distance >= 4) {
                mCurrentPath.add(new LatLng(location.getLatitude(), location.getLongitude()));
                mCurrentPath.width(5).color(Color.RED);
                mMap.addPolyline(mCurrentPath);
                mPrayerDistance += distance;
                mPrayerDistance=(float)Math.round(mPrayerDistance*100)/100;
            }
//            mDistanceText.setText(String.format(Locale.ENGLISH, "%.01f miles", mPrayerDistance));
            mDistanceText.setText(mPrayerDistance + "");
        }
        mLastPosition = location;
    }


    /**
     * Handle event from UI
     */

    public void onStartPrayClick(View v) {
        if (v.getId() == R.id.startPray) {
            mCurrentDistance = 0;
            duration = 0;
            bPraying = true;
            mStopPray.setVisibility(View.VISIBLE);
            mStartPray.setVisibility(View.INVISIBLE);
            //-      mCurrentPath.add(new LatLng(mLastPosition.getLatitude(), mLastPosition.getLongitude()));
            mCurrentPath.width(5).color(Color.RED);
            mCurrentPath.getPoints().clear();
            mCurrentPolyline = mMap.addPolyline(mCurrentPath);
            startTime = System.currentTimeMillis();
            timerHandler.postDelayed(timerRunnable, 60);
            mCurrentDistance = 0f;
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
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            AddPrayerUserWittStart();
            mPrayerDistance = 0;
//            AddPrayerUserWittStart();

            startUpdatesButtonHandler();
        }
    }

    public void onStopPrayClick(View v) {
        if (v.getId() == R.id.stopPray) {
            bPraying = false;
            mStartPray.setVisibility(View.VISIBLE);
            mStopPray.setVisibility(View.INVISIBLE);
            timerHandler.removeCallbacks(timerRunnable);
            stopTime = System.currentTimeMillis();
            duration = (int) (stopTime - startTime);
            Log.d("Time:", duration + "");
            showPopupSavePrayer();
            stopUpdatesButtonHandler();
            mCurrentPolyline.remove();
        }
    }

    //
    private static final int PICK_IMAGE = 600;

    //
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    try {
                        imageAddInputStream = getContentResolver().openInputStream(imageReturnedIntent.getData());
                        imageAdded = BitmapFactory.decodeStream(imageAddInputStream);
                        prayerImageView.setImageBitmap(imageAdded);
                        savePrayerAddButton.setVisibility(View.INVISIBLE);
                        savePrayerRemoveButton.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                    }
                }
        }
    }

    //
    void showPopupSavePrayer() {
        if(UserAnonymous==null){
            UserAnonymous=new User();
        }

        LayoutInflater inflater = (LayoutInflater)
                this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View mainView = this.findViewById(R.id.layout);
        View popupView = inflater.inflate(R.layout.save_prayer_popup, null, false);
        View imageHolder = popupView.findViewById(R.id.imageHolder);
        final PopupWindow pw = new PopupWindow(
                popupView,
                mainView.getWidth() * 4 / 5,
                mainView.getHeight() / 2,
                true);

        prayerImageView = (ImageView) imageHolder.findViewById(R.id.imageSavePrayer);
        savePrayerAddButton = (ImageButton) imageHolder.findViewById(R.id.addButtonSavePrayer);
        savePrayerRemoveButton = (ImageButton) imageHolder.findViewById(R.id.removeButtonSavePrayer);

        Button buttonSave = (Button) popupView.findViewById(R.id.savePrayerButton);

        final EditText nameEditText = (EditText) popupView.findViewById(R.id.commentSavePrayer);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                Prayer newPrayer = new Prayer("0 mph",
                        (double) mPrayerDistance,
                        duration / 1000,
                        (double) stopTime,
                        imageAdded != null,
                        mLastPosition.getLatitude(),
                        mLastPosition.getLongitude(),
                        new Double(0),
                        name,
                        idPrayerOfUserCurrentStart,
                        FireBaseAuthManager.sharedInstance.getUserUid(),
                        null);
                PrayerUser prayerUser=new PrayerUser("",mPrayerDistance,duration/1000,(double)stopTime,0.0,newPrayer.uid);
                prayerUser.id=idPrayerOfUserCurrentStart;
                FireBaseDatabaseManager.sharedInstance.savePrayer(newPrayer);
                prayerUser.prayerId=newPrayer.uid;
                Log.d("id prayer:",newPrayer.uid);
                FireBaseDatabaseManager.sharedInstance.savePrayerUser(CurrentWeek.weekId,CurentDay.DayId,prayerUser);
                if(imageAdded!=null){
                    FireBaseStorageManager.sharedInstance.saveImage(newPrayer, imageAdded);
                }
                UserAnonymous.numPrayer++;
                UserAnonymous.totalDistance = UserAnonymous.totalDistance + newPrayer.distance;
                UserAnonymous.totalDuration = UserAnonymous.totalDuration + newPrayer.duration;
                FireBaseDatabaseManager.sharedInstance.saveUser(UserAnonymous);
                listWeekActivitie.clear();
                FireBaseDatabaseManager.sharedInstance.getEventWeek();
                Calendar ca = Calendar.getInstance();

                pw.dismiss();
            }
        });
        savePrayerAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
        savePrayerRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageAdded = null;
                imageAddInputStream = null;
                prayerImageView.setImageResource(R.drawable.default_image);
                savePrayerRemoveButton.setVisibility(View.INVISIBLE);
                savePrayerAddButton.setVisibility(View.VISIBLE);
            }
        });
        savePrayerRemoveButton.setVisibility(View.INVISIBLE);
        pw.setFocusable(true);
        pw.showAtLocation(mainView, Gravity.CENTER, 0, 0);
    }

    /**
     * FireBase Stuff
     */

    @Override
    public void onLoginSuccess(FirebaseUser user) {
//        checkWeek=1;

        FireBaseDatabaseManager.sharedInstance.getUserReference(user.getUid());
        FireBaseDatabaseManager.sharedInstance.getPrayerReference();
        FireBaseDatabaseManager.sharedInstance.addEvent();
        FireBaseDatabaseManager.sharedInstance.setFireBaseDatabaseDelegate(this);
        checkWeek();
        FireBaseStorageManager.sharedInstance.init();
        Log.d("demo:","ok");

    }

    @Override
    public void OnPrayerAdded(Prayer newPrayer) {
        ArrayList<Integer> arr = new ArrayList<>();
        for (Prayer prayer : prayerList) {
            if (prayer.uid == newPrayer.uid) {
                return;
            }
        }
        prayerList.add(newPrayer);
        addMarkerWithPrayer(newPrayer);
    }

    User UserAnonymous = null;

    @Override
    public void OnUserUpdated(User user) {
        if (user != null) {
            user.userId = FireBaseAuthManager.sharedInstance.getUserUid();
            UserAnonymous = user;
        }
    }

    @Override
    public void showUserFromPrayer(User user, Prayer prayer) {
        ShowUserPrayPindetail(prayer, user);
    }


    // start prayer and add save
    private void AddPrayerUserWittStart() {
        if(hasWeek&&hasDate){
            FireBaseDatabaseManager.sharedInstance.AddlocaltionPoint(CurrentWeek.weekId, CurentDay.DayId, mCurrentLocation);
            addLocaltionPoint = false;
        } else if ((hasWeek)&&(!hasDate)) {
            Calendar ca = Calendar.getInstance();
            ca.set(Calendar.HOUR_OF_DAY, 0);
            ca.set(Calendar.MINUTE, 0);
            ca.set(Calendar.SECOND, 0);
            ca.set(Calendar.MILLISECOND, 0);
            FireBaseDatabaseManager.sharedInstance.addDateActiviies(CurrentWeek.weekId, 100, 2, -ca.getTimeInMillis());
            AddWeekNew = 0;
            addLocaltionPoint = true;
            FireBaseDatabaseManager.sharedInstance.getDayByWeek(CurrentWeek);
        }     else if (!hasWeek) {
                 AddWeekWitNotWeek();
                 addLocaltionPoint=true;
        }
    }

    private void AddWeekWitNotWeek() {
        checkWeek = 0;
        Calendar c = Calendar.getInstance();
        Date d = UtilCalendar.getMondayOfWeek(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        c.setTime(d);
        c.set(Calendar.MILLISECOND, 0);
        FireBaseDatabaseManager.sharedInstance.addweekActivities(20, 15, -c.getTimeInMillis());
        AddWeekNew = 1;

    }

    public void checkWeek() {
        CurrentWeek = CurentDay = null;
        hasWeek=false; hasDate = false;
        Calendar c = Calendar.getInstance();
        Date d = UtilCalendar.getMondayOfWeek(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        c.setTime(d);
        c.set(Calendar.MILLISECOND, 0);
        FireBaseDatabaseManager.sharedInstance.getCheckWeekByTime(Util.getInstaceResetDate(c));
        checkWeek = 1;
    }

    private void getDayByWeeK(WeekActivitie week) {
        if (listDayActivitives == null) listDayActivitives = new ArrayList<>();
        listDayActivitives.clear();
        checkDate = 1;
        FireBaseDatabaseManager.sharedInstance.getDayByWeek(week);
    }

    WeekActivitie CurrentWeek = null;
    DayActivitive CurentDay = null;
    ArrayList<WeekActivitie> listWeekActivitie = new ArrayList<>();
    ArrayList<DayActivitive> listDayActivitives = new ArrayList<>();
    int AddWeekNew, checkDate, checkWeek = 0;
    boolean addLocaltionPoint, hasWeek, hasDate = false;


    @Override
    public void OnWeekActivitie(WeekActivitie weekActivitie) {
        listWeekActivitie.add(weekActivitie);
        Log.d("demo:","ok2");
    }

    @Override
    public void OnDayOfWeekActivive(DayActivitive dayActivitive) {
    }

    String idPrayerOfUserCurrentStart = "";

    @Override
    public void StartPrayer(String idPray) {
        idPrayerOfUserCurrentStart = idPray;
    }

    @Override
    public void OnGetWeekCheck(WeekActivitie w) {
        if (checkWeek == 1) {
            CurrentWeek = w;
            hasWeek = true;
            checkDate = 1;
            Calendar calendar = Util.getInstaceResetDate(Calendar.getInstance());
            FireBaseDatabaseManager.sharedInstance.getCheckDateByTimeAndWeek(w.weekId, calendar);
            checkWeek = 0;
        }
        if (AddWeekNew == 1) {
            CurrentWeek = w;
            hasWeek = true;
            checkDate = 1;
            Calendar c = Util.getInstaceResetDate(Calendar.getInstance());
            FireBaseDatabaseManager.sharedInstance.addDateActiviies(CurrentWeek.weekId, 100, 2, -c.getTimeInMillis());
            AddWeekNew = 0;
           FireBaseDatabaseManager.sharedInstance.getCheckDateByTimeAndWeek(CurrentWeek.weekId, c);

        }
    }

    @Override
    public void OnGetDayCheck(DayActivitive w) {
        if (checkDate == 1) {
            CurentDay = w;
            checkDate = 0;
            hasDate=true;
        }
        if(addLocaltionPoint){
            FireBaseDatabaseManager.sharedInstance.AddlocaltionPoint(CurrentWeek.weekId, CurentDay.DayId, mCurrentLocation);
            addLocaltionPoint = false;
            hasDate=true;
        }

    }

    private void addMarkerWithPrayer(Prayer prayer) {
        if (mMap == null) {
            return;
        }
        LatLng latLng = new LatLng(prayer.lastLatitude, prayer.lastLongitude);
        Boolean bNoName = false;
        Boolean bFeature = prayer.hasImage;
        String title = prayer.name;
        if (title.isEmpty()) {
            title = "Unnamed Preayer";
            bNoName = true;
        }

        BitmapDescriptor icon;
        if (bFeature) {
            icon = BitmapDescriptorFactory.fromResource(R.drawable.pin_feature_icon);
        } else if (bNoName) {
            icon = BitmapDescriptorFactory.fromResource(R.drawable.pin_noname_icon);
        } else {
            icon = BitmapDescriptorFactory.fromResource(R.drawable.pin_normal_icon);
        }

        if (mMap.getProjection().getVisibleRegion().latLngBounds.contains(latLng)) {
            Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(title).icon(icon));
            prayer.marker = marker;
            showingMarkers.put(marker, prayer);
        }
    }

    private void checkAllPrayer() {
        for (Prayer prayer : prayerList) {
            LatLng latLng = new LatLng(prayer.lastLatitude, prayer.lastLongitude);
            if (mMap.getProjection().getVisibleRegion().latLngBounds.contains(latLng)) {
                if (prayer.marker == null) {
                    Boolean bNoName = false;
                    Boolean bFeature = prayer.hasImage;

                    String title = prayer.name;
                    if (title.isEmpty()) {
                        title = "Unnamed Prayer";
                        bNoName = true;
                    }

                    BitmapDescriptor icon;

                    if (bFeature) {
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.pin_feature_icon);
                    } else if (bNoName) {
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.pin_noname_icon);
                    } else {
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.pin_normal_icon);
                    }
                    Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(title).icon(icon));
                    prayer.marker = marker;
                    showingMarkers.put(marker, prayer);
                }
            } else if (prayer.marker != null) {
                prayer.marker.remove();
                showingMarkers.remove(prayer.marker);
                prayer.marker = null;
            }
        }

    }

    /**
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        Log.i(TAG, "Updating values from bundle");
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                // Since LOCATION_KEY was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
            }

        }
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Handles the Start Updates button and requests start of location updates. Does nothing if
     * updates have already been requested.
     */
    public void startUpdatesButtonHandler() {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            startLocationUpdates();
        }
    }

    /**
     * Handles the Stop Updates button, and requests removal of location updates. Does nothing if
     * updates were not previously requested.
     */
    public void stopUpdatesButtonHandler() {
        if (mRequestingLocationUpdates) {
            mRequestingLocationUpdates = false;

            stopLocationUpdates();
        }
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
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
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }


    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    /**
     * Stores activity data in the Bundle.
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if (mCurrentLocation == null) {
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
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

        }

        // If the user presses the Start Updates button before GoogleApiClient connects, we set
        // mRequestingLocationUpdates to true (see startUpdatesButtonHandler()). Here, we check
        // the value of mRequestingLocationUpdates and if it is true, we start location updates.
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Home Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
}