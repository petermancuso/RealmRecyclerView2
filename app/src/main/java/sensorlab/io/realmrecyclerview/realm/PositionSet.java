package sensorlab.io.realmrecyclerview.realm;

import io.realm.annotations.RealmClass;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.RealmClass;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
@RealmClass
public class PositionSet implements RealmModel {
    @PrimaryKey
    private String UUID;
    private int totalRecords;
    private long startTime;
    private long endTime;
    private long elapsedTime;
    private String date;

    public RealmList<Position> positions;

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public RealmList<Position> getPositions() {
        return positions;
    }

    public void setPositions(RealmList<Position> positions) {
        this.positions = positions;
    }
}
