package com.animee.forecast;


import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.animee.forecast.base.BaseFragment;
import com.animee.forecast.bean.WeatherBean;
import com.animee.forecast.db.DBManager;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class CityWeatherFragment extends BaseFragment {
    TextView tempTv,cityTv,conditionTv,windTv,tempRangeTv,dateTv;
    LinearLayout futureLayout;
    ScrollView outLayout;
    String url1 = "https://free-api.heweather.net/s6/weather/forecast?location=";
    String url2 = "&key=669b9babdaa94451b61b458a5534f8b7";
    String city;
    private SharedPreferences pref;
    private int bgNum;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_weather, container, false);
        initView(view);
//        You can get the weather condition of the current fragment through the value passed in the activity.
        Bundle bundle = getArguments();
        city = bundle.getString("city");
        String url = url1+city+url2;
//      Call the parent class to get data
        loadData(url);
        return view;
    }

    @Override
    public void onSuccess(String result) {
//        Parse and display the data
         parseShowData(result);
//        update data
        int i = DBManager.updateInfoByCity(city, result);
        if (i<=0) {
//        Failed to update the database, indicating that there is no information about this city, increase this city record
            DBManager.addCityInfo(city,result);
        }
    }
    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
//        Last lookup in the database is displayed in the fragment
        String s = DBManager.queryInfoByCity(city);
        if (!TextUtils.isEmpty(s)) {
            parseShowData(s);
        }

    }
    private void parseShowData(String result) {
//        Parsing data using gson
        WeatherBean weatherBean = new Gson().fromJson(result, WeatherBean.class);
        WeatherBean.HeWeather6Bean resultsBean = weatherBean.getHeWeather6().get(0);
//        Setting up TextView
        dateTv.setText(resultsBean.getDaily_forecast().get(0).getDate());
        cityTv.setText(resultsBean.getBasic().getLocation());
//        Get Today's Weather
        windTv.setText("wind："+resultsBean.getDaily_forecast().get(0).getWind_dir());
        tempRangeTv.setText(resultsBean.getDaily_forecast().get(0).getTmp_min()+"℃-"+resultsBean.getDaily_forecast().get(0).getTmp_max()+"℃");
        conditionTv.setText(resultsBean.getDaily_forecast().get(0).getCond_txt_d());
        tempTv.setText(resultsBean.getDaily_forecast().get(0).getTmp_min()+"℃/"+resultsBean.getDaily_forecast().get(0).getTmp_max()+"℃");




        List<WeatherBean.HeWeather6Bean.DailyForecastBean> futureList = resultsBean.getDaily_forecast();
        futureList.remove(0);
        for (int i = 0; i < futureList.size(); i++) {
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_main_center, null);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            futureLayout.addView(itemView);
            TextView idateTv = itemView.findViewById(R.id.item_center_tv_date);
            TextView iconTv = itemView.findViewById(R.id.item_center_tv_con);
            TextView itemprangeTv = itemView.findViewById(R.id.item_center_tv_temp);
//        Get weather conditions for the corresponding location
            WeatherBean.HeWeather6Bean.DailyForecastBean dataBean = futureList.get(i);
            idateTv.setText(dataBean.getDate());
            iconTv.setText(dataBean.getCond_txt_n());
            itemprangeTv.setText(dataBean.getTmp_min()+"-"+dataBean.getTmp_max());

        }
    }

    private void initView(View view) {
//        Used to initialize control operations
        tempTv = view.findViewById(R.id.frag_tv_currenttemp);
        cityTv = view.findViewById(R.id.frag_tv_city);
        conditionTv = view.findViewById(R.id.frag_tv_condition);
        windTv = view.findViewById(R.id.frag_tv_wind);
        tempRangeTv = view.findViewById(R.id.frag_tv_temprange);
        dateTv = view.findViewById(R.id.frag_tv_date);


        futureLayout = view.findViewById(R.id.frag_center_layout);
        outLayout = view.findViewById(R.id.out_layout);
    }


}
