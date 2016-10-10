package com.example.luan.adoptyourstreet.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ScrollingTabContainerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luan.adoptyourstreet.R;
import com.example.luan.adoptyourstreet.adapters.PlaceAutocompleteAdapter;
import com.example.luan.adoptyourstreet.firebase.FireBaseAuthCallback;
import com.example.luan.adoptyourstreet.firebase.FireBaseAuthManager;
import com.example.luan.adoptyourstreet.firebase.FireBaseDatabaseCallback;
import com.example.luan.adoptyourstreet.firebase.FireBaseDatabaseManager;
import com.example.luan.adoptyourstreet.firebase.FireBaseStorageManager;
import com.example.luan.adoptyourstreet.firebase.FirebaseCallBackInActivity;
import com.example.luan.adoptyourstreet.models.DayActivitive;
import com.example.luan.adoptyourstreet.models.Event;
import com.example.luan.adoptyourstreet.models.Prayer;
import com.example.luan.adoptyourstreet.models.User;
import com.example.luan.adoptyourstreet.models.WeekActivitie;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.angmarch.views.NiceSpinner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;


public class CreateEventActivity extends AppCompatActivity implements  GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnCameraChangeListener,
        OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener {

    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapterAtuoComplete;
    private AutoCompleteTextView autoCompleteTextView;
    Toolbar toolbar;
    NiceSpinner spinner;
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));

    static final int PICK_IMAGE_REQUEST=100;
    ImageView imgAdd;

    TextView tvTimeSelect,tvDateSelect;
    Button btnCreateEvent;

    TextView mTvEventName;
    String mTopic;
    Calendar mCurentTime;
    Bitmap mImgaeCurent;
    LatLng mLatlngCurent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        mTopic="Love";
        setActionBar();
        spinner=(NiceSpinner) findViewById(R.id.spinnerCreateEvent);
        ImageView iv=  (ImageView) findViewById(R.id.imgIconBar);
        autoCompleteTextView=(AutoCompleteTextView) findViewById(R.id.autocomplete_places_create);
        autoCompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        imgAdd=(ImageView) findViewById(R.id.addImagEvent);
        tvDateSelect=(TextView) findViewById(R.id.tvSelectDateCreate);
        tvTimeSelect=(TextView )findViewById(R.id.tvSelectTimeCreateEv);
        mTvEventName=(TextView) findViewById(R.id.tvNameEvent);
        btnCreateEvent=(Button) findViewById(R.id.btnCreateEvent);
        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent();
            }
        });
        tvTimeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        CreateEventActivity.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });
        tvDateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        CreateEventActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
// Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        iv.setVisibility(View.GONE);
        setSpinner();
        setTab();
        initMap();
        initAdapter();
        setClickTileViewTab(1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                mImgaeCurent = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));


                imgAdd.setImageBitmap(mImgaeCurent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        FireBaseAuthManager.sharedInstance.onStart();
        if(FireBaseAuthManager.sharedInstance.isLoggedIn()){
            Log.d("login:","ok id:"+ FireBaseAuthManager.sharedInstance.getUserUid());

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag("TimepickerDialog");

        if(tpd != null) tpd.setOnTimeSetListener(this);
        if(dpd != null) dpd.setOnDateSetListener(this);
    }

    private void setSpinner() {
        // Adding child data
        final List<String> item = new ArrayList<String>();
        item.add("Love");
        item.add("Neighbour");
        item.add("Family");
        item.add("School");
        //  spinner.setItems("Love", "Neighbour", "Family", "School");
        spinner.attachDataSource(item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mTopic=item.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void setActionBar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView tv = (TextView) findViewById(R.id.tvActionbar);
        tv.setText("Create Event");
    }
    private void setTimeViewCurent(){

        mCurentTime=Calendar.getInstance();
      showTimeSelectCurent();
    }
    private void showTimeSelectCurent(){
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
        String dateCurent=ft.format(mCurentTime.getTime());
        tvDateSelect.setText(dateCurent);
        ft=new SimpleDateFormat("hh:mm a");
        tvTimeSelect.setText(ft.format(mCurentTime.getTime()));
    }
    private void setClickContentViewTab(int i){
        FrameLayout f1=(FrameLayout) findViewById(R.id.pImageCreate);
        f1.setVisibility(View.GONE);
        FrameLayout f2=(FrameLayout) findViewById(R.id.pMapgCreate);
        f2.setVisibility(View.GONE);
        FrameLayout f3=(FrameLayout) findViewById(R.id.pCoutdownCreate);
        f3.setVisibility(View.GONE);
        if(i==1){
            f1.setVisibility(View.VISIBLE);
        }
        if(i==2){
            f2.setVisibility(View.VISIBLE);
        }
        if(i==3){
            f3.setVisibility(View.VISIBLE);
            setTimeViewCurent();

        }
    }

    private void setClickTileViewTab(int i){
        FrameLayout f1=(FrameLayout) findViewById(R.id.tab1EventCreate);
        f1.setBackgroundColor(getResources().getColor(R.color.ColorEvent));
        FrameLayout f2=(FrameLayout) findViewById(R.id.tab2EventCreate);
        f2.setBackgroundColor(getResources().getColor(R.color.ColorEvent));
        FrameLayout f3=(FrameLayout) findViewById(R.id.tab3EventCreate);
        f3.setBackgroundColor(getResources().getColor(R.color.ColorEvent));
        TextView t1=(TextView) findViewById(R.id.icTitTab1Create);
        TextView t2=(TextView) findViewById(R.id.icTitTab2Create);
        TextView t3=(TextView) findViewById(R.id.icTitTab3Create);
        t1.setVisibility(View.VISIBLE);
        t2.setVisibility(View.VISIBLE);
        t3.setVisibility(View.VISIBLE);
        if(i==1){
            f1.setBackgroundColor(getResources().getColor(R.color.white));
            t1.setVisibility(View.GONE);
        }
        if(i==2){
            f2.setBackgroundColor(getResources().getColor(R.color.white));
            t2.setVisibility(View.GONE);
        }
        if(i==3){
            f3.setBackgroundColor(getResources().getColor(R.color.white));
            t3.setVisibility(View.GONE);
        }

    }

    private void setTab(){
        FrameLayout f1=(FrameLayout) findViewById(R.id.tab1EventCreate);
        FrameLayout f2=(FrameLayout) findViewById(R.id.tab2EventCreate);
        FrameLayout f3=(FrameLayout) findViewById(R.id.tab3EventCreate);
        f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClickTileViewTab(1);
                setClickContentViewTab(1);
            }
        });
        f2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClickTileViewTab(2);
                setClickContentViewTab(2);
            }
        });
        f3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClickTileViewTab(3);
                setClickContentViewTab(3);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void initMap(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapCreateEvent);
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    private void initAdapter(){

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mAdapterAtuoComplete = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_GREATER_SYDNEY,
                null);
        autoCompleteTextView.setAdapter(mAdapterAtuoComplete);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }


    GoogleMap gMap;
    BitmapDescriptor icon= BitmapDescriptorFactory.fromResource(R.drawable.pin_feature_icon);
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        if(gMap==null) gMap=googleMap;
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.d("click map:",latLng.toString());
                mLatlngCurent=latLng;
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(latLng).icon(icon).title("pos"));
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
//                + connectionResult.getErrorCode());

    }

    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapterAtuoComplete.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i("log:", "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
            Log.i("log", "Called getPlaceById to get Place details for " + placeId);
        }
    };
    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e("log ", "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);
            updateMapCurentPlace(place);

            Log.i("log:", "Place details received: " + place.getName());

            places.release();
        }
    };
    private void updateMapCurentPlace(Place p){
        CameraUpdate cameraUpdate= CameraUpdateFactory.newLatLngZoom(p.getLatLng(),10);
        mLatlngCurent=p.getLatLng();
        gMap.animateCamera(cameraUpdate);
        gMap.clear();
        gMap.addMarker(new MarkerOptions().position(p.getLatLng()).icon(icon).title("pos"));
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
       mCurentTime.set(Calendar.YEAR,year);
       mCurentTime.set(Calendar.MONTH,monthOfYear);
       mCurentTime.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        showTimeSelectCurent();
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        mCurentTime.set(Calendar.HOUR_OF_DAY,hourOfDay);
        mCurentTime.set(Calendar.MINUTE,minute);
        mCurentTime.set(Calendar.SECOND,second);
        showTimeSelectCurent();
    }

    private void saveEvent(){
        Event newEvent=new Event(null,mTvEventName.getText().toString(),mTopic,Calendar.getInstance().getTimeInMillis(),mCurentTime.getTimeInMillis()/1000,mLatlngCurent.latitude,mLatlngCurent.longitude,FireBaseAuthManager.sharedInstance.getUserUid());
        FireBaseDatabaseManager.sharedInstance.saveEvent(newEvent);
        if(mImgaeCurent!=null){
            FireBaseStorageManager.sharedInstance.saveImagesEvent(newEvent,mImgaeCurent);
        }
        finish();
    }
}
