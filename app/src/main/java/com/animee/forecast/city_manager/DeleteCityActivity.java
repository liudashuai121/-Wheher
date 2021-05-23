package com.animee.forecast.city_manager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.animee.forecast.R;
import com.animee.forecast.db.DBManager;

import java.util.ArrayList;
import java.util.List;

public class DeleteCityActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView errorIv,rightIv;
    ListView deleteLv;
    List<String>mDatas;   //listview的数据源
    List<String>deleteCitys;  //表示存储了删除的城市信息
    private DeleteCityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_city);
        errorIv = findViewById(R.id.delete_iv_error);
        rightIv = findViewById(R.id.delete_iv_right);
        deleteLv = findViewById(R.id.delete_lv);
        mDatas = DBManager.queryAllCityName();
        deleteCitys = new ArrayList<>();
//        Set click listen event
        errorIv.setOnClickListener(this);
        rightIv.setOnClickListener(this);
//        Adapter settings
        adapter = new DeleteCityAdapter(this, mDatas, deleteCitys);
        deleteLv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_iv_error:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Tips").setMessage("Are you sure you want to discard your changes?")
                        .setPositiveButton("Discard changes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    finish();   //关闭当前的activity
                            }
                        });
                builder.setNegativeButton("cancel",null);
                builder.create().show();
                break;
            case R.id.delete_iv_right:
                for (int i = 0; i < deleteCitys.size(); i++) {
                    String city = deleteCitys.get(i);
//              Call delete city function
                    int i1 = DBManager.deleteInfoByCity(city);
                }
//                Delete successfully and return to the previous page
                finish();
                break;
        }
    }
}
