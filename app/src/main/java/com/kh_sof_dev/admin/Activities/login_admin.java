package com.kh_sof_dev.admin.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kh_sof_dev.admin.Clasess.Admin;
import com.kh_sof_dev.admin.R;

public class login_admin extends AppCompatActivity {
private Button login;
private TextView password,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        ImageView back=findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences sp=getSharedPreferences("user_info", MODE_PRIVATE);
        if (!sp.getString("name","").equals("") ){
            startActivity(new Intent(login_admin.this,MainActivity.class));
            login_admin.this.finish();
        }
        password=findViewById(R.id.password);
        email=findViewById(R.id.email);
        login=findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFun();
            }
        });

    }

    private void loginFun() {

        final String email_=email.getText().toString();
        final String pw=password.getText().toString();
        findViewById(R.id.progress).setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                findViewById(R.id.progress).setVisibility(View.GONE);
                Toast.makeText(login_admin.this,"error",Toast.LENGTH_LONG).show();

            }
        }, 5000);
        if (email_.isEmpty()){
            email.setError(email.getHint());
            return;
        }

        if (pw.isEmpty()){
            password.setError(password.getHint());
            return;
        }

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference("Admins");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    try {
                        Admin admin=dataSnapshot.getValue(Admin.class);

                        if (email_.equals(admin.getEmail()) && pw.equals(admin.getPassword())){
                            save_adminLogin(admin);
                            startActivity(new Intent(login_admin.this,MainActivity.class));
                            login_admin.this.finish();
                        }
                    }catch (Exception e){

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void save_adminLogin(Admin admin) {
        SharedPreferences sp=getSharedPreferences("user_info", MODE_PRIVATE);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.putString("name", String.valueOf(admin.getName()));
        Ed.putString("email", String.valueOf(admin.getEmail()));
        Ed.commit();
    }
}
