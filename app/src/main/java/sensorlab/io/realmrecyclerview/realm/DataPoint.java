package sensorlab.io.realmrecyclerview.realm;
import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
@RealmClass
public class DataPoint implements RealmModel {
    @PrimaryKey
    private String UUID;
    private float yValue;
    private float xValue;
    private double latitude;
    private double longitude;
    private String sensor;

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public DataPoint(String UUID, float xValue, float yValue, float latitude, float longitude) {
        this.UUID = UUID;
        this.xValue = xValue;
        this.yValue = yValue;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public DataPoint() {

    }

    public DataPoint(float yValue) {
        this.yValue = yValue;
    }

    public DataPoint(float xValue, float yValue) {
        this.yValue = yValue;
        this.xValue = xValue;
    }

    public float getyValue() {
        return yValue;
    }

    public void setyValue(float yValue) {
        this.yValue = yValue;
    }

    public float getxValue() {
        return xValue;
    }

    public void setxValue(float xValue) {
        this.xValue = xValue;
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

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }
}