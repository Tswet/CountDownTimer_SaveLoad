package com.gmail.mtswetkov.countdowntimer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.ArrayList;

/**
 * Created by Mikhail on 14.11.2017.
 */

public class ShowTimerService extends Service {
    public static ArrayList<ShowTimer> ars;
    public static ValueChangeListener mOnChange = new ValueChangeListener();
    int count = 0;
    public static ShowTimer st;


    public void onCreate(){
        super.onCreate();
    }
    public int onStartCommand (Intent intent, int flags, int startID) {

        ars = Singleton.getInstance().getArrayList();

        st = ars.get(count);
        st.start();

        mOnChange.setListener(new ValueChangeListener.ChangeListener() {
            @Override
            public void onChange() {
                count++;
                st = ars.get(count);
                st.start();
            }
        });

        return super.onStartCommand(intent, flags, startID);
    }

    public void onResume(){
        super.onDestroy();
        stopSelf();
    }

    public void onDestroy(){
        st.cancel();
        super.onDestroy();
        stopSelf();

    }

    public IBinder onBind(Intent intent) {
        return null;
    }

}

