package com.tbg.yamoov;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualite);


        //mHeaderView = (TextView) findViewById(R.id.missionHeader);
        //mActualiteListView = (ListView) findViewById(R.id.list_cards);


       // db = FirebaseFirestore.getInstance();
        //Set up the ArrayList
        mActualiteList = new ArrayList<Actualite>();
        //set the Adapter
       // mActualitesAdaptater = new ActualitesAdaptater(this, mActualiteList);

        //mActualiteListView.setAdapter(mActualitesAdaptater);


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
                    ActualitesAdaptater mActualitesAdaptat = new ActualitesAdaptater(ActualiteActivity.this,mActualiteList);
                    mMissionsListView.setAdapter(mActualitesAdaptat);
                } else {
                    Log.d("MissionActivity", "Error getting documents: ", task.getException());
                }
            }
        });


        //add the whole Arraylist of MIssions to the adapter
        //mActualitesAdaptater.addAll(mActualiteListView);
    }



}
