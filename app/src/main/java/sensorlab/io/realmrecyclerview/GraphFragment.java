package sensorlab.io.realmrecyclerview;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Location;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.RealmResults;
import sensorlab.io.realmrecyclerview.realm.Graph;
import sensorlab.io.realmrecyclerview.realm.GraphLab;
import sensorlab.io.realmrecyclerview.realm.PositionLab;
import sensorlab.io.realmrecyclerview.realm.SensorLab;
import sensorlab.io.realmrecyclerview.realm.SensorSpecifications;

public class GraphFragment extends Fragment implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener, Observer {

    private static final String ARG_GRAPH_ID = "graph_id";
    private final int MY_PERMISSION_FINE_LOCATION = 1;

    private boolean fineLocation = false; // Permission for fine location access
    protected LocationSettingsRequest mLocationSettingsRequest;
    protected Location mLastLocation;
    protected Location mCurrentLocation;
    protected Date mLastLocationTime;
    protected Button mStartUpdatesButton;
    protected Button mStopUpdatesButton;
    protected LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private TextView mlatitude;
    private TextView mlongitude;
    private TextView mLastUpdateTime;
    protected String mLatitudeLabel;
    protected String mLongitudeLabel;

    protected SensorManager mSensorManager;
    protected List<Sensor> mSensorList;
    protected TextView mSensorListText;

    private EditText mTitleField;
    private Graph mGraph;
    private GraphLab graphLab;
    private TextView mUUID;
    private TextView mDateField;
    private RealmResults<SensorSpecifications> mSensors;

    private Callbacks mCallbacks;

    @Override
    public void update(Location location){

        mlatitude.setText("Latitude: " + String.valueOf((location.getLatitude())));
        mlongitude.setText("Logitude: " + String.valueOf(location.getLongitude()));
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss:SSS");
        String reportDate = df.format(PositionLab.getInstance(getContext()).mLastLocationTime);
        mLastUpdateTime.setText(reportDate);
        //Toast toast = Toast.makeText(getContext(), "Update Called", Toast.LENGTH_SHORT);
        //toast.show();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String UUID = (String) getArguments().getSerializable((ARG_GRAPH_ID));
        mGraph = GraphLab.getInstance(getActivity()).getGraph(UUID);
        graphLab = GraphLab.getInstance(getContext());
        returnResult();

        // Check for fine location access

        if ( ContextCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_FINE_LOCATION )
                != PackageManager.PERMISSION_GRANTED ) {
            // Should we show an explanation?
            if(ActivityCompat.shouldShowRequestPermissionRationale(
                    getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                        getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_FINE_LOCATION);
            }
        }

        //TODO this blows up, fix buildSensorList
        SensorLab.getInstance(getContext()).buildSensorList(getContext());
        PositionLab.getInstance(getContext()).buildGoogleApiClient(getContext());


        //buildGoogleApiClient();
        PositionLab.getInstance(getContext()).createLocationRequest(1000l, 1000l, LocationRequest.PRIORITY_HIGH_ACCURACY);
        //createLocationRequest(1000l, 1000l, LocationRequest.PRIORITY_HIGH_ACCURACY);
        PositionLab.getInstance(getContext()).buildLocationSettingsRequest();
        //buildLocationSettingsRequest();

        PositionLab.getInstance(getContext()).registerObserver(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.detail_view, container, false);

        mTitleField = (EditText) v.findViewById(R.id.graph_title);

        mUUID = (TextView) v.findViewById(R.id.graph_id);
        mUUID.setText(mGraph.getUUID());

        mStartUpdatesButton = (Button) v.findViewById(R.id.start_updates_button);
        mStopUpdatesButton = (Button) v.findViewById(R.id.stop_updates_button);

        mStartUpdatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PositionLab.getInstance(getContext()).startLocationUpdates();
            }
        });


        mStopUpdatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PositionLab.getInstance(getContext()).stopLocationUpdates();
            }
        });

        mTitleField = (EditText) v.findViewById(R.id.graph_title);
        mTitleField.setText(mGraph.getTitle());

        mDateField = (TextView) v.findViewById(R.id.graph_date);
        mDateField.setText(mGraph.getDate().toString());

        mlatitude = (TextView) v.findViewById(R.id.graph_latitude);
        mlongitude = (TextView) v.findViewById(R.id.graph_longitude);

        mLatitudeLabel = "Latitude";
        mLongitudeLabel = "Longitude";

        //TODO Switch to using Singleton
        buildGoogleApiClient();

        mLastUpdateTime = (TextView) v.findViewById((R.id.graph_last_location_time));
        mLastUpdateTime.setText(DateFormat.getTimeInstance().format(new Date()));

        mlatitude.setText("Latitude");
        mlongitude.setText("Longitude");
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Graph graph = new Graph();
                graph.setUUID(mGraph.getUUID());
                graph.setTitle(s.toString());
                graph.setDate(mGraph.getDate());

                mCallbacks.onGraphUpdated(mGraph);
                graphLab.updateGraph(graph);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSensorListText = (TextView) v.findViewById(R.id.sensor_list);
        mSensorListText.setText(SensorLab.getInstance(getContext()).listRawSensors());
        mSensors = SensorLab.getInstance(getContext()).getSensors();

        return v;
    }

    public interface Callbacks {
        //Required interface for hosting activities.
        void onGraphUpdated(Graph graph);
        void onGraphDeleted(Graph graph);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        PositionLab.getInstance(getContext()).stopLocationUpdates();

    }


    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
            }
        });



        Toast toast = Toast.makeText(getActivity(), "Start Location Updates", Toast.LENGTH_SHORT);
        toast.show();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates( mGoogleApiClient, this);
        Toast toast = Toast.makeText(getContext(), "Stop Location Updates", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onRequestPermissionsResult(int resultCode, String permissions[], int[] grantresults){
        switch (resultCode) {
            case MY_PERMISSION_FINE_LOCATION: {
                //Permission granted, set flag, proceed with location setup
                if(grantresults.length  > 0 && grantresults[0] == PackageManager.PERMISSION_GRANTED){
                    enableLocation();
                    PositionLab.getInstance(getContext()).startLocationUpdates();
                } else {
                    //permission denied, set flag, disable location functionality
                    disableLocation();
                }
                return;
            }
        }
    }

    public void disableLocation(){
        fineLocation = false;
    }

    public void enableLocation(){
        fineLocation = true;
    }
    @Override
    public void onPause() {
        super.onPause();
        GraphLab.getInstance(getActivity()).updateGraph(mGraph);
        if(mGoogleApiClient.isConnected()) {
            PositionLab.getInstance(getContext()).stopLocationUpdates();
        }

        PositionLab.getInstance(getContext()).removeObserver(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mGoogleApiClient.isConnected()) {
            PositionLab.getInstance(getContext()).removeObserver(this);
            PositionLab.getInstance(getContext()).startLocationUpdates();
        }

    }

    public void updateUI(){
        mlatitude.setText("Latitude: " + String.valueOf((PositionLab.getInstance(getContext()).mCurrentLocation.getLatitude())));
        mlongitude.setText("Logitude: " + String.valueOf(PositionLab.getInstance(getContext()).mCurrentLocation.getLongitude()));
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss:SSS");
        String reportDate = df.format(PositionLab.getInstance(getContext()).mLastLocationTime);
        mLastUpdateTime.setText(reportDate);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastLocationTime = Calendar.getInstance().getTime();
        //updateUI();

    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mlatitude.setText(String.format("%s: %f", mLatitudeLabel,
                    mLastLocation.getLatitude()));
            mlongitude.setText(String.format("%s: %f", mLongitudeLabel,
                    mLastLocation.getLongitude()));
            mDateField.setText(DateFormat.getTimeInstance().format(new Date()));
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest(Long setInterval, Long fastestInterval, int priority ) {

                mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(setInterval);
                mLocationRequest.setFastestInterval(fastestInterval);
                mLocationRequest.setPriority(priority);
                Toast toast = Toast.makeText(getActivity(), "Set Interval: " + setInterval +"ms " +"Fastest Interval: "+ fastestInterval +"ms " , Toast.LENGTH_SHORT);
                toast.show();

    }

    protected void buildLocationSettingsRequest(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    public void returnResult() {
        Intent data = new Intent();
        data.putExtra(ARG_GRAPH_ID, mGraph.getUUID());
        getActivity().setResult(Activity.RESULT_OK, data);
    }

    public static GraphFragment newInstance(String UUID) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_GRAPH_ID, UUID);
        GraphFragment fragment = new GraphFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
