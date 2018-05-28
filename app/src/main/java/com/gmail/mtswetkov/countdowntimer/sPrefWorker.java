package com.gmail.mtswetkov.countdowntimer;


import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class sPrefWorker {
    private static ArrayList<Tren> trens;
    static SharedPreferences sPref;
    private static String jsonStr;
    static Gson g = new Gson();
    public static final String MY_SET = "mySet";


    static  void saveTren (ArrayList<Tren> trens, SharedPreferences sp){

        sPref = sp;
        jsonStr = g.toJson(trens);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("ALL_TRN", jsonStr);
        ed.apply();

    }

    static ArrayList<Tren> loadTren(SharedPreferences sp){
        sPref = sp;
        jsonStr = sPref.getString("ALL_TRN", "");
        System.out.println("JSON_STRNG" + jsonStr);
        if (jsonStr != "") {
            Type type = new TypeToken<ArrayList<Tren>>() {
            }.getType();
            trens = g.fromJson(jsonStr, type);
        }
        return trens;
    }

}
