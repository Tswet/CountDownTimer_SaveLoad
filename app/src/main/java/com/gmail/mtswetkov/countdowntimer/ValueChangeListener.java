package com.gmail.mtswetkov.countdowntimer;

/**
 * Created by Mikhail on 20.02.2018.
 */

public class ValueChangeListener {

        private boolean boo = false;
        private ChangeListener listener;

        public boolean isBoo() {
            return boo;
        }

        public void setBoo(boolean boo) {
            this.boo = boo;
            if (listener != null) listener.onChange();
        }

        public ChangeListener getListener() {
            return listener;
        }

        public void setListener(ChangeListener listener) {
            this.listener = listener;
        }

        public interface ChangeListener {
            void onChange();
        }
}
