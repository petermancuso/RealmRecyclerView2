package sensorlab.io.realmrecyclerview.realm;
import java.util.Date;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
@RealmClass
public class Graph implements RealmModel {
    @PrimaryKey
    private String UUID;
    private String title;
    private Date date;
    private int duration;
    private float max;
    private float min;

    public RealmList<DataSet> dataSets;

    public RealmList<DataSet> getDataSets() {
        return dataSets;
    }

    public void setDataSets(RealmList<DataSet> dataSets) {
        this.dataSets = dataSets;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
