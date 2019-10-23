package com.kh_sof_dev.admin.Activities;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    private TextView total_tot,order_ot,talef_tot,prodction_tot;
    private Double total_tot_=0.0,order_tot_=0.0,talef_tot_=0.0,prodction_tot_=0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production);

        productionList=new ArrayList<>();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

               if (productionList.size()==0){
                   findViewById(R.id.progress).setVisibility(View.GONE);
                   findViewById(R.id.noItem).setVisibility(View.VISIBLE);
               }

            }
        }, 5000);
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

        order_ot=findViewById(R.id.order_tot);
        total_tot=findViewById(R.id.total_tot);
        prodction_tot=findViewById(R.id.production_tot);
        talef_tot=findViewById(R.id.talef_tot);

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
                if (productionList.size()==0){
                    findViewById(R.id.progress).setVisibility(View.GONE);
                }
                production production_=dataSnapshot.getValue(production.class);
                addToTot(production_);
                production_.setDate(dataSnapshot.getKey());
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

    private void addToTot(production p_) {
        total_tot_+= p_.getProduction()- p_.getOrder();
        prodction_tot_+= p_.getProduction();
        talef_tot_+= p_.getTalif();
        order_tot_+= p_.getOrder();


        order_ot.setText(order_tot_.toString());
        talef_tot.setText(talef_tot_.toString());
        prodction_tot.setText(prodction_tot_.toString());
        total_tot.setText(total_tot_.toString());
    }
}
