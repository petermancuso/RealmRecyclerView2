package sensorlab.io.realmrecyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;
import io.realm.Sort;
import sensorlab.io.realmrecyclerview.realm.Graph;
import sensorlab.io.realmrecyclerview.realm.GraphLab;

public class GraphListFragment extends Fragment {

    private static final int REQUEST_GRAPH = 1;
    private static String ARG_GRAPH_ID = "graph_id";

    private RealmRecyclerView mRealmRecyclerView;
    private GraphRecyclerViewAdapter mGraphAdapter;
    private Graph mGraph;
    private Realm realm;
    private Context context;
    private GraphLab graphLab;
    private int graphPosition;
    boolean isBulk = false;
    private RealmResults<Graph> mGraphs;
    private Callbacks mCallbacks;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_GRAPH){
            graphPosition = data.getIntExtra(ARG_GRAPH_ID, 0);
        }
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }
    @Override
    public void onResume(){
        super.onResume();
        mGraphAdapter.notifyDataSetChanged();
    }

    @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflator) {
        super.onCreateOptionsMenu(menu, inflator);
            inflator.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_add_graph) {
            Graph graph = new Graph();
            graph.setUUID(UUID.randomUUID().toString());
            graph.setTitle("Title");
            graph.setDate(new Date());

            graphLab.addGraph(graph);
            mCallbacks.onGraphSelected(graph);
            mGraphs = GraphLab.getInstance(context).getGraphs();
            return true;

        } else if (id == R.id.action_bulk_add_initial) {

            graphLab.bulkAdd();
            return true;

        } else if (id == R.id.action_bulk_add_two) {

            graphLab.bulkAddTwo();
            return true;

        } else if (id == R.id.action_add_footer) {

            mGraphAdapter.addFooter();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //Connects the Adapter to RecyclerView. Creates a RecordAdapter and set it on the RecyclerView.

    //TODO
    //finish this method. Test.
    public void updateUI(int c) {

            //mAdapter.notifyItemChanged(c);*/
            mGraphAdapter.notifyDataSetChanged();


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        context = getContext();
        graphLab =  GraphLab.getInstance(context);
        realm = Realm.getInstance(graphLab.getRealmConfig());

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.activity_main_linear_layout, container, false);
        mRealmRecyclerView = (RealmRecyclerView) view.findViewById(R.id.realm_recycler_view);

        mGraphs = realm
                .where(Graph.class)
                .findAllSorted("title", Sort.ASCENDING);
        mGraphAdapter = new GraphRecyclerViewAdapter(
                getContext(),
                mGraphs,
                true,
                true,
                isBulk ? "date" : null);
        mRealmRecyclerView.setAdapter(mGraphAdapter);
        mGraphs.toArray();
        return view;
    }


    /**
     * Required interface for hosting activities.
     */
    public interface Callbacks {
        void onGraphSelected(Graph graph);
        void onGraphDeleted(Graph graph);
    }

    public class GraphRecyclerViewAdapter extends RealmBasedRecyclerViewAdapter<Graph,
            GraphRecyclerViewAdapter.ViewHolder> {
        private List<Graph> mGraphs;

        public GraphRecyclerViewAdapter(
                Context context,
                RealmResults<Graph> realmResults,
                boolean automaticUpdate,
                boolean animateIdType,
                String animateExtraColumnName) {
            super(context, realmResults, automaticUpdate, animateIdType, animateExtraColumnName);
        }

        public class ViewHolder extends RealmViewHolder {

            public FrameLayout container;

            public TextView graphTextView;

            public TextView footerTextView;

            public ViewHolder(FrameLayout container) {
                super(container);
                this.container = container;
                this.graphTextView = (TextView) container.findViewById(R.id.graph_text_view);
                this.footerTextView = (TextView) container.findViewById(R.id.footer_text_view);
            }
        }

        @Override
        public GraphRecyclerViewAdapter.ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
            View v = inflater.inflate(R.layout.item_view, viewGroup, false);
            return new GraphRecyclerViewAdapter.ViewHolder((FrameLayout) v);
        }

        @Override
        public void onBindRealmViewHolder(ViewHolder viewHolder, int position) {
            final Graph graph = realmResults.get(position);
            viewHolder.graphTextView.setText(graph.getTitle());

            viewHolder.graphTextView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!RealmObject.isValid(graph)) {
                                return;
                            }
                            //graphLab.removeGraph(graphModel.getUUID());

                            mCallbacks.onGraphSelected(graph);


                        }
                    }
            );
            viewHolder.graphTextView.setOnLongClickListener(
                    new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {


                            if (!RealmObject.isValid(graph)) {
                                return false;
                            }

                            mCallbacks.onGraphDeleted(graph);
                            graphLab.removeGraph(graph.getUUID());
                            return true;
                        }
                    }
            );

            viewHolder.graphTextView.setText(graph.getTitle());
        }

        @Override
        public void onBindFooterViewHolder(GraphRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.footerTextView.setText("I'm a footer");
        }

        @Override
        public GraphRecyclerViewAdapter.ViewHolder onCreateFooterViewHolder(ViewGroup viewGroup) {
            View v = inflater.inflate(R.layout.footer_view, viewGroup, false);
            return new GraphRecyclerViewAdapter.ViewHolder((FrameLayout) v);
        }

    }
    @Override
    public void onPause(){
        super.onPause();

    }
}
