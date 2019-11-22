package com.kh_sof_dev.admin.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
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
import android.support.design.widget.TabLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kh_sof_dev.admin.Adapters.Products_adapter;
import com.kh_sof_dev.admin.Clasess.Product;
import com.kh_sof_dev.admin.Clasess.Request;
import com.kh_sof_dev.admin.Clasess.users;
import com.kh_sof_dev.admin.Fragments.cancel_request;
import com.kh_sof_dev.admin.Fragments.compte_request;
import com.kh_sof_dev.admin.Fragments.current_request;
import com.kh_sof_dev.admin.Fragments.waite_request;
import com.kh_sof_dev.admin.R;

import org.json.JSONException;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Odrer_activity extends AppCompatActivity {
    public static String userID;
    public static users mUser;
    private TextView name,phone,cod,address,wallet,pointDay;
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
    private  String mpermissions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odrer_activity);

        Button addPointement=findViewById(R.id.pointment);
        if (!users.getpermissions(this,"Point")){
            addPointement.setVisibility(View.GONE);
        }
        addPointement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pointDay_popup();
            }
        });
ImageView back=findViewById(R.id.back_btn);
back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
     startActivity(new Intent(Odrer_activity.this,Users_activity.class));
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
if (!users.getpermissions(this,"checkout")){
    checkout.setVisibility(View.GONE);
}

final Button add_order=findViewById(R.id.add_order);
add_order.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        add_orderFun();
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
        pointDay=findViewById(R.id.poinDay);

        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            mUser=new users();
            name.setText(bundle.getString("name"));
            userID=bundle.getString("id");
            cod.setText(bundle.getString("nb"));
            phone.setText(bundle.getString("phone"));
            address.setText(bundle.getString("address"));
            pointDay.setText(bundle.getString("pointDay"));

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
        if (!users.getpermissions(this,"block")){
            delete.setVisibility(View.GONE);
        }
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
                        DatabaseReference reference=database.getReference();
                        reference.child("Users").child(userID).child("status").setValue(0);
                        reference.child("Requests").child("Waite").child(userID).removeValue();
                        reference.child("Requests").child("Current").child(userID).removeValue();
                        reference.child("Requests").child("Complete").child(userID).removeValue();
                        reference.child("Requests").child("Cancel").child(userID).removeValue();

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
        tabLayout.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"));

        tabLayout.setupWithViewPager(mViewPager);

    }

    private void pointDay_popup() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("تحديد يوم الزيارة");
        final String[] days = getResources().getStringArray(R.array.pointDay);
        b.setItems(days, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int potions) {

                dialog.dismiss();
               FirebaseDatabase database=FirebaseDatabase.getInstance();
               DatabaseReference reference=database.getReference("Users");
               reference.child(userID).child("pointDay").setValue(days[potions]);
               pointDay.setText(days[potions]);
            }

        });

        b.show();
    }

    private void add_orderFun() {
        final BottomSheetDialog dialog=new BottomSheetDialog(this);
        dialog.setContentView(R.layout.popup_prod_list);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                dialog.findViewById(R.id.progress).setVisibility(View.GONE);
               dialog. findViewById(R.id.noItem).setVisibility(View.VISIBLE);

            }
        }, 5000);

        RecyclerView RV=dialog.findViewById(R.id.RV);

        productList=new ArrayList<>();
        adapter=new Products_adapter(this, productList, new Products_adapter.onEditeListenner() {
            @Override
            public void Onselected(Product product) {
                bay_popup(product);
            }
        });
        adapter.bay=true;
        RV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        RV.setAdapter(adapter);
        
        fesh_data(dialog);
        dialog.show();
        ImageView close=dialog.findViewById(R.id.close_btn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    List<Product> productList;
    Products_adapter adapter;
    private void fesh_data(final BottomSheetDialog dialog) {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference();
        reference.child("Products").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (productList.size()==0){
                   dialog. findViewById(R.id.progress).setVisibility(View.GONE);
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
    private void bay_popup(final Product product) {
        final BottomSheetDialog dialog=new BottomSheetDialog(this);
        dialog.setContentView(R.layout.popup_bay);
        final EditText count=dialog.findViewById(R.id.order_count);
        final EditText talif=dialog.findViewById(R.id.order_talif);
        Button cancel=dialog.findViewById(R.id.cancel);
        Button save=dialog.findViewById(R.id.bay);
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {





                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd  HH:mm");
                Date date = new Date();

                Double cnt=0.0,tlf=0.0;

                try {
                    cnt=Double.parseDouble(count.getText().toString());
                }catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    tlf= Double.parseDouble(talif.getText().toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (count.getText().toString().isEmpty()  || cnt==0.0 ){
                    count.setError(count.getHint());
                    return;
                }
                if (talif.getText().toString().isEmpty() ){
                    talif.setError(talif.getHint());
                    return;
                }

                Double priceTOT=product.getPrice()*cnt
                        -
                       product.getPrice()*tlf;

                Request request=new Request(dateFormat.format(date)
                        ,product.getName()+"( "+product.getWeight()+" KG )",
                        product.getWeight()
                        ,priceTOT,cnt,tlf);
                request.setProd_id(product.getId());

                create_request(request);




                dialog.dismiss();
            }
        });
    }

    private void create_request(final Request request) {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference_nb=database.getReference("App_number").child("requests_nb");
        reference_nb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    int nb=dataSnapshot.getValue(int.class);
                    add_newrequest(nb,request);
                }else {
                    add_newrequest(0,request);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void add_newrequest(int i, Request request) {
        final DecimalFormat decimalFormat = new DecimalFormat("000000");
        request.setNb(decimalFormat.format(i+1));

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference("Requests").child("Waite");
        reference.child(mUser.getId()).push().setValue(request);


        DatabaseReference reference_nb=database.getReference("App_number").child("requests_nb");
        reference_nb.setValue(i+1);

        Toast.makeText(this,this.getString(R.string.ur_req_succ),Toast.LENGTH_LONG).show();
        add_request();

    }
    private void add_request(){
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference reference=database.getReference("Users").child(userID);
        long millis = System.currentTimeMillis();
        reference.child("time_modify").setValue(millis);
        reference.child("request_wail_nb").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    int nb=dataSnapshot.getValue(int.class)+1;
                    reference.child("request_wail_nb").setValue(nb);
                }else {
                    reference.child("request_wail_nb").setValue(1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
