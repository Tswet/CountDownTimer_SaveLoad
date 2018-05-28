package com.gmail.mtswetkov.countdowntimer;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.*;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity{

    public Button startButton;

    TextView tTimerAll; //количество подходов
    TextView tcountEdu; //количество упражнений
    TextView teduc; //время на упражнение
    TextView trest; //отдых между упражнениями
    TextView tbigRest; //отдых между подходами минуты
    TextView tbigRestSec; //отдых между подходами секунды

    public static final String TREN = "TREN" ;
    public static final String JSONTREN = "JSONTREN";
    //переменные для сохранения последней тренеровки
    public static final String MY_SET = "mySet";
    SharedPreferences sPref;

    public ArrayList mTrns = new ArrayList<>();

    Tren MyTrain = new Tren();
    String jsonStr;
    Gson g = new Gson();
    public TextInputLayout trngNameInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        jsonStr = "[{\"bigRest\":300,\"bigRestSec\":0,\"countEdu\":3,\"educ\":15,\"mId\":\"bd75fb60-efc6-4bf6-a6c3-34d31013aff4\",\"mName\":\"1st grng\",\"rest\":15,\"timerAll\":2}]";

        // загружаем последнюю тренеровку в память
        sPref = getSharedPreferences(MY_SET, Context.MODE_PRIVATE);
        int setEd = sPref.getInt("SET_ED", 0);
        int setRe = sPref.getInt("SET_RE", 0);
        int setCed = sPref.getInt("SET_CED", 0);
        int setBr = sPref.getInt("SET_BR", 0);
        int setBrs = sPref.getInt("SET_BRS", 0);
        int setCic = sPref.getInt("SET_CIC", 0);
        jsonStr = sPref.getString("ALL_TRN", "");

        if(jsonStr != "") {
            mTrns = g.fromJson(jsonStr, mTrns.getClass());
        }

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


        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener( new View.OnClickListener(){

            public void onClick(View view) {
                MyTrain = myTrnFiller();

                Intent i = new Intent(MainActivity.this, TimerActivity.class);
                i.putExtra(TREN, MyTrain);

                SharedPreferences.Editor ed = sPref.edit();
                ed.putInt("SET_ED", MyTrain.getEduc());
                ed.putInt("SET_RE", MyTrain.getRest());
                ed.putInt("SET_CED", MyTrain.getCountEdu());
                ed.putInt("SET_BR", (MyTrain.getBigRest() - MyTrain.getBigRestSec())/60 );
                ed.putInt("SET_CIC", MyTrain.getTimerAll());
                ed.putInt("SET_BRS", MyTrain.getBigRestSec());
                ed.putString("ALL_TRN", jsonStr);
                ed.apply();

                if(MyTrain.getTimerAll()==0 || MyTrain.getCountEdu() == 0  ){
                    String toastText = getResources().getString(R.string.error_null);
                    Toast t =  Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT);
                    t.show();
                } else {
                    startActivity(i);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        trngNameInput = findViewById(R.id.trng_name_input);
        //MyTrain = myTrnFiller();

        switch (item.getItemId()) {
            case R.id.save:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setTitle(R.string.save_title);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                  /*      MyTrain.setName(input.getText().toString());
                        mTrns.add(MyTrain);*/
                        String name = input.getText().toString();
                        int tAll = Integer.parseInt(tTimerAll.getText().toString());
                        int cEdu = Integer.parseInt(tcountEdu.getText().toString());
                        int edu = Integer.parseInt(teduc.getText().toString());
                        int re = Integer.parseInt(trest.getText().toString());
                        int tmpBigRestSec = Integer.parseInt(String.valueOf(tbigRestSec.getText()));
                        int bRestSec = Integer.parseInt(String.valueOf(tbigRest.getText()))*60 + tmpBigRestSec;
                        mTrns.add(new Tren(name, tAll, cEdu, edu, re, bRestSec, tmpBigRestSec));
                        sPrefWorker.saveTren(mTrns, sPref);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return true;
            case R.id.load:
                Intent load = new Intent(MainActivity.this, LoadActivity.class);
                if (jsonStr != "") {
                    startActivity(load);
                }else{
                    String toText = getResources().getString(R.string.null_json_load_error);
                    Toast t = Toast.makeText(getApplicationContext(), toText, Toast.LENGTH_SHORT );
                    t.show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public Tren myTrnFiller(){
        int tmpBigRestSec = Integer.parseInt(String.valueOf(tbigRestSec.getText()));
        int bRestSec = Integer.parseInt(String.valueOf(tbigRest.getText()))*60 + tmpBigRestSec;
        MyTrain.setBigRest(bRestSec);
        MyTrain.setCountEdu(Integer.parseInt(tcountEdu.getText().toString()));
        MyTrain.setEduc(Integer.parseInt(teduc.getText().toString()));
        MyTrain.setRest(Integer.parseInt(trest.getText().toString()));
        MyTrain.setTimerAll(Integer.parseInt(tTimerAll.getText().toString()));
        MyTrain.setBigRestSec(tmpBigRestSec);
        return MyTrain;
    }
}

