package com.kh_sof_dev.admin.Fragments.AllOrders;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kh_sof_dev.admin.Activities.AllOdrer_activity;
import com.kh_sof_dev.admin.Activities.Odrer_activity;
import com.kh_sof_dev.admin.Adapters.Requestes_adapter;
import com.kh_sof_dev.admin.Clasess.Const;
import com.kh_sof_dev.admin.Clasess.Request;
import com.kh_sof_dev.admin.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class waite_Allrequest extends Fragment {


    public waite_Allrequest() {
        // Required empty public constructor
    }

RecyclerView list;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private List<Request> requestList;
    private Requestes_adapter adapter;
    private ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       final View view= inflater.inflate(R.layout.fragment_compte_request, container, false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (requestList.size()==0){
                    view.findViewById(R.id.progress).setVisibility(View.GONE);
                    view.findViewById(R.id.noItem).setVisibility(View.VISIBLE);
                }

            }
        }, 5000);
        database=FirebaseDatabase.getInstance();
        progressBar=view.findViewById(R.id.progress);
        reference=database.getReference("Requests").child(Const.wait);
        list=view.findViewById(R.id.request_list);
        requestList=new ArrayList<>();
        adapter=new Requestes_adapter(getContext(),requestList,null);
        list.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,true));
        list.setAdapter(adapter);
        fetch_data();
        return  view;
    }


    @Override
    public void onStart() {
        super.onStart();
        AllOdrer_activity.onfilterListenner=new AllOdrer_activity.Onfilter() {
            @Override
            public void onfilterDate(String date) {
                FilterByDate(date);
                Toast.makeText(getContext(),date,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onfilterName(String name) {
                FilterByName(name);
                Toast.makeText(getContext(),name,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onfilterWeight(String weight) {
//                FilterByWeight(Double.parseDouble(weight));
                FilterByName(weight);

                Toast.makeText(getContext(),weight,Toast.LENGTH_LONG).show();

            }
        };
    }
    List<Request>  requestListFilter=new ArrayList<>();

    private void FilterByDate(String date) {
        if (requestListFilter.size()==0){
            requestListFilter.addAll(requestList);
        }
        List<Request>  RL_Filter=new ArrayList<>();
        for (Request rqs:requestListFilter
        ) {
            if (rqs.getDate().contains(date)){
                RL_Filter.add(rqs);
            }

        }
        requestListFilter.clear();
        requestListFilter.addAll(RL_Filter);
        adapter=new Requestes_adapter(getContext(),requestListFilter,null);
        list.setAdapter(adapter);
    }
    private void FilterByName(String name) {
        if (requestListFilter.size()==0){
            requestListFilter.addAll(requestList);
        }
        List<Request>  RL_Filter=new ArrayList<>();
        for (Request rqs:requestListFilter
        ) {
            if (rqs.getProduct().contains(name)){
                RL_Filter.add(rqs);
            }

        }
        requestListFilter.clear();
        requestListFilter.addAll(RL_Filter);
        adapter=new Requestes_adapter(getContext(),requestListFilter,null);
        list.setAdapter(adapter);
    }

    private void fetch_data() {
    reference.addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            for(DataSnapshot ds : dataSnapshot.getChildren()) {

                if (requestList.size()==0){
                    progressBar.setVisibility(View.GONE);
                }
                Request request=ds.getValue(Request.class);
                request.setType(1);
                request.setId(dataSnapshot.getKey());
                requestList.add(request);
                adapter.notifyDataSetChanged();


            }


        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    }

}
