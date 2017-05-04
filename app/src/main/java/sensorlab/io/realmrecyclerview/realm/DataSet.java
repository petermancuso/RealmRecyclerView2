package sensorlab.io.realmrecyclerview.realm;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.RealmClass;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
@RealmClass
public class DataSet implements RealmModel {
    @PrimaryKey
    private String UUID;
    private int totalRecords;
    private long startTime;
    private long endTime;
    private long elapsedTime;
    private String date;
    private Boolean hasLocationData;

    public RealmList<DataPoint> dataPoints;

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getUUID() {
        return UUID;
    }

    public Boolean getHasLocationData() {
        return hasLocationData;
    }

    public void setHasLocationData(Boolean hasLocationData) {
        this.hasLocationData = hasLocationData;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public RealmList<DataPoint> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(RealmList<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
