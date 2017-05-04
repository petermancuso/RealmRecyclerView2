package sensorlab.io.realmrecyclerview.realm;
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

import com.google.android.gms.cast.LaunchOptions;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import sensorlab.io.realmrecyclerview.Observer;
import sensorlab.io.realmrecyclerview.Subject;
import sensorlab.io.realmrecyclerview.realm.Graph;
import sensorlab.io.realmrecyclerview.realm.GraphLab;
import sensorlab.io.realmrecyclerview.realm.SensorLab;
import sensorlab.io.realmrecyclerview.realm.SensorSpecifications;


public class PositionLab implements GoogleApiClient.OnConnectionFailedListener,
                GoogleApiClient.ConnectionCallbacks, LocationListener, Subject{

    private volatile static PositionLab singleton;
    public ArrayList observers;
    private Context mContext;
    public LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    public Location mLastLocation;
    public Location mCurrentLocation;
    public Date mLastLocationTime;
    private Realm realm;
    private RealmConfiguration realmConfiguration;
    private PositionSet mPositionSet;

    private static GoogleApiClient mGoogleApiClient;

    public static PositionLab getInstance(Context context){
        if(singleton == null){
            synchronized (PositionLab.class){
                if(singleton == null){
                    singleton = new PositionLab(context);
                }
            }
        }

        return singleton;
    }

    private PositionLab(Context context){
        mContext = context;
        Realm.setDefaultConfiguration(this.getRealmConfig());

        if(realm == null) {
            realmConfiguration = getRealmConfig();
        }

        realm = Realm.getDefaultInstance();
        mPositionSet = new PositionSet();

        observers = new ArrayList();
    }

    @Override
    public void registerObserver(Observer o){
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o){
        int i = observers.indexOf(o);
        if(i>=0){
            observers.remove(i);
        }
    }

    @Override
    public void notifyObservers(Location location){
        for(int i = 0; i < observers.size();i++) {
            Observer observer = (Observer) observers.get(i);
            observer.update(location);
        }
    }


    public RealmResults<PositionSet> getPositionSet(){
        final RealmResults<PositionSet> results = realm.where(PositionSet.class).findAll();
        return results;
    }

    public void addPositionSet(final PositionSet positionSet){

        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                realm.copyToRealmOrUpdate(positionSet);
            }
        });
    }

    public void removePositionSet(final String id){

        final RealmResults<PositionSet> results = realm.where(PositionSet.class).equalTo("UUID", id).findAll();

        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                results.deleteAllFromRealm();
            }
        });
    }

    public PositionSet getPositionSet(final String id){
        final PositionSet positionSet = realm.where(PositionSet.class).equalTo("UUID", id).findFirst();
        if (positionSet != null) {
            return positionSet;
        }
        else return null;
    }

    public void updateDataSet(final PositionSet positionSet){

        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                realm.copyToRealmOrUpdate(positionSet);
            }
        });
    }

    public void addPosition(final PositionSet positionSet, final Position position) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                positionSet.positions.add(position);
            }
        });
    }

    public void removeDataPoint(final PositionSet positionSet, final Position position ){

        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                positionSet.positions.remove(position);
            }
        });
    }

    public void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
            }
        });

        mPositionSet = new PositionSet();
        mPositionSet.setUUID(UUID.randomUUID().toString());
        Toast toast = Toast.makeText(mContext, "Started Location Updates", Toast.LENGTH_SHORT);
        toast.show();

    }

    public void stopLocationUpdates() {
        if(mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Toast toast = Toast.makeText(mContext, "Stopped Location Updates", Toast.LENGTH_SHORT);
            toast.show();
            for(int i = 0; i>observers.size();i++){
                Observer observer = (Observer) observers.get(i);
                removeObserver(observer);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
            mCurrentLocation = location;
            mLastLocationTime = Calendar.getInstance().getTime();
            notifyObservers(location);

        /*mCurrentLocation = location;
        mLastLocationTime = Calendar.getInstance().getTime();
        Position position = new Position();
        position.setUUID(mPosition.getUUID());
        position.setLatitude(location.getLatitude());
        position.setLongitude(location.getLongitude());
        position.setRecordTime(Calendar.getInstance().getTime().toString());
        position.setGlobalTime(Calendar.getInstance().getTime().toString());
        //TODO Add Position Set instead of just position.
        addPosition(position);
        //updateUI();*/
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

    }

    @Override
    public void onConnectionSuspended(int cause) {

    }

    public void buildLocationSettingsRequest(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    public void createLocationRequest(Long setInterval, Long fastestInterval, int priority ) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(setInterval);
        mLocationRequest.setFastestInterval(fastestInterval);
        mLocationRequest.setPriority(priority);
    }

    public synchronized void buildGoogleApiClient(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    public RealmConfiguration getRealmConfig() {
        if (realmConfiguration == null) {
            realmConfiguration = new RealmConfiguration
                    .Builder(mContext)
                    .deleteRealmIfMigrationNeeded()
                    .build();
            Realm.setDefaultConfiguration(realmConfiguration);
        }
        return realmConfiguration;
    }

}
