package com.kh_sof_dev.admin.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.TabLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kh_sof_dev.admin.Clasess.users;
import com.kh_sof_dev.admin.Fragments.cancel_request;
import com.kh_sof_dev.admin.Fragments.compte_request;
import com.kh_sof_dev.admin.Fragments.current_request;
import com.kh_sof_dev.admin.Fragments.waite_request;
import com.kh_sof_dev.admin.R;

public class Odrer_activity extends AppCompatActivity {
    public static String userID;
    public static users mUser;
    private TextView name,phone,cod,address,wallet;
private Button delete;


    private final String[] PAGE_TITLES = new String[]{
            "بإنتظار الموافقة",
            "الحالية",

            "المكتملة",
            "الملغية",



    };

    private final Fragment[] PAGES = new Fragment[]{
           new waite_request(),
            new current_request(),
            new compte_request(),
            new cancel_request()
    };
    public static FragmentManager fragmentManager;
    public static Context context;
private Double lat=0.0,lng=0.0;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odrer_activity);
ImageView back=findViewById(R.id.back_btn);
back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        finish();
    }
});

        final Button checkout=findViewById(R.id.pay);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_outFun();
            }
        });
final Button gotomap=findViewById(R.id.gomap);
gotomap.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        double latitude = lat;
        double longitude = lng;
        String label = "I'm Here!";
        String uriBegin = "geo:" + latitude + "," + longitude;
        String query = latitude + "," + longitude + "(" + label + ")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
        Uri uri = Uri.parse(uriString);
        Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        startActivity(mapIntent);
    }
});
        name=findViewById(R.id.user_name);
        wallet=findViewById(R.id.wallet);
        phone=findViewById(R.id.phone);
        cod=findViewById(R.id.code);
        address=findViewById(R.id.address);

        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            mUser=new users();
            name.setText(bundle.getString("name"));
            userID=bundle.getString("id");
            cod.setText(bundle.getString("nb"));
            phone.setText(bundle.getString("phone"));
            address.setText(bundle.getString("address"));

            mUser.setId(userID);
           mUser.setToken(bundle.getString("token"));
           mUser.setWallet(bundle.getDouble("wallet"));
           wallet.setText(mUser.getWallet().toString());
lat=bundle.getDouble("lat");
lng=bundle.getDouble("lng");
            if (lat==0.0 && lng==0.0){
                gotomap.setEnabled(false);
            }
        }
delete=findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(Odrer_activity.this);
                dialog.setContentView(R.layout.popup_delete);

                Button delet_pop=dialog.findViewById(R.id.delete);
                Button cancel_pop=dialog.findViewById(R.id.cancel);
                delet_pop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase database=FirebaseDatabase.getInstance();
                        DatabaseReference reference=database.getReference("Requests");
                        reference.child("Waite").child(userID).removeValue();
                        reference.child("Current").child(userID).removeValue();
                        reference.child("Complete").child(userID).removeValue();
                        reference.child("Cancel").child(userID).removeValue();

                        Toast.makeText(Odrer_activity.this,"تم الحذف بنجاح ",Toast.LENGTH_LONG).show();

                        dialog.dismiss();
                        Odrer_activity.this.finish();
                    }
                });
                cancel_pop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
///////////////

        fragmentManager = getSupportFragmentManager();
        mViewPager = (ViewPager) findViewById(R.id.viewpage);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
//        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#56ce8b"));
        tabLayout.setSelectedTabIndicatorHeight((int) (4 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#011401"));

        tabLayout.setupWithViewPager(mViewPager);

    }
    private void add_wallet(final Double newPrice){
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference reference=database.getReference("Users").child(mUser.getId());
        reference.child("wallet").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Double wallet_=dataSnapshot.getValue(Double.class)-newPrice;
                    if(wallet_>0) {
                        reference.child("wallet").setValue(wallet_);
                        Toast.makeText(getApplicationContext(),"تم اضافة المبلغ المستلم ",Toast.LENGTH_LONG).show();
                        wallet.setText(wallet_.toString());
                    }else {
                        Toast.makeText(getApplicationContext(),"المبلغ المستلم اكبر من الدين",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"المبلغ المستلم اكبر من الدين",Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void check_outFun() {
        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.popup_checkout);
        Button ok=dialog.findViewById(R.id.checkout_btn);
        Button cancel=dialog.findViewById(R.id.cancel);
        final EditText price=dialog.findViewById(R.id.price);
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (price.getText().toString().isEmpty()){
                    price.setError("المبلغ المستلم");
                    dialog.dismiss();
                    return;
                }
                Double price_=Double.parseDouble(price.getText().toString());
                if (price_!=0  ){
                       add_wallet(price_);
                       dialog.dismiss();
                }else {
                    price.setError("المبلغ المستلم");
                    dialog.dismiss();
                    return;
                }
            }
        });
    }


    public class MyPagerAdapter extends android.support.v4.app.FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {

            return PAGES[position];
        }

        @Override
        public int getCount() {
            return PAGES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return PAGE_TITLES[position];
        }

    }
}
