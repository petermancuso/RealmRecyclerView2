package sensorlab.io.realmrecyclerview.realm;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
@RealmClass
public class SensorSpecifications implements RealmModel {
    @PrimaryKey
    private String UUID;
    private int type;
    private String name;
    private String vendor;
    private float dataRate;
    private String units;
    private float range;
    private float resolution;
    private float delay;
    private float power;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDelay() {
        return delay;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getDataRate() {
        return dataRate;
    }

    public void setDataRate(float dataRate) {
        this.dataRate = dataRate;
    }

    public Float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public Float getPower() {
        return power;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public Float getResolution() {
        return resolution;
    }

    public void setResolution(float resolution) {
        this.resolution = resolution;
    }
}
