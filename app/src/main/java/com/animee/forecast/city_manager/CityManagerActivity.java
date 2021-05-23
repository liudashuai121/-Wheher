package com.animee.forecast.city_manager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.animee.forecast.R;
import com.animee.forecast.db.DBManager;
import com.animee.forecast.db.DatabaseBean;

import java.util.ArrayList;
import java.util.List;

public class CityManagerActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView addIv,backIv,deleteIv;
    ListView cityLv;
    List<DatabaseBean>mDatas;  //Display list data source
    private CityManagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);
        addIv = findViewById(R.id.city_iv_add);
        backIv = findViewById(R.id.city_iv_back);
        deleteIv = findViewById(R.id.city_iv_delete);
        cityLv = findViewById(R.id.city_lv);
        mDatas = new ArrayList<>();
//        Add a click listener event
        addIv.setOnClickListener(this);
        deleteIv.setOnClickListener(this);
        backIv.setOnClickListener(this);
//        Setting up the adapter
        adapter = new CityManagerAdapter(this, mDatas);
        cityLv.setAdapter(adapter);
    }
/*  Get the real data source in the database, add it to the original data source, and prompt the adapter to update*/
    @Override
    protected void onResume() {
        super.onResume();
        List<DatabaseBean> list = DBManager.queryAllInfo();
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.city_iv_add:
                int cityCount = DBManager.getCityCount();
                if (cityCount<5) {
                    Intent intent = new Intent(this, SearchCityActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(this,"Can't add more",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.city_iv_back:
                finish();
                break;
            case R.id.city_iv_delete:
                Intent intent1 = new Intent(this, DeleteCityActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
