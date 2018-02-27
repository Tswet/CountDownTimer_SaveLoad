package com.gmail.mtswetkov.countdowntimer;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Mikhail on 22.03.2017.
 */
public class Tren implements Serializable {

    private UUID mId;
    private String mName;
    private int timerAll;
    private int countEdu;
    private int educ;
    int rest;
    int bigRest;
    int bigRestSec;

    public Tren(UUID mId, String mName, int timerAll, int countEdu, int educ, int rest, int bigRest, int bigRestSec) {
        this.mId = mId;
        this.mName = mName;
        this.timerAll = timerAll;
        this.countEdu = countEdu;
        this.educ = educ;
        this.rest = rest;
        this.bigRest = bigRest;
        this.bigRestSec = bigRestSec;
    }

    public Tren(){

    }

    public int getTimerAll() {
        return timerAll;
    }

    public void setTimerAll(int timerAll) {
        this.timerAll = timerAll;
    }

    public int getCountEdu() {
        return countEdu;
    }

    public void setCountEdu(int countEdu) {
        this.countEdu = countEdu;
    }

    public int getEduc() {
        return educ;
    }

    public void setEduc(int educ) {
        this.educ = educ;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public int getBigRest() {
        return bigRest;
    }

    public void setBigRest(int bigRest) {
        this.bigRest = bigRest;
    }

    public int getBigRestSec() {
        return bigRestSec;
    }

    public void setBigRestSec(int bigRestSec) {
        this.bigRestSec = bigRestSec;
    }
}
