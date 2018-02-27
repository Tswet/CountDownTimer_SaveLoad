package com.gmail.mtswetkov.countdowntimer;

import android.media.MediaPlayer;
import android.util.TypedValue;
import android.widget.TextView;

import java.sql.Time;
import java.util.ArrayList;

    public class ShowTimer extends MyCountDownTimer {

        TextView chStatus;
        TextView chView;
        TextView inSetView;
        TextView allSetView;
        String status;
        ArrayList<ShowTimer> ar;
        int i;
        MediaPlayer mp;
        int inset;


        public ShowTimer(int mMillisInFuture, TextView chViev, TextView chStatus, String status, ArrayList<ShowTimer> ar, int i, MediaPlayer mp, TextView inSetView, TextView allSetView) {
            super(mMillisInFuture * 1000, 1000);
            this.chStatus = chStatus;
            this.chView = chViev;
            this.inSetView = inSetView;
            this.allSetView = allSetView;
            this.status = status;
            this.ar = ar;
            this.i = i;
            this.mp = mp;
        }

        @Override
        public void onTick( int timeToFinish) {
            chStatus.setText(status);
            int allSet = TimerActivity.getALLSET();
            allSetView.setText(String.format("%02d", allSet));

            if (chStatus.getText().equals(TimerActivity.WORK)) {
                TimerActivity.getMBGLL().setBackgroundResource(R.drawable.workout_gradient);
                TimerActivity.getfButton().setBackgroundResource(R.drawable.workout_button_gradient);
                TimerActivity.getpButton().setBackgroundResource(R.drawable.workout_button_gradient);
                chView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,200);
                if ((timeToFinish/1000) >99) chView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,120);
                inset = TimerActivity.getINSET();
                if (inset <= allSet) {
                    inSetView.setText(String.format("%02d", inset));
                }

            }
            if (chStatus.getText().equals(TimerActivity.REST)) {
                TimerActivity.getMBGLL().setBackgroundResource(R.drawable.rest_gradient);
                TimerActivity.getfButton().setBackgroundResource(R.drawable.rest_button_gradient);
                TimerActivity.getpButton().setBackgroundResource(R.drawable.rest_button_gradient);
                chView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,200);
                if ((timeToFinish/1000) >99) chView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,120);
            }
            if (chStatus.getText().equals(TimerActivity.BIGREST)) {
                TimerActivity.getMBGLL().setBackgroundResource(R.drawable.bigrest_gradient);
                TimerActivity.getfButton().setBackgroundResource(R.drawable.bigrest_button_gradient);
                TimerActivity.getpButton().setBackgroundResource(R.drawable.bigrest_button_gradient);
                chView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,200);
                if ((timeToFinish/1000) >99) chView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,120);
            }
            TimerActivity.setINTICK(Integer.parseInt(String.format("%02d", (timeToFinish - 1) / 1000)));
            System.out.println("+++"+timeToFinish);

            chView.setText(String.format("%02d", (timeToFinish - 1) / 1000));  // timeToFinish-1 для убирания проблемы, что long некорректно делится на int
            System.out.println(String.format("%02d", (timeToFinish - 1) / 1000));
        }

        @Override
        public void onFinish() {
            mp.start();
            i++;
            TimerActivity.setINUSE(i);
            if (chStatus.getText().equals(TimerActivity.WORK)) {
                inset = TimerActivity.getINSET();
                inset++;
                TimerActivity.setINSET(inset);
            }
            int arLenght = TimerActivity.ARLENGHT;

            if (arLenght - 1 > i) {
                ShowTimerService.mOnChange.setBoo(true);
                this.cancel();
            } else {
                chStatus.setText(TimerActivity.DONE);
                this.cancel();

            }

        }
    }
