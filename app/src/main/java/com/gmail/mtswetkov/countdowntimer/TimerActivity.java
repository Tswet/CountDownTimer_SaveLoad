package com.gmail.mtswetkov.countdowntimer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;


/**
 * Created by Mikhail on 23.03.2017.
 */
public class TimerActivity extends Activity {


    public TextView chronView;
    public TextView chStatus;
    public TextView inSetView;
    public TextView allSetView;

    public static Button getfButton() {
        return fButton;
    }

    public static Button getpButton() {
        return pButton;
    }

    public static Button fButton;
    public static Button pButton;
    public String pStatus;
    final ArrayList<ShowTimer> ar = new ArrayList<>();
    Tren mTREN;


    public static void setMBGLL(LinearLayout MBGLL) {
        TimerActivity.MBGLL = MBGLL;
    }

    public static LinearLayout getMBGLL() {
        return MBGLL;
    }

    public static void setINUSE(int INUSE) {
        TimerActivity.INUSE = INUSE;
    }

    public static void setINTICK(int INTICK) {
        TimerActivity.INTICK = INTICK;
    }

    public static LinearLayout MBGLL; // определение цвета бэкграунда
    //набор статических переменных для работы кнопок
    public static int INUSE = 0; //индекс активного элемента массива
    public static int INTICK = 0;   //использкется для работы паузы. Запоминается тик в сете
    public static int ARLENGHT = 0; //общая длина массива упражнений. используется для закрытия приложения

    public static int getINSET() {
        return INSET;
    }

    public static void setINSET(int INSET) {
        TimerActivity.INSET = INSET;
    }

    public static int getALLSET() {
        return ALLSET;
    }

    //набор статических переменных для отображения общего счетчика
    public static int INSET = 1;
    public static int ALLSET = 0;

    //набор статических  переменных для статусов
    public static String WORK;
    public static String REST;
    public static String BIGREST;
    public static String DONE;
    //набор статических  переменных для названия кнопок
    public static String PAUSE;
    public static String RESUME;
    //набор статических  переменных для стилей кнопок
    Intent intent;


    @Override
    public void finish() {
        stopService(new Intent(this, ShowTimerService.class));
        super.finish();
        INSET = 1;
        INTICK = 0;
        INUSE = 0;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //не  выключать экрвн
        chronView = (TextView) findViewById(R.id.chronView);
        chStatus = (TextView) findViewById(R.id.chronText);
        inSetView = (TextView) findViewById(R.id.partEdu);
        allSetView = (TextView) findViewById(R.id.fromEdu);
        pButton = (Button)findViewById(R.id.pauseButton);
        fButton = (Button)findViewById(R.id.finishButton);
        MBGLL = (LinearLayout) findViewById(R.id.timerLayout);

        //получаем данные из Активити Мэйн
        Bundle b = getIntent().getExtras();
        mTREN = (Tren) b.get(MainActivity.TREN);

        //получение ресурсов
        WORK = String.valueOf(this.getResources().getString(R.string.workout));
        REST = String.valueOf(this.getResources().getString(R.string.rest));
        BIGREST = String.valueOf(this.getResources().getString(R.string.big_rest));
        DONE = String.valueOf(this.getResources().getString(R.string.done));
        PAUSE = String.valueOf(this.getResources().getString(R.string.pause_button));
        RESUME = String.valueOf(this.getResources().getString(R.string.pause_button_resume));
        intent =  new Intent(this, ShowTimerService.class);


        int count =0;
        final MediaPlayer alarm = MediaPlayer.create(this, R.raw.alarm);
        final MediaPlayer reload = MediaPlayer.create(this, R.raw.ring1s);

        fButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tick = INTICK;
                int inUse = INUSE;

                pStatus = (String) chStatus.getText();
                if(pButton.getText().equals(TimerActivity.PAUSE)){
                    pButton.setText(TimerActivity.RESUME);
                    ShowTimerService.st.cancel();

                }else {
                    pButton.setText(TimerActivity.PAUSE);
                    ShowTimerService.ars.remove(inUse);
                    ShowTimerService.ars.add(inUse, new ShowTimer(tick , chronView, chStatus, pStatus, ar, inUse, alarm, inSetView, allSetView ));
                    ShowTimerService.st = ShowTimerService.ars.get(inUse);
                    ShowTimerService.st.start();
                }
            }
        });

        ar.add(new ShowTimer(5,chronView, chStatus, "Ready", ar, count, alarm, inSetView, allSetView));
        for(int j = 0; j<mTREN.getTimerAll(); j++){
            for(int h = 0; h < mTREN.getCountEdu();h++){
                count = ar.size()-1;
                count++;
                ar.add(new ShowTimer(mTREN.getEduc() , chronView, chStatus, WORK, ar, count, reload, inSetView, allSetView ));
                if(mTREN.getRest() !=0) {
                    count++;
                    ar.add(new ShowTimer(mTREN.getRest(), chronView, chStatus, REST, ar, count, alarm, inSetView, allSetView));
                }
            }
            count = ar.size()-1;
            if(mTREN.getBigRest() != 0) {
                count++;
                ar.add(new ShowTimer(mTREN.getBigRest(), chronView, chStatus, BIGREST, ar, count, alarm, inSetView, allSetView));
            }
        }
        ARLENGHT = ar.size();
        ALLSET = mTREN.getCountEdu()*mTREN.getTimerAll() ;
        //System.out.println(ARLENGHT+"++++++");

        Singleton.getInstance().SetArrayList(ar);
        startService(intent);

    }
}
