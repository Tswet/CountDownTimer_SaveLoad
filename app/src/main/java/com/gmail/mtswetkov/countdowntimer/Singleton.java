package com.gmail.mtswetkov.countdowntimer;

import java.util.ArrayList;

/**
 * Created by Mikhail on 15.11.2017.
 */

public class Singleton {

    private ArrayList<ShowTimer> mArrayList;

    private Singleton(){

    }

    private static class SingletonHolder{
        private final static Singleton instance = new Singleton();
    }

    public static Singleton getInstance(){
        return SingletonHolder.instance;
    }

    public void SetArrayList(ArrayList<ShowTimer> ar){
        mArrayList = ar;
    }

    public ArrayList<ShowTimer> getArrayList(){
        return mArrayList;
    }
}
