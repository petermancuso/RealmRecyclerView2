package sensorlab.io.realmrecyclerview;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import sensorlab.io.realmrecyclerview.realm.Graph;
import sensorlab.io.realmrecyclerview.realm.RealmBaseActivity;


public class GraphListActivity extends RealmBaseActivity implements GraphListFragment.Callbacks, GraphFragment.Callbacks {
    private static final int REQUEST_GRAPH = 1;
    private static String ARG_GRAPH_ID = "graph_id";
    private int graphChanged;
    private Graph mGraph;


    @Override
    protected Fragment createFragment(){
        return new GraphListFragment();
    }
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }
    @Override
    public void onGraphSelected(Graph graph) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            mGraph = graph;
            Intent intent = GraphPagerActivity.newIntent(this, graph.getUUID());
            startActivityForResult(intent, REQUEST_GRAPH);
        } else {
            Fragment newDetail = GraphFragment.newInstance(graph.getUUID());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();

            mGraph = graph;

        }
    }

    @Override
    public void onGraphUpdated(Graph graph){
        GraphListFragment listFragment = (GraphListFragment)
                getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
                listFragment.updateUI(0);
    }
    @Override
    public void onGraphDeleted(Graph graph){
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.detail_fragment_container);
        if(fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            Toast.makeText(getApplicationContext(), graph.getUUID(),
                    Toast.LENGTH_LONG).show();
        }
    }
}
