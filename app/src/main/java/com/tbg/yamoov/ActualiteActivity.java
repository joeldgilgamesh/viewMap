package com.tbg.yamoov;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tbg.yamoov.adapter.ActualiteAdapter;
import com.tbg.yamoov.adapter.ActualitesAdaptater;
import com.tbg.yamoov.adapter.CardsAdapter;
import com.tbg.yamoov.model.Actualite;
import com.tbg.yamoov.model.CardModel;

import java.util.ArrayList;
import java.util.List;

public class ActualiteActivity extends AppCompatActivity {

    //private TextView mHeaderView;
    private ListView mActualiteListView;

    //Firebase
    private FirebaseFirestore db;

    //Adapter
    private ActualitesAdaptater mActualitesAdaptater;
    private ArrayList<Actualite> mActualiteList;
    ActualitesAdaptater mActualitesAdaptat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualite);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mActualiteList = new ArrayList<Actualite>();
        mActualitesAdaptat  = new ActualitesAdaptater(ActualiteActivity.this,mActualiteList);


        FirebaseFirestore.getInstance().collection("Actualites").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Actualite> mMissionsList = new ArrayList<>();
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        Actualite miss = document.toObject(Actualite.class);
                        mActualiteList.add(miss);
                    }
                    ListView mMissionsListView = (ListView) findViewById(R.id.list_cards);
                    mMissionsListView.setAdapter(mActualitesAdaptat);
                } else {
                    Log.d("MissionActivity", "Error getting documents: ", task.getException());
                }
            }
        });


        //add the whole Arraylist of MIssions to the adapter
        //mActualitesAdaptater.addAll(mActualiteListView);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem menuItem = menu.findItem(R.id.search_view);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mActualitesAdaptat.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.search_view){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/


}
