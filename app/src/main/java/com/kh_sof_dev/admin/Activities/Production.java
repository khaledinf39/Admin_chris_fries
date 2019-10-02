package com.kh_sof_dev.admin.Activities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kh_sof_dev.admin.Adapters.Production_adapter;
import com.kh_sof_dev.admin.Clasess.Product;
import com.kh_sof_dev.admin.Clasess.production;
import com.kh_sof_dev.admin.R;

import java.util.ArrayList;
import java.util.List;

public class Production extends AppCompatActivity {

    private ImageView back;
    private RecyclerView RV;
    private Production_adapter adapter;
    private List<production> productionList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production);
        back=findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RV=findViewById(R.id.RV);
        RV.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.
                VERTICAL,true));
       productionList=new ArrayList<>();
       adapter=new Production_adapter(getApplicationContext(), productionList, null);
       RV.setAdapter(adapter);
       Bundle bundle=getIntent().getExtras();
       if (bundle!=null) {
           Fetch_data(bundle.getString("prod"));
       }


    }

    private void Fetch_data(String prod) {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference("Products").child(prod);
        reference.child("Productions").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                production production_=dataSnapshot.getValue(production.class);
                productionList.add(production_);
                adapter.notifyDataSetChanged();
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
