package com.example.mobilproje;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class WeeklyEvents extends AppCompatActivity {

    FloatingActionButton previousEventsButton;
    FloatingActionButton nextEventsButton;
    TextView dateBetween;
    private SQLiteDatabase mDatabase;
    private EventAdapter mAdapter;
    private int mAmount = 0;
    int gun,ay,yil;
    int gun2,ay2,yil2;
    String dateView;
    String dateDb,dateDb2;
    Calendar calendar;
    Calendar calendar2;
    int fark;
    String dateView1,dateView2;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_events);
        gun=getIntent().getIntExtra("gun",1);
        ay=getIntent().getIntExtra("ay",1);
        yil=getIntent().getIntExtra("yil",2020);
        calendar = Calendar.getInstance();
        calendar2 = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,gun);
        calendar.set(Calendar.MONTH,ay);
        calendar.set(Calendar.YEAR,yil);
        fark=calendar.getFirstDayOfWeek()-calendar.get(Calendar.DAY_OF_WEEK);
        if(fark==1) fark=-6;//Haftayı pazartesiden başlatmak için.Normalde başlangıç pazar.
        calendar.add(Calendar.DAY_OF_YEAR, fark);
        calendar2.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH));
        calendar2.set(Calendar.MONTH,calendar.get(Calendar.MONTH));
        calendar2.set(Calendar.YEAR,calendar.get(Calendar.YEAR));
        calendar2.add(Calendar.DAY_OF_YEAR, 6);
        gun=calendar.get(Calendar.DAY_OF_MONTH);
        ay=calendar.get(Calendar.MONTH);
        yil=calendar.get(Calendar.YEAR);
        gun2=calendar2.get(Calendar.DAY_OF_MONTH);
        ay2=calendar2.get(Calendar.MONTH);
        yil2=calendar2.get(Calendar.YEAR);
        dateView=String.format("%04d-%02d-%02d/%04d-%02d-%02d",yil,ay+1,gun,yil2,ay2+1,gun2);
        dateDb=String.format("%04d-%02d-%02d",yil,ay,gun);
        dateDb2=String.format("%04d-%02d-%02d",yil2,ay2,gun2);
        dateView1=String.format("%04d-%02d-%02d",yil,ay+1,gun);
        dateView2=String.format("%04d-%02d-%02d",yil2,ay2+1,gun2);
        previousEventsButton=findViewById(R.id.floatingActionButtonSol);
        nextEventsButton=findViewById(R.id.floatingActionButtonSag);
        dateBetween=findViewById(R.id.dateBetween_Text);
        dateBetween.setText(dateView);
        Database dbHelper = new Database(this);
        mDatabase = dbHelper.getWritableDatabase();
        recyclerView = findViewById(R.id.list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new EventAdapter(this, getAllItemsByBetweenDate(dateView1,dateView2));
        recyclerView.setAdapter(mAdapter);
        View background=findViewById(R.id.list_recyclerview);
        final SharedPreferences sp = getSharedPreferences("ayarlar", MODE_PRIVATE);
        String app_theme = sp.getString("app-theme", "light");
        if(app_theme.equals("light")){
            background.setBackgroundColor(Color.WHITE);
        }
        else{
            background.setBackgroundColor(0xFF383131);
        }
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction==4){
                    showItem((long) viewHolder.itemView.getTag());
                }
                else if(direction==8){
                    removeItem((long) viewHolder.itemView.getTag());
                    long id=(long) viewHolder.itemView.getTag();
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
                    for(int i = 0; i < 3; i++){
                        int alarmId=(int)id*3+i;
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), alarmId, intent, 0);
                        alarmManager.cancel(pendingIntent);
                    }
                    Snackbar.make(getWindow().getDecorView(),"Etkinlik silindi",Snackbar.LENGTH_SHORT).show();
                }
            }
        }).attachToRecyclerView(recyclerView);
        //dateBetween.setText();

        previousEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DAY_OF_YEAR, -7);
                calendar2.add(Calendar.DAY_OF_YEAR, -7);
                gun=calendar.get(Calendar.DAY_OF_MONTH);
                ay=calendar.get(Calendar.MONTH);
                yil=calendar.get(Calendar.YEAR);
                gun2=calendar2.get(Calendar.DAY_OF_MONTH);
                ay2=calendar2.get(Calendar.MONTH);
                yil2=calendar2.get(Calendar.YEAR);
                dateView=String.format("%04d-%02d-%02d/%04d-%02d-%02d",yil,ay+1,gun,yil2,ay2+1,gun2);
                dateDb=String.format("%04d-%02d-%02d",yil,ay,gun);
                dateDb2=String.format("%04d-%02d-%02d",yil2,ay2,gun2);
                dateView1=String.format("%04d-%02d-%02d",yil,ay+1,gun);
                dateView2=String.format("%04d-%02d-%02d",yil2,ay2+1,gun2);
                previousEventsButton=findViewById(R.id.floatingActionButtonSol);
                nextEventsButton=findViewById(R.id.floatingActionButtonSag);
                dateBetween=findViewById(R.id.dateBetween_Text);
                dateBetween.setText(dateView);
                Database dbHelper = new Database(getApplicationContext());
                mDatabase = dbHelper.getWritableDatabase();
                RecyclerView recyclerView = findViewById(R.id.list_recyclerview);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                mAdapter = new EventAdapter(getApplicationContext(), getAllItemsByBetweenDate(dateView1,dateView2));
                recyclerView.setAdapter(mAdapter);
            }
        });
        nextEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar.add(Calendar.DAY_OF_YEAR, 7);
                calendar2.add(Calendar.DAY_OF_YEAR, 7);
                gun=calendar.get(Calendar.DAY_OF_MONTH);
                ay=calendar.get(Calendar.MONTH);
                yil=calendar.get(Calendar.YEAR);
                gun2=calendar2.get(Calendar.DAY_OF_MONTH);
                ay2=calendar2.get(Calendar.MONTH);
                yil2=calendar2.get(Calendar.YEAR);
                dateView=String.format("%04d-%02d-%02d/%04d-%02d-%02d",yil,ay+1,gun,yil2,ay2+1,gun2);
                dateDb=String.format("%04d-%02d-%02d",yil,ay,gun);
                dateDb2=String.format("%04d-%02d-%02d",yil2,ay2,gun2);
                dateView1=String.format("%04d-%02d-%02d",yil,ay+1,gun);
                dateView2=String.format("%04d-%02d-%02d",yil2,ay2+1,gun2);
                previousEventsButton=findViewById(R.id.floatingActionButtonSol);
                nextEventsButton=findViewById(R.id.floatingActionButtonSag);
                dateBetween=findViewById(R.id.dateBetween_Text);
                dateBetween.setText(dateView);
                Database dbHelper = new Database(getApplicationContext());
                mDatabase = dbHelper.getWritableDatabase();
                RecyclerView recyclerView = findViewById(R.id.list_recyclerview);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                mAdapter = new EventAdapter(getApplicationContext(), getAllItemsByBetweenDate(dateView1,dateView2));
                recyclerView.setAdapter(mAdapter);
            }
        });
    }
    private void showItem(long id){
        Intent showEventIntent =new Intent(WeeklyEvents.this,ShowEventActivity.class);
        showEventIntent.putExtra("id",id);
        startActivity(showEventIntent);
    }


    private void removeItem(long id) {
        mDatabase.delete("events",
                "id" + "=" + id, null);
        mAdapter.swapCursor(getAllItemsByBetweenDate(dateView1,dateView2));
    }

    private Cursor getAllItemsByBetweenDate(String startDate,String stopDate){
        String whereClause = "date >= ? AND date <= ?";
        String[] whereArgs = new String[] {
                startDate,stopDate
        };
        return mDatabase.query(
                "events",
                null,
                whereClause,
                whereArgs,
                null,
                null,
                "date" + " ASC"
        );
    }
    @Override
    protected void onResume() {
        super.onResume();
        mAdapter = new EventAdapter(getApplicationContext(), getAllItemsByBetweenDate(dateView1,dateView2));
        recyclerView.setAdapter(mAdapter);

    }
}
