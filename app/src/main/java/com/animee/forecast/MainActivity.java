package com.animee.forecast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.animee.forecast.city_manager.CityManagerActivity;
import com.animee.forecast.db.DBManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView addCityIv,moreIv;
    LinearLayout pointLayout;
    RelativeLayout outLayout;
    ViewPager mainVp;
    List<Fragment>fragmentList;
    List<String>cityList;
    List<ImageView>imgList;
    private CityFragmentPagerAdapter adapter;
    private SharedPreferences pref;
    private int bgNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addCityIv = findViewById(R.id.main_iv_add);
        moreIv = findViewById(R.id.main_iv_more);
        pointLayout = findViewById(R.id.main_layout_point);
        outLayout = findViewById(R.id.main_out_layout);
        mainVp = findViewById(R.id.main_vp);
//        Add a click event
        addCityIv.setOnClickListener(this);
        moreIv.setOnClickListener(this);

        fragmentList = new ArrayList<>();
        cityList = DBManager.queryAllCityName();  //Get the list of cities contained in the database
        imgList = new ArrayList<>();

        if (cityList.size()==0) {
            cityList.add("lille");
        }
        /* Because the search interface may be clicked to jump to this interface, the value will be passed, so get it here*/
        try {
            Intent intent = getIntent();
            String city = intent.getStringExtra("city");
            if (!cityList.contains(city)&&!TextUtils.isEmpty(city)) {
                cityList.add(city);
            }
        }catch (Exception e){
            Log.i("animee","程序出现问题了！！");
        }
//        Method to initialize ViewPager page
        initPager();
        adapter = new CityFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        mainVp.setAdapter(adapter);

        initPoint();
//       Set last city info
        mainVp.setCurrentItem(fragmentList.size()-1);
//        Set ViewPager page listener
        setPagerListener();
    }
    private void setPagerListener() {
        mainVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < imgList.size(); i++) {
                    imgList.get(i).setImageResource(R.mipmap.a1);
                }
                imgList.get(position).setImageResource(R.mipmap.a2);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initPoint() {
//      Function to create a dot ViewPager page indicator
        for (int i = 0; i < fragmentList.size(); i++) {
            ImageView pIv = new ImageView(this);
            pIv.setImageResource(R.mipmap.a1);
            pIv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) pIv.getLayoutParams();
            lp.setMargins(0,0,20,0);
            imgList.add(pIv);
            pointLayout.addView(pIv);
        }
        imgList.get(imgList.size()-1).setImageResource(R.mipmap.a2);


    }

    private void initPager() {
        for (int i = 0; i < cityList.size(); i++) {
            CityWeatherFragment cwFrag = new CityWeatherFragment();
            Bundle bundle = new Bundle();
            bundle.putString("city",cityList.get(i));
            cwFrag.setArguments(bundle);
            fragmentList.add(cwFrag);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.main_iv_add:
                intent.setClass(this,CityManagerActivity.class);
                break;
            case R.id.main_iv_more:
                intent.setClass(this,MoreActivity.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        Get the remaining city collection in the database
        List<String> list = DBManager.queryAllCityName();
        if (list.size()==0) {
            list.add("lille");
        }
        cityList.clear();    //Empty the original data source before rewriting the load
        cityList.addAll(list);
//        The remaining cities must also create corresponding fragment pages
        fragmentList.clear();
        initPager();
        adapter.notifyDataSetChanged();
//       The number of pages changes, the number of indicators also changes, rewrite settings to add indicators
        imgList.clear();
        pointLayout.removeAllViews();   //Remove all elements from the layout
        initPoint();
        mainVp.setCurrentItem(fragmentList.size()-1);
    }
}
