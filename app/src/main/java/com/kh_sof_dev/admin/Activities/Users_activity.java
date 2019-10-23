package com.kh_sof_dev.admin.Activities;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kh_sof_dev.admin.Adapters.Users_adapter;
import com.kh_sof_dev.admin.Clasess.users;
import com.kh_sof_dev.admin.R;

import java.util.ArrayList;
import java.util.List;

public class Users_activity extends AppCompatActivity {
private FirebaseDatabase database;
DatabaseReference reference;
private RecyclerView Rv;
private List<users> usersList;
private Users_adapter adapter;
private int usersNB=0;
TextView nb_users;
private Spinner pointDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_activity);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                findViewById(R.id.progress).setVisibility(View.GONE);
                findViewById(R.id.noItem).setVisibility(View.VISIBLE);

            }
        }, 5000);
        pointDay=findViewById(R.id.pointDay);
        pointDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] point=getResources().getStringArray(R.array.pointDay);
                filterBYDAy(point[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
       nb_users= (TextView)findViewById(R.id.users_nb);
        ImageView back=findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Users");
        usersList=new ArrayList<>();
        Rv=findViewById(R.id.users_list);
        Rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapter=new Users_adapter(this,usersList,null);
        Rv.setAdapter(adapter);
        fetch_data();

    }

    private void filterBYDAy(String s) {

        if (s.equals("الكل")){
            adapter=new Users_adapter(this,usersList,null);
            Rv.setAdapter(adapter);
            nb_users.setText(usersList.size() +" عدد العملاء  ");

            return;
        }
       List<users> usersList_=new ArrayList<>();

        for (users user : usersList) {
            if (user.getPointDay().equals(s)){
                usersList_.add(user);
            }
        }
      adapter=new Users_adapter(this,usersList_,null);
        Rv.setAdapter(adapter);
        nb_users.setText(usersList_.size() +" عدد العملاء  ");
    }

    private void fetch_data() {
        reference.orderByChild("time_modify");
    reference.addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            if (usersList.size()==0){
                findViewById(R.id.progress).setVisibility(View.GONE);
            }
            users user=dataSnapshot.getValue(users.class);
            user.setId(dataSnapshot.getKey());
            if (user.getStatus()!=0){
                usersList.add(user);
                adapter.notifyDataSetChanged();
                usersNB++;
                nb_users.setText(usersNB +" عدد العملاء  ");
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
