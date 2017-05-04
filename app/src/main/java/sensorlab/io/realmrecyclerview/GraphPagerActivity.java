package sensorlab.io.realmrecyclerview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import io.realm.RealmResults;
import sensorlab.io.realmrecyclerview.realm.Graph;
import sensorlab.io.realmrecyclerview.realm.GraphLab;


public class GraphPagerActivity extends AppCompatActivity implements GraphFragment.Callbacks {
    private ViewPager mViewPager;
    private RealmResults<Graph> mGraphs;

    private static final String EXTRA_GRAPH_ID = "graph_id";

    public static Intent newIntent(Context packageContext, String graphId) {
        Intent intent = new Intent(packageContext, GraphPagerActivity.class);
        intent.putExtra(EXTRA_GRAPH_ID, graphId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_pager);

        String UUID = (String) getIntent().getSerializableExtra(EXTRA_GRAPH_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_graph_pager_view_pager);

        mGraphs = GraphLab.getInstance(getApplicationContext()).getGraphs();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Graph graph = mGraphs.get(position);
                return GraphFragment.newInstance(graph.getUUID());
            }

            @Override
            public int getCount() {
                return mGraphs.size();
            }
        });

        for(int i = 0; i<mGraphs.size(); i++){
            if(mGraphs.get(i).getUUID().equals(UUID)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
    @Override
    public void onGraphUpdated(Graph graph){
        //TODO

    }
    @Override
    public void onGraphDeleted(Graph graph){
        //TODO
    }
}
