package com.kh_sof_dev.admin.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kh_sof_dev.admin.Adapters.Products_adapter;
import com.kh_sof_dev.admin.Clasess.Admin;
import com.kh_sof_dev.admin.Clasess.Product;
import com.kh_sof_dev.admin.Clasess.production;
import com.kh_sof_dev.admin.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
private Button save_btn,cancel_btn,my_users,logout,Allorder;
private EditText prod_name,prod_wieght,price;
private FirebaseDatabase database;
private DatabaseReference reference;
private Products_adapter adapter;
private List<Product> productList;
private  String id;
private RecyclerView recyclerView;
private TextView name,email;
private String mPermissions="";
private ConstraintLayout add_cl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database=FirebaseDatabase.getInstance();
        reference=database.getReference();
admin_info();
take_FCMtoken();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                findViewById(R.id.progress).setVisibility(View.GONE);
                findViewById(R.id.noItem).setVisibility(View.VISIBLE);

            }
        }, 5000);

productList=new ArrayList<>();
add_cl=findViewById(R.id.add_cl);
GetPermissions();

my_users=findViewById(R.id.Myusers);
        logout=findViewById(R.id.logout);
        my_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Users_activity.class));
            }
        });
        Allorder=findViewById(R.id.Allorder);
        Allorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AllOdrer_activity.class));
            }
        });
        ImageView list_compt=findViewById(R.id.list_compt);
        list_compt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Admins_activity.class));
            }
        });
logout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        SharedPreferences sp=getSharedPreferences("user_info", MODE_PRIVATE);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.putString("name", "");
        Ed.putString("email", "");
        Ed.commit();
        finish();
    }
});
findViewById(R.id.edit_compt).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        edit_pop();
    }
});
adapter=new Products_adapter(this ,productList, new Products_adapter.onEditeListenner() {
    @Override
    public void Onselected(final Product product) {
      prod_name.setText(product.getName());
      prod_wieght.setText(product.getWeight().toString());
      price.setText(product.getPrice().toString());
      id=product.getId();
      Log.d("prod_id",product.getId());
      save_btn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              edite_fun(product);
          }
      });

    }
});
recyclerView=(RecyclerView) findViewById(R.id.List_prod);
recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true));
recyclerView.setAdapter(adapter);
fesh_data();
        save_btn=findViewById(R.id.save);
        cancel_btn=findViewById(R.id.cancel);

        prod_name=findViewById(R.id.prod_name);
        prod_wieght=findViewById(R.id.prod_weight);
        price=findViewById(R.id.price);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_fun();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleare_fun();
            }
        });
    }

    private void GetPermissions() {
        if (!mPermissions.contains("MPL")){
            add_cl.setVisibility(View.GONE);
        }
        if (!mPermissions.contains("MAL")){
            findViewById(R.id.list_compt).setVisibility(View.GONE);
        }
    }

    private void edit_pop() {
        final BottomSheetDialog dialog=new BottomSheetDialog(MainActivity.this);
        dialog.setContentView(R.layout.popup_edite_compt);
        final EditText email=dialog.findViewById(R.id.email);
        final EditText name=dialog.findViewById(R.id.user_name);
        final EditText pw=dialog.findViewById(R.id.password);
        final EditText repw=dialog.findViewById(R.id.repassword);

        final SharedPreferences sp=getSharedPreferences("user_info", MODE_PRIVATE);
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
                DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Admins");
                reference.child(userID).child("name").setValue(admin.getName());
                reference.child(userID).child("email").setValue(admin.getEmail());
                reference.child(userID).child("password").setValue(admin.getPassword());

                SharedPreferences.Editor Ed=sp.edit();
                Ed.putString("name",admin.getName());
                Ed.putString("email",admin.getEmail());
                Ed.putString("password",admin.getPassword());
                Ed.commit();
                admin_info();

                dialog.dismiss();

            }
        });
        dialog.show();
    }

    private void take_FCMtoken() {
        String token= FirebaseInstanceId.getInstance().getToken();
        reference.child("Notification").child("Token").setValue(token);
    }

    private  String userID;
    private void admin_info() {

    name=findViewById(R.id.user_name);
    email=findViewById(R.id.email);

        SharedPreferences sp=getSharedPreferences("user_info", MODE_PRIVATE);
        name.setText(sp.getString("name",""));
        email.setText(sp.getString("email",""));
        mPermissions=sp.getString("permissions","");
        userID=sp.getString("userID","");
    }

    private void fesh_data() {
        reference.child("Products").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (productList.size()==0){
                    findViewById(R.id.progress).setVisibility(View.GONE);
                }
                Product product=dataSnapshot.getValue(Product.class);
                product.setId(dataSnapshot.getKey());
                Log.d("prod_id 1",dataSnapshot.getKey());
                productList.add(product);
                Log.d("product :",product.getName()+"  "+product.getId());
                adapter.notifyDataSetChanged();
//                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

//
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Product product=dataSnapshot.getValue(Product.class);
                product.setId(dataSnapshot.getKey());
                productList.remove(product);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void edite_fun(Product product_) {

        String p_name=prod_name.getText().toString();
        String p_weight=prod_wieght.getText().toString();
        String p_price=price.getText().toString();

        if (p_name.isEmpty()){
            prod_name.setError(p_name);
            return;
        }
        if (p_weight.isEmpty()){
            prod_wieght.setError(p_weight);
            return;
        }
        if (p_price.isEmpty()){
            price.setError(p_price);
            return;
        }
        productList.remove(product_);
        Product product=new Product(p_name,Double.parseDouble(p_weight),Double.parseDouble(p_price));
        reference.child("Products").child(id).setValue(product);
        Toast.makeText(getApplicationContext(),"تم تعديل المنتج بنجاح ",Toast.LENGTH_LONG).show();
        cleare_fun();
    }

    private void save_fun() {

        String p_name=prod_name.getText().toString();
        String p_weight=prod_wieght.getText().toString();
        String p_price=price.getText().toString();

        if (p_name.isEmpty()){
            prod_name.setError(prod_name.getHint());
            return;
        }
        if (p_weight.isEmpty()){
            prod_wieght.setError(prod_wieght.getHint());
            return;
        }
        if (p_price.isEmpty()){
            price.setError(price.getHint());
            return;
        }
        Product product=new Product(p_name,Double.parseDouble(p_weight),Double.parseDouble(p_price));
        String key=reference.child("Products").push().getKey();
reference.child("Products").child(key).setValue(product);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        production prod=new production(0.0,0.0);
        reference.child("Products").child(key).child("Productions").child(dateFormat.format(date)).setValue(prod);

        Toast.makeText(getApplicationContext(),"تم حفظ المنتج بنجاح ",Toast.LENGTH_LONG).show();
        cleare_fun();

    }

    private void cleare_fun() {
        prod_wieght.setText("");
        prod_name.setText("");
        price.setText("");
    }
}
