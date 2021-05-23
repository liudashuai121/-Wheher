package com.animee.forecast.base;

import android.support.v4.app.Fragment;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;


public class BaseFragment extends Fragment implements Callback.CommonCallback<String>{

    public void loadData(String path){
        RequestParams params = new RequestParams(path);
        x.http().get(params,this);
    }
//    Interface that will call back when data is successfully obtained
    @Override
    public void onSuccess(String result) {

    }
//  Interface that will be called when data acquisition fails
    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

    }
//  Interface that will be called when the request is cancelled
    @Override
    public void onCancelled(CancelledException cex) {

    }
//  The interface that will call back after the request is completed
    @Override
    public void onFinished() {

    }
}
