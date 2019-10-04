package com.kh_sof_dev.admin.Activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.kh_sof_dev.admin.Fragments.AllOrders.cancel_Allrequest;
import com.kh_sof_dev.admin.Fragments.AllOrders.complete_Allrequest;
import com.kh_sof_dev.admin.Fragments.AllOrders.current_Allrequest;
import com.kh_sof_dev.admin.Fragments.AllOrders.waite_Allrequest;
import com.kh_sof_dev.admin.R;

public class AllOdrer_activity extends AppCompatActivity {

public interface Onfilter{
    void onfilterDate(String date);
    void onfilterName(String name);
    void onfilterWeight(String weight);
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
    private EditText date,name,weight;
private Double lat=0.0,lng=0.0;
    private ViewPager mViewPager;
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

        date=findViewById(R.id.date);
        name=findViewById(R.id.prod_name);
        weight=findViewById(R.id.prod_weight);
Button filter=findViewById(R.id.filter);
filter.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String date_=date.getText().toString();
        String name_=name.getText().toString();
        String weight_=weight.getText().toString();
        if (!date_.isEmpty()){
            onfilterListenner.onfilterDate(date_);
        }
        if (!name_.isEmpty()){
            onfilterListenner.onfilterName(name_);
        }
        if (!weight_.isEmpty()){
            onfilterListenner.onfilterWeight(weight_);
        }
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
