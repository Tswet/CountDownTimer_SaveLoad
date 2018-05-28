package com.gmail.mtswetkov.countdowntimer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.gson.Gson;
import java.util.ArrayList;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class LoadActivity extends AppCompatActivity {

    Gson g = new Gson();
    public ArrayList mTrns = new ArrayList<>();
    public static final String LOAD_TREN = "LOAD_TREN";
    RecyclerView recyclerView;
    TrngAdapter adapter;
    SharedPreferences sPref;
    public static final String MY_SET = "mySet";

    public static Intent newIntent(Context packageContext, ArrayList<Tren> mTrns) {
        Intent intent = new Intent(packageContext, TimerActivity.class);
        intent.putExtra(LOAD_TREN, mTrns);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity);

        Bundle b = getIntent().getExtras();
        sPref = getSharedPreferences(MY_SET, Context.MODE_PRIVATE);
        mTrns = sPrefWorker.loadTren(sPref);

        recyclerView = findViewById(R.id.trens_list);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);
        updateUI();
    }

    public class TrngAdapter extends RecyclerView.Adapter<ViewHoolder> {

        private LayoutInflater inflater;
        private ArrayList<Tren> mTrns;

        TrngAdapter(ArrayList<Tren> mTrns) {
            this.mTrns = mTrns;
            this.inflater = LayoutInflater.from(LoadActivity.this);
        }


        public ViewHoolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_for_list, parent, false);
            return new ViewHoolder(view);
        }

        public Tren getItem(int position) {
            return mTrns.get(position);
        }


        @Override
        public void onBindViewHolder(final ViewHoolder holder, int position) {
            Tren tren = mTrns.get(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.onClick(view);                    
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    holder.onLongClick(view);
                    return false;
                }
            });

            holder.bind(tren);
        }

        @Override
        public int getItemCount() {
            return mTrns.size();
        }
    }

    public class ViewHoolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        TextView nameView,
                waView,
                restView,
                brestView,
                cycleView,
                wacycleView;

        ViewHoolder(View view) {
            super(view);
            nameView = view.findViewById(R.id.trng_nane_view);
            restView = view.findViewById(R.id.trng_rest_view);
            waView = view.findViewById(R.id.trng_wa_view);
            brestView = view.findViewById(R.id.trng_brest_view);
            wacycleView = view.findViewById(R.id.trng_wacycle_view);
            cycleView = view.findViewById(R.id.trng_cycle_view);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        void bind(Tren tren) {
            if (tren.getName() == null) {
                nameView.setText("Training");
            } else {
                nameView.setText(tren.getName());
            }
            restView.setText(String.format("%02d", tren.getRest()));
            waView.setText(String.format("%02d", tren.getEduc()));
            brestView.setText(String.format("%02d", tren.getBigRest()));
            wacycleView.setText(String.format("%02d", tren.getCountEdu()));
            cycleView.setText(String.format("%02d", tren.getTimerAll()));
        }


        @Override
        public boolean onLongClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoadActivity.this);
            builder.setTitle(R.string.del_title);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int c = getPosition();
                    mTrns.remove(c);
                    sPrefWorker.saveTren(mTrns, sPref);
                    updateUI();
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
        }

        @Override
        public void onClick(View view) {
            Tren MyTrain = new Tren();
            Intent i = LoadActivity.newIntent(LoadActivity.this, mTrns);
             MyTrain.setCountEdu(Integer.parseInt(wacycleView.getText().toString()));
            MyTrain.setEduc(Integer.parseInt(waView.getText().toString()));
            MyTrain.setRest(Integer.parseInt(restView.getText().toString()));
            MyTrain.setTimerAll(Integer.parseInt(cycleView.getText().toString()));
            MyTrain.setBigRest(Integer.parseInt(String.valueOf(brestView.getText())));
            i.putExtra(LOAD_TREN, MyTrain);
            startActivity(i);
        }

    }
    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    private void updateUI() {

        adapter = new TrngAdapter(sPrefWorker.loadTren(sPref));
        recyclerView.setAdapter(adapter);

/*        if (adapter == null) {
            adapter = new TrngAdapter(sPrefWorker.loadTren(sPref));
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }*/
    }
}

