package com.animee.forecast.city_manager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.animee.forecast.MainActivity;
import com.animee.forecast.R;
import com.animee.forecast.base.BaseActivity;
import com.animee.forecast.bean.WeatherBean;
import com.google.gson.Gson;

public class SearchCityActivity extends BaseActivity implements View.OnClickListener{
    EditText searchEt;
    ImageView submitIv;
    private ArrayAdapter<String> adapter;
    String url1 = "https://free-api.heweather.net/s6/weather/forecast?location=";
    String url2 = "&key=669b9babdaa94451b61b458a5534f8b7";
    String city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);
        searchEt = findViewById(R.id.search_et);
        submitIv = findViewById(R.id.search_iv_submit);
        submitIv.setOnClickListener(this);
//        Setting up the adapter

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_iv_submit:
                city = searchEt.getText().toString();
                if (!TextUtils.isEmpty(city)) {
//                      Determine if you can find this city
                        String url = url1+city+url2;
                        loadData(url);
                }else {
                    Toast.makeText(this,"null！",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onSuccess(String result) {
        WeatherBean weatherBean = new Gson().fromJson(result, WeatherBean.class);
        if (1==1) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("city",city);
            startActivity(intent);
        }else{
            Toast.makeText(this,"Weather information for this city is not currently available ...",Toast.LENGTH_SHORT).show();
        }
    }
}
