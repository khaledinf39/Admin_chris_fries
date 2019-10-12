package com.kh_sof_dev.admin.Activities;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kh_sof_dev.admin.Adapters.admins_adapter;
import com.kh_sof_dev.admin.Clasess.Admin;
import com.kh_sof_dev.admin.Clasess.permissin;
import com.kh_sof_dev.admin.Clasess.users;
import com.kh_sof_dev.admin.R;

import java.util.ArrayList;
import java.util.List;

public class Admins_activity extends AppCompatActivity {

    private RecyclerView RV;
    private FloatingActionButton fab;
    private List<Admin> admins;
    private admins_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admins_activity);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
if (admins.size()==0) {
    findViewById(R.id.progress).setVisibility(View.GONE);
    findViewById(R.id.noItem).setVisibility(View.VISIBLE);
}

            }
        }, 5000);
        RV = findViewById(R.id.RV);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_popup();
            }
        });

        admins = new ArrayList<>();
        adapter = new admins_adapter(this, admins, null);
        RV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        RV.setAdapter(adapter);
        Fetch_allAdmins();
    }

    private void Fetch_allAdmins() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Admins");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Admin admin = dataSnapshot.getValue(Admin.class);
                if (!Admin.getAdmin(Admins_activity.this).getEmail().equals(admin.getEmail())){

                    admin.setUid(dataSnapshot.getKey());
                    for (DataSnapshot ds : dataSnapshot.child("Permissions").getChildren()
                    ) {
                        permissin prm = ds.getValue(permissin.class);
                        admin.getPermissin().add(prm);
                    }
                    admins.add(admin);
                    adapter.notifyDataSetChanged();
                }

                if (admins.size()==0){
                    findViewById(R.id.progress).setVisibility(View.GONE);
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


    private void add_popup() {
        final BottomSheetDialog dialog=new BottomSheetDialog(this);
        dialog.setContentView(R.layout.popup_edite_compt);
        final EditText email=dialog.findViewById(R.id.email);
        final EditText name=dialog.findViewById(R.id.user_name);
        final EditText pw=dialog.findViewById(R.id.password);
        final EditText repw=dialog.findViewById(R.id.repassword);
        LinearLayout permissions_lay=dialog.findViewById(R.id.permissin_lay);
        permissions_lay.setVisibility(View.VISIBLE);

        final CheckBox MAL=dialog.findViewById(R.id.MAL);
        final CheckBox checkout=dialog.findViewById(R.id.checkout);
        final CheckBox MO=dialog.findViewById(R.id.MO);
        final CheckBox block=dialog.findViewById(R.id.block);
        final CheckBox production=dialog.findViewById(R.id.production);
        final CheckBox MPL=dialog.findViewById(R.id.MPL);

        SharedPreferences sp=getSharedPreferences("user_info", MODE_PRIVATE);
        name.setText(sp.getString("name",""));
        email.setText(sp.getString("email",""));


        Button save=dialog.findViewById(R.id.save);
        Button cancel=dialog.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email_=email.getText().toString();
                final String pw_=pw.getText().toString();
                final String repw_=repw.getText().toString();
                final String name_=name.getText().toString();

                if (email_.isEmpty()){
                    email.setError(email.getHint());
                    return;
                }
                if (name_.isEmpty()){
                    name.setError(name.getHint());
                    return;
                }
                if (pw_.isEmpty() || !pw_.equals(repw_)){
                    pw.setError(pw.getHint());
                    return;
                }
                Admin admin=new Admin();
                admin.setEmail(email_);
                admin.setName(name_);
                admin.setPassword(pw_);

                checkBox(admin.getPermissin(),MAL);
                checkBox(admin.getPermissin(),checkout);
                checkBox(admin.getPermissin(),MO);
                checkBox(admin.getPermissin(),block);
                checkBox(admin.getPermissin(),production);
                checkBox(admin.getPermissin(),MPL);

                DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Admins");
                reference.push().setValue(admin);
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    private void checkBox(List<permissin> permissin, CheckBox mal) {
        if (mal.isChecked()){
            permissin.add(new permissin(mal.getText().toString()));
        }
    }

}