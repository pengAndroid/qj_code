package com.demo.li.vc_room_out;

import android.app.Application;

import com.demo.li.vc_room_out.library.tools.CrashHandler;


/**
 *
 * on 2018/12/13 16:30.
 */
public class VisitApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }
}
