package sensorlab.io.realmrecyclerview.realm;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

import static android.content.Context.SENSOR_SERVICE;

public class SensorLab {
    private volatile static SensorLab singleton;
    private Context mContext;
    private Realm realm;
    private RealmConfiguration realmConfiguration;

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private RealmResults<Graph> mGraphs;
    private List<Sensor> mSensorList;
    private List<DataSet> mDataSources;

    public static SensorLab getInstance(Context context){
        if(singleton == null){
            synchronized (SensorLab.class){
                if(singleton == null){
                    singleton = new SensorLab(context);
                }
            }
        }
        return singleton;
    }

    private SensorLab(Context context){
        mContext = context;
        Realm.setDefaultConfiguration(this.getRealmConfig());
        realm = Realm.getDefaultInstance();
        realmConfiguration = getRealmConfig();
        //deleteAllGraphs();

    }

    public RealmResults<SensorSpecifications> getSensors(){

        RealmResults<SensorSpecifications> sensors = realm
                .where(SensorSpecifications.class)
                .findAll();
        return sensors;
    }

    public String listRawSensors(){

        String sensorList = new String();
        RealmResults<SensorSpecifications> sensors = realm
                .where(SensorSpecifications.class)
                .findAllSorted("type", Sort.ASCENDING);

        sensorList += sensors.size() + " Sensors\n\n";
        for(SensorSpecifications s : sensors){
            sensorList += s.getType() + ": " + s.getName()+ "\n";
            sensorList += "Vendor: " + s.getVendor() + "\n";
            if( s.getType() == 7 || s.getType() == 13) {
                sensorList += "Range: " + s.getRange() + s.getUnits() + "\n";
            }
            else {
                sensorList += "Range: " + s.getRange() + " " + s.getUnits() + "\n";
            }
            
            sensorList += "Resolution: " + s.getResolution() + " " + s.getUnits() + "\n";
            sensorList += "Delay: " + s.getDelay() + " μs \n";
            sensorList += "Power: " + s.getPower() + " mA\n\n";

        }
        return sensorList;
    }

    public void addSensor(final SensorSpecifications sensor){

        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                realm.copyToRealm(sensor);
            }
        });
    }

    public void removeSensor(final String id) {

        final RealmResults<SensorSpecifications> results = realm.where(SensorSpecifications.class).equalTo("UUID", id).findAll();
        if (results != null) {

            realm.executeTransaction(new Realm.Transaction(){
                @Override
                public void execute(Realm realm){
                    results.deleteAllFromRealm();
                }
            });
        }
        mSensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL );
    }

    public SensorSpecifications getSensor(final String id){
        final SensorSpecifications sensor = realm.where(SensorSpecifications.class).equalTo("UUID", id).findFirst();
        if (sensor != null) {
            return sensor;
        }
        else return null;
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

    public void buildSensorList(Context context){
        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);


        //Delete current SensorSpecifications
        final RealmResults<SensorSpecifications> results = realm.where(SensorSpecifications.class).findAll();
        if (results != null) {

            realm.executeTransaction(new Realm.Transaction(){
                @Override
                public void execute(Realm realm){
                    results.deleteAllFromRealm();
                }
            });
        }

        mSensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        SensorSpecifications tmp = new SensorSpecifications();

        for (int i = 0; i < mSensorList.size(); i++) {

            if (mSensorList.get(i).getType() == Sensor.TYPE_ACCELEROMETER) {
                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(1);
                tmp.setName("Accelerometer X");
                tmp.setUnits("m/s^2");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);

                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(1);
                tmp.setName("Accelerometer Y");
                tmp.setUnits("m/s^2");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);

                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(1);
                tmp.setName("Accelerometer Z");
                tmp.setUnits("m/s^2");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);
                continue;
            }

            if (mSensorList.get(i).getType() == Sensor.TYPE_MAGNETIC_FIELD) {

                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(2);
                tmp.setName("Magnetic Field X");
                tmp.setUnits("μT");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);

                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(2);
                tmp.setName("Magnetic Field Y");
                tmp.setUnits("μT");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);

                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(2);
                tmp.setName("Magnetic Field Z");
                tmp.setUnits("μT");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);
                continue;
            }


            if (mSensorList.get(i).getType() == Sensor.TYPE_ORIENTATION) {
                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(3);
                tmp.setName("Orientation X");
                tmp.setUnits("°");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);

                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(3);
                tmp.setName("Orientation Y");
                tmp.setUnits("°");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);

                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(3);
                tmp.setName("Orientation Z");
                tmp.setUnits("°");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);
                continue;
            }

            if (mSensorList.get(i).getType() == Sensor.TYPE_GYROSCOPE) {
                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(4);
                tmp.setName("Gyroscope X");
                tmp.setUnits("rad/s");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);

                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(4);
                tmp.setName("Gyroscope Y");
                tmp.setUnits("rad/s");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);

                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(4);
                tmp.setName("Gyroscope Z");
                tmp.setUnits("rad/s");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);
                continue;
            }

            if (mSensorList.get(i).getType() == Sensor.TYPE_LIGHT) {
            tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(5);
                tmp.setName("Light");
                tmp.setUnits("lx");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);
                continue;
            }

            if (mSensorList.get(i).getType() == Sensor.TYPE_PRESSURE) {
            tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(6);
                tmp.setUnits("hPa");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);
                continue;
            }

            if (mSensorList.get(i).getType() == Sensor.TYPE_TEMPERATURE) {
            tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(7);
                tmp.setName("Temperature");
                tmp.setUnits("°");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);
                continue;
            }
            if (mSensorList.get(i).getType() == Sensor.TYPE_PROXIMITY) {
            tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(8);
                tmp.setName("Proximity");
                tmp.setUnits("m");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);
                continue;
            }

            if (mSensorList.get(i).getType() == Sensor.TYPE_GRAVITY) {
                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(9);
                tmp.setName("Gravity X");
                tmp.setUnits("m/s^2");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);

                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(9);
                tmp.setName("Gravity Y");
                tmp.setUnits("m/s^2");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);

                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(9);
                tmp.setName("Gravity Z");
                tmp.setUnits("m/s^2");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);
                continue;
            }

            if (mSensorList.get(i).getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(10);
                tmp.setName("Linear Acceleration X");
                tmp.setUnits("m/s^2");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);

                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(10);
                tmp.setName("Linear Acceleration Y");
                tmp.setUnits("m/s^2");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);

                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(10);
                tmp.setName("Linear Acceleration Z");
                tmp.setUnits("m/s^2");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);
                continue;
            }

            if (mSensorList.get(i).getType() == Sensor.TYPE_ROTATION_VECTOR) {

                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(11);
                tmp.setName("Rotation Vector X");
                tmp.setUnits("");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);

                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(11);
                tmp.setName("Rotation Vector Y");
                tmp.setUnits("");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);

                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(11);
                tmp.setName("Rotation Vector Z");
                tmp.setUnits("");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);

                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(11);
                tmp.setName("Rotation Vector (cos(θ/2))");
                tmp.setUnits("");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);
                continue;
            }
            if (mSensorList.get(i).getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(12);
                tmp.setName("Relative Humidity");
                tmp.setUnits("");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);
                continue;
            }

            if (mSensorList.get(i).getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(13);
                tmp.setName("Ambient Temperature");
                tmp.setUnits("°");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);
                continue;
            }


            if (mSensorList.get(i).getType() == Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED) {
                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(14);
                tmp.setName("Magnetic Field Uncalibrated X");
                tmp.setUnits("μT");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);

                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(14);
                tmp.setName("Magnetic Field Uncalibrated Y");
                tmp.setUnits("μT");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);

                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(14);
                tmp.setName("Magnetic Field Uncalibrated Z");
                tmp.setUnits("μT");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);
                continue;
            }

            if (mSensorList.get(i).getType() == Sensor.TYPE_GAME_ROTATION_VECTOR) {
                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(15);
                tmp.setName("Game Rotation Vector X");
                tmp.setUnits("");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);

                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(15);
                tmp.setName("Game Rotation Vector Y");
                tmp.setUnits("");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);

                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(15);
                tmp.setName("Game Rotation Vector Z");
                tmp.setUnits("");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);
                continue;
            }

            if (mSensorList.get(i).getType() == Sensor.TYPE_GYROSCOPE_UNCALIBRATED) {

                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(16);
                tmp.setName("Gyroscope Uncalibrated No Drift X");
                tmp.setUnits("rad/s");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);

                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(16);
                tmp.setName("Gyroscope Uncalibrated No Drift Y");
                tmp.setUnits("rad/s");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);

                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(16);
                tmp.setName("Gyroscope Uncalibrated No Drift Z");
                tmp.setUnits("rad/s");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);

                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(16);
                tmp.setName("Gyroscope Uncalibrated Estimated Drift X");
                tmp.setUnits("rad/s");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);

                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(16);
                tmp.setName("Gyroscope Uncalibrated Estimated Drift Y");
                tmp.setUnits("rad/s");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);

                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(16);
                tmp.setName("Gyroscope Uncalibrated Estimated Drift Z");
                tmp.setUnits("rad/s");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);
                continue;
            }

            if (mSensorList.get(i).getType() == Sensor.TYPE_SIGNIFICANT_MOTION) {
                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(17);
                tmp.setName("Significant Motion");
                tmp.setUnits("");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);
                continue;
            }


            if (mSensorList.get(i).getType() == Sensor.TYPE_STEP_DETECTOR) {
            tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(18);
                tmp.setName("Step Detector");
                tmp.setUnits("");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);
                continue;
            }

            if (mSensorList.get(i).getType() == Sensor.TYPE_STEP_COUNTER) {
                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(19);
                tmp.setName("Step Counter");
                tmp.setUnits("");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);
                continue;
            }

            if (mSensorList.get(i).getType() == Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR) {
                tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(20);
                tmp.setName("Geomagnetic Roation Vector");
                tmp.setUnits("");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);
                continue;
            }

            if (mSensorList.get(i).getType() == Sensor.TYPE_HEART_RATE) {
            tmp.setUUID(UUID.randomUUID().toString());
                tmp.setType(21);
                tmp.setName("Heart Rate");
                tmp.setUnits("");
                tmp.setVendor(mSensorList.get(i).getVendor());
                tmp.setDataRate(0);
                tmp.setRange(mSensorList.get(i).getMaximumRange());
                tmp.setResolution(mSensorList.get(i).getResolution());
                tmp.setDelay(mSensorList.get(i).getMinDelay());
                tmp.setPower(mSensorList.get(i).getPower());
                SensorLab.getInstance(context).addSensor(tmp);
                continue;
            }
        }
    }
}

