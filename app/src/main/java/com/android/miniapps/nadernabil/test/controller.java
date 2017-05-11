package com.android.miniapps.nadernabil.test;

import android.app.Application;
import android.util.Log;

/**
 * Created by NaderNabil on 4/25/2017.
 */

public class controller extends Application {
    private static final controller ourInstance = new controller();
    private Presenter presenter;

    static controller getInstance() {
        Log.d("nader", "instance reached");
        return ourInstance;
    }

    public controller() {
    }

    public void setPresenter(Presenter presenter) {
        Log.d("nader", "set reached");
        this.presenter = presenter;

    }

    public Presenter getPresenter() {
        return this.presenter;
    }

    public String showlog() {
        return "a7eh";
    }
}
