package com.animee.forecast;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.animee.forecast.db.DBManager;

public class MoreActivity extends AppCompatActivity implements View.OnClickListener{
    TextView shareTv;
    RadioGroup exbgRg;
    ImageView backIv;
    private SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        shareTv = findViewById(R.id.more_tv_share);
        backIv = findViewById(R.id.more_iv_back);

        shareTv.setOnClickListener(this);
        backIv.setOnClickListener(this);
        pref = getSharedPreferences("bg_pref", MODE_PRIVATE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_iv_back:
                finish();
                break;
            case R.id.more_tv_share:
                shareSoftwareMsg("This app is cute. Download it now!");
                break;

        }
    }

    private void shareSoftwareMsg(String s) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,s);
        startActivity(Intent.createChooser(intent,"ISEN Weather"));
    }



}
