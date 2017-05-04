package sensorlab.io.realmrecyclerview.realm;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

import static java.lang.Thread.sleep;
public class GraphLab {
    private volatile static GraphLab singleton;
    private Context mContext;
    private Realm realm;
    private RealmConfiguration realmConfiguration;

    public static GraphLab getInstance(Context context){
        if(singleton == null){
            synchronized (GraphLab.class){
                if(singleton == null){
                    singleton = new GraphLab(context);
                }
            }
        }
        return singleton;
    }



    private GraphLab(Context context){
        mContext = context;
        Realm.setDefaultConfiguration(this.getRealmConfig());
        realm = Realm.getDefaultInstance();
        realmConfiguration = getRealmConfig();
        //deleteAllGraphs();

    }

    public RealmResults<Graph> getGraphs(){

        RealmResults<Graph> graphs = realm
                .where(Graph.class)
                .findAll();
        return graphs;
    }

    public void addGraph(final Graph graph){

        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                realm.copyToRealm(graph);
            }
        });

                /*
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealm(graph);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
            }
        });*/
    }

    public void removeGraph(final String id) {

                final RealmResults<Graph> results = realm.where(Graph.class).equalTo("UUID", id).findAll();
                if (results != null) {

                    realm.executeTransaction(new Realm.Transaction(){
                        @Override
                        public void execute(Realm realm){
                            results.deleteAllFromRealm();
                        }
                    });
                }

    }

    public Graph getGraph(final String id){
        final Graph graph = realm.where(Graph.class).equalTo("UUID", id).findFirst();
        if (graph != null) {
            return graph;

        }
        else return null;


    }

    public void updateGraph(final Graph graph){

        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                realm.copyToRealmOrUpdate(graph);
            }
        });

    }

    public void addDataSet(final Graph graph, final DataSet dataSet){
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                graph.getDataSets().add(dataSet);
            }
        });
    }

    public void removeDataSet(final Graph graph, final DataSet dataSet){
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                graph.getDataSets().remove(dataSet);
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

    public void bulkAdd() {
        realm.executeTransaction(new Realm.Transaction(){
            Graph mGraph;
            @Override
            public void execute(Realm realm){
                for(int i=0; i<4;i++){
                    mGraph = realm.createObject(Graph.class);
                    mGraph.setUUID(UUID.randomUUID().toString());
                    mGraph.setTitle("Bulk GraphLab Instance: " + i + " Added");
                    mGraph.setDate(new Date());
                    realm.copyToRealm(mGraph);
                }
            }
        });
    }

    public void loadMoreQuotes() {
        // Add some delay to the refresh/remove action.
        try {
            sleep(1000);
        } catch (InterruptedException ignored) {}

        realm.executeTransaction(new Realm.Transaction(){
            Graph mGraph;
            @Override
            public void execute(Realm realm){
                for(int i=0; i<40;i++){
                    mGraph = realm.createObject(Graph.class);
                    mGraph.setUUID(UUID.randomUUID().toString());
                    mGraph.setTitle("Bulk GraphLab Instance: " + i + " Added");
                    mGraph.setDate(new Date());
                    realm.copyToRealm(mGraph);
                }
            }
        });
    }

    public void deleteAllGraphs() {
        final RealmResults<Graph> results = realm.where(Graph.class).findAll();
        if (results != null) {
            realm.executeTransaction(new Realm.Transaction(){
                @Override
                public void execute(Realm realm){
                    results.deleteAllFromRealm();
                }
            });
        }
    }

    public void bulkAddTwo() {
        realm.executeTransaction(new Realm.Transaction() {
            Graph mGraph;
            @Override
            public void execute(Realm realm) {
                mGraph = realm.createObject(Graph.class);
                mGraph.setUUID(UUID.randomUUID().toString());
                mGraph.setTitle("Bulk GraphLab Instance: BulkTwo" + "1" + " Added");
                mGraph.setDate(new Date());
                realm.copyToRealm(mGraph);
                mGraph = realm.createObject(Graph.class);
                mGraph.setUUID(UUID.randomUUID().toString());
                mGraph.setTitle("Bulk GraphLab Instance BulkTwo: " + "2" + " Added");
                mGraph.setDate(new Date());
                realm.copyToRealm(mGraph);
            }
        });
    }


}
