package com.gmail.mtswetkov.countdowntimer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    public Button startButton;

    TextView tTimerAll; //количество подходов
    TextView tcountEdu; //количество упражнений
    TextView teduc; //время на упражнение
    TextView trest; //отдых между упражнениями
    TextView tbigRest; //отдых между подходами минуты
    TextView tbigRestSec; //отдых между подходами секунды

    public static final String TREN = "TREN" ;
    //переменные для сохранения последней тренеровки
    public static final String MY_SET = "mySet";
    SharedPreferences sPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // загружаем последнюю тренеровку в память
        sPref = getSharedPreferences(MY_SET, Context.MODE_PRIVATE);
        int setEd = sPref.getInt("SET_ED", 0);
        int setRe = sPref.getInt("SET_RE", 0);
        int setCed = sPref.getInt("SET_CED", 0);
        int setBr = sPref.getInt("SET_BR", 0);
        int setBrs = sPref.getInt("SET_BRS", 0);
        int setCic = sPref.getInt("SET_CIC", 0);

        //инициализируем и заполняем из SharedPreferencies данные для таймера
        tTimerAll = (EditText)findViewById(R.id.timerAll);
        if(setCic !=0){
            tTimerAll.setText(String.format("%02d", setCic));
        }
        tcountEdu = (EditText)findViewById(R.id.countEdu);
        if(setCed !=0){
            tcountEdu.setText(String.format("%02d", setCed));
        }
        teduc = (EditText)findViewById(R.id.chrEduc);
        if(setEd !=0){
            teduc.setText(String.format("%02d", setEd));
        }
        trest = (EditText)findViewById(R.id.chrRest);
        if(setRe !=0){
            trest.setText(String.format("%02d", setRe));
        }
        tbigRest = (EditText)findViewById(R.id.chrBigRestMin);
        if(setBr !=0){
            tbigRest.setText(String.format("%02d", setBr));
        }
        tbigRestSec = (EditText)findViewById(R.id.chrBigRestSec);
        if(setBr !=0){
            tbigRestSec.setText(String.format("%02d", setBrs));
        }

        //звполняем объект для передачи в TimerActivity
        final Tren MyTrain = new Tren();


        startButton = (Button)findViewById(R.id.startButton);
        startButton.setOnClickListener( new View.OnClickListener(){

            public void onClick(View view) {

                int tmpBigRestSec = Integer.parseInt(String.valueOf(tbigRestSec.getText()));
                int bRestSec = Integer.parseInt(String.valueOf(tbigRest.getText()))*60 + tmpBigRestSec;

                MyTrain.setBigRest(bRestSec);
                MyTrain.setCountEdu(Integer.parseInt(tcountEdu.getText().toString()));
                MyTrain.setEduc(Integer.parseInt(teduc.getText().toString()));
                MyTrain.setRest(Integer.parseInt(trest.getText().toString()));
                MyTrain.setTimerAll(Integer.parseInt(tTimerAll.getText().toString()));
                MyTrain.setBigRestSec(tmpBigRestSec);

                Intent i = new Intent(MainActivity.this, TimerActivity.class);

                i.putExtra(TREN, MyTrain);

                SharedPreferences.Editor ed = sPref.edit();
                ed.putInt("SET_ED", MyTrain.getEduc());
                ed.putInt("SET_RE", MyTrain.getRest());
                ed.putInt("SET_CED", MyTrain.getCountEdu());
                ed.putInt("SET_BR", (MyTrain.getBigRest() - MyTrain.getBigRestSec())/60 );
                ed.putInt("SET_CIC", MyTrain.getTimerAll());
                ed.putInt("SET_BRS", MyTrain.getBigRestSec());
                ed.commit();

                if(MyTrain.getTimerAll()==0 || MyTrain.getCountEdu() == 0  ){
                    String toastText = String.valueOf(R.string.error_null);
                    Toast t =  Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT);
                    t.show();
                } else {
                    startActivity(i);
                }

            }
        });


    }
}

