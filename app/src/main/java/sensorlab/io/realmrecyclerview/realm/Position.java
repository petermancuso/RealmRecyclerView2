package sensorlab.io.realmrecyclerview.realm;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

import io.realm.annotations.PrimaryKey;

@RealmClass


public class Position implements RealmModel {

    @PrimaryKey
    private String UUID;
    private double latitude;
    private double longitude;
    private String recordTime;
    private String globalTime;

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getGlobalTime() {
        return globalTime;
    }

    public void setGlobalTime(String globalTime) {
        this.globalTime = globalTime;
    }
}
