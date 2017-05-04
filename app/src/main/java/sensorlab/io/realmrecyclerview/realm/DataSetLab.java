package sensorlab.io.realmrecyclerview.realm;


import android.content.Context;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class DataSetLab {
    private volatile static DataSetLab singleton;
    private Context mContext;
    private Realm realm;
    private RealmConfiguration realmConfiguration;

    // Convert Realm Results to ArrayList
    // ArrayList<People> list = new ArrayList<(mRealm.where(People.class).findAll());

    public static DataSetLab getInstance(Context context){
        if(singleton == null){
            synchronized (DataSetLab.class){
                if(singleton == null){
                    singleton = new DataSetLab(context);
                }
            }
        }
        return singleton;
    }

    private DataSetLab(Context context){
        mContext = context;
        Realm.setDefaultConfiguration(this.getRealmConfig());

        if (realm == null) {
            realmConfiguration = getRealmConfig();
        }

        realm = Realm.getDefaultInstance();

    }

    public RealmResults<DataSet> getDataSets(){
        final RealmResults<DataSet> results = realm.where(DataSet.class).findAll();
        return results;
    }

    public void addDataSet(final DataSet dataSet){

        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                realm.copyToRealmOrUpdate(dataSet);
            }
        });
    }

    public void removeDataSet(final String id){

        final RealmResults<DataSet> results = realm.where(DataSet.class).equalTo("UUID", id).findAll();

        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                results.deleteAllFromRealm();
            }
        });
    }

    public DataSet getDataSet(final String id){
        final DataSet dataSet = realm.where(DataSet.class).equalTo("UUID", id).findFirst();
        if (dataSet != null) {
            return dataSet;
        }
        else return null;
    }

    public void updateDataSet(final DataSet dataSet){

        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                realm.copyToRealmOrUpdate(dataSet);
            }
        });
    }

    public void addDataPoint(final DataSet dataSet, final DataPoint dataPoint ){

        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                dataSet.dataPoints.add(dataPoint);
            }
        });
    }

    public void removeDataPoint(final DataSet dataSet, final DataPoint dataPoint ){

        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                dataSet.dataPoints.remove(dataPoint);
            }
        });
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
