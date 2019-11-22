package com.kh_sof_dev.admin.Activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kh_sof_dev.admin.Adapters.Requestes_adapter;
import com.kh_sof_dev.admin.Adapters.SpinAdapterProduct;
import com.kh_sof_dev.admin.Adapters.SpinAdapterUsers;
import com.kh_sof_dev.admin.Clasess.Const;
import com.kh_sof_dev.admin.Clasess.Product;
import com.kh_sof_dev.admin.Clasess.Request;
import com.kh_sof_dev.admin.Clasess.users;
import com.kh_sof_dev.admin.Fragments.AllOrders.cancel_Allrequest;
import com.kh_sof_dev.admin.Fragments.AllOrders.complete_Allrequest;
import com.kh_sof_dev.admin.Fragments.AllOrders.current_Allrequest;
import com.kh_sof_dev.admin.Fragments.AllOrders.waite_Allrequest;
import com.kh_sof_dev.admin.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AllOdrer_activity extends AppCompatActivity {

public interface Onfilter{
    void onfilterDate(String date_in, String date);
    void onfilterName(String name);
    void onfilterUser(String user_id);

    void onfilterPoint(String day);
}
    public static   Onfilter onfilterListenner;
    private final String[] PAGE_TITLES = new String[]{
            "بإنتظار الموافقة",
            "الحالية",

            "المكتملة",
            "الملغية",



    };

    private final Fragment[] PAGES = new Fragment[]{
            new waite_Allrequest(),
            new current_Allrequest(),
            new complete_Allrequest(),
            new cancel_Allrequest()
    };
    public static FragmentManager fragmentManager;
    public static Context context;
    private Button datein,dateto;
    private Spinner nameSP,usersSP,daySp,orderTypeSP;
private Double lat=0.0,lng=0.0;
    private ViewPager mViewPager;
    private SpinAdapterUsers adapterUsers;
    private SpinAdapterProduct adapterProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_odrer_activity);
ImageView back=findViewById(R.id.back_btn);
back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        finish();
    }
});

        datein=findViewById(R.id.indate);
        dateto=findViewById(R.id.todate);
        nameSP=findViewById(R.id.prod_name);
        usersSP=findViewById(R.id.users);
        daySp=findViewById(R.id.days);
        orderTypeSP=findViewById(R.id.order_type);
datein.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        showThePickers(1);
    }
});
        dateto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showThePickers(2);
            }
        });
        FetchData();
Button filter=findViewById(R.id.filter);
filter.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String date_in=datein.getText().toString();
        String date_to=dateto.getText().toString();

        if (!date_in.isEmpty() && !date_to.isEmpty()){
            FilterByAll();
        }


    }
});
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (requestList.size()==0){
                    findViewById(R.id.progress).setVisibility(View.GONE);
                   findViewById(R.id.noItem).setVisibility(View.VISIBLE);
                }

            }
        }, 5000);
        database=FirebaseDatabase.getInstance();
        progressBar=findViewById(R.id.progress);
        list=findViewById(R.id.request_list);


    }
    final List<users> usersList=new ArrayList<>();
    private void FetchData() {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference1=database.getReference("Users");
        users u=new users("0");
        usersList.add(u);
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds:dataSnapshot.getChildren()
                         ) {
                        users user=ds.getValue(com.kh_sof_dev.admin.Clasess.users.class);
                        user.setId(ds.getKey());
                       usersList.add(user);
                    }
                    adapterUsers=new SpinAdapterUsers(AllOdrer_activity.this,
                            android.R.layout.simple_spinner_item,usersList);
                    usersSP.setAdapter(adapterUsers);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DatabaseReference reference2=database.getReference("Products");
        final List<Product> productList=new ArrayList<>();
        Product p =new Product("الكل ",0.0,0.0);
        productList.add(p);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds:dataSnapshot.getChildren()
                    ) {
                       Product pro=ds.getValue(Product.class);
                        pro.setId(ds.getKey());
                        productList.add(pro);
                    }
                    adapterProduct=new SpinAdapterProduct(AllOdrer_activity.this,
                            android.R.layout.simple_spinner_item,productList);
                    nameSP.setAdapter(adapterProduct);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       SetONSPclick();


    }
String Productname_,user_id,day;
  int  orderTypeNB=1;
    private void SetONSPclick() {
        nameSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Productname_=adapterProduct.getItem(position).getName();
                FilterByAll();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        usersSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user_id=adapterUsers.getItem(position).getId();
               FilterByAll();
                Toast.makeText(AllOdrer_activity.this,adapterUsers.getItem(position).getName()
                        ,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        daySp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] point=getResources().getStringArray(R.array.pointDay);
                day=point[position];
                onfilterPoint(day);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        orderTypeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

switch (position){
    case 0: FitchorderByType(Const.wait);
        orderTypeNB=1;
    break;
    case 1: FitchorderByType(Const.current);
        orderTypeNB=2;

        break;
    case 2: FitchorderByType(Const.complete);
        orderTypeNB=3;

        break;
    case 3: FitchorderByType(Const.cancel);
        orderTypeNB=4;

        break;
}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    Calendar calendar;
    public DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            calendar= Calendar.getInstance();
            calendar.set(Calendar.YEAR,selectedYear);
            calendar.set(Calendar.DAY_OF_MONTH,selectedDay);
            calendar.set(Calendar.MONTH,selectedMonth);
//            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);

            view.setVisibility(view.isShown()
                    ?View.GONE
                    :View.VISIBLE);
            String formatedDate = format.format(calendar.getTime());
            if (date==1){
                datein.setText(formatedDate);

            }else {
                dateto.setText(formatedDate);

            }


        }
    };

    int date;
    public void showThePickers(int i) {
date=i;
        calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(this,
                R.style.TimePickerTheme,datePickerListener,
                calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    RecyclerView list;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private List<Request> requestList;
    private Requestes_adapter adapter;
    private ProgressBar progressBar;

    private void FitchorderByType(String s)
        {
            reference=database.getReference("Requests");
            requestList=new ArrayList<>();
            adapter=new Requestes_adapter(this,requestList,null);
            list.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true));
            list.setAdapter(adapter);


            Log.d("orderType",s);
//             requestList.clear();
            findViewById(R.id.progress).setVisibility(View.VISIBLE);
            findViewById(R.id.noItem).setVisibility(View.GONE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (requestList.size()==0){
                        findViewById(R.id.progress).setVisibility(View.GONE);
                        findViewById(R.id.noItem).setVisibility(View.VISIBLE);
                    }

                }
            }, 5000);

            reference.child(s).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    String user_id=dataSnapshot.getKey();
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {


                            progressBar.setVisibility(View.GONE);

                        Request request=ds.getValue(Request.class);
                        request.setType(orderTypeNB);
                        request.setUsers_id(user_id);
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
    List<Request>  requestListFilter=new ArrayList<>();
    private void FilterByAll() {
        String dateIN=datein.getText().toString();
        String dateTO=dateto.getText().toString();

        String usersID=user_id;
        String productName=Productname_;


        List<Request>  RL_Filter=new ArrayList<>();

        for (Request rqs:requestList
        ) {
                    if (FilterByDate(dateIN,dateTO,rqs) && FilterByName(productName,rqs) && FilterByUsers(usersID,rqs)){
                        RL_Filter.add(rqs);
                           }



        }
        Toast.makeText(getApplicationContext(),"لديك "+RL_Filter.size()+"طلب",Toast.LENGTH_LONG).show();

        adapter=new Requestes_adapter(AllOdrer_activity.this,RL_Filter,null);
        list.setAdapter(adapter);
    }

    private boolean FilterByDate(String date_in, String date_to,Request R) {

        if (date_in.contains("من تاريخ") || date_to.contains("الى تاريخ")){
            return true;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date date = sdf.parse(R.getDate());
            Date datein = sdf.parse(date_in);
            Date dateto = sdf.parse(date_to);

            if ((datein.before(date) && dateto.after(date)) || date.compareTo(dateto)==0 || date.compareTo(datein)==0 ){
               return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }
    private boolean FilterByName(String Productname,Request R) {

        if (Productname.contains("الكل")){
            return true;
        }
        if (R.getProduct().contains(Productname)){
            return true;
        }
        return false;
    }
    private boolean FilterByUsers(String id,Request R) {
        if (id.equals("0")){
            return true;
        }
        if (R.getUsers_id().contains(id)){
            return true;
        }
        return false;

    }

    private void onfilterPoint(String day) {
        final List<users> usersList_=new ArrayList<>();
        if (day.equals("الكل")){
            adapterUsers=new SpinAdapterUsers(AllOdrer_activity.this,
                    android.R.layout.simple_spinner_item,usersList);
            usersSP.setAdapter(adapterUsers);
            return;
        }

        for (users user:usersList
             ) {
            if (user.getPointDay().equals(day)){
                usersList_.add(user);
            }
        }
        adapterUsers=new SpinAdapterUsers(AllOdrer_activity.this,
                android.R.layout.simple_spinner_item,usersList_);
        usersSP.setAdapter(adapterUsers);
    }


    @Override
    protected void onStart() {
        super.onStart();
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
