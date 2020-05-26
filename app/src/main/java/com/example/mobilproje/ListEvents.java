package com.example.mobilproje;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ListEvents extends AppCompatActivity {
    FloatingActionButton addEventButton;
    TextView dateBetween;
    private SQLiteDatabase mDatabase;
    private EventAdapter mAdapter;
    RecyclerView recyclerView;
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_events);
        final int gun=getIntent().getIntExtra("gun",1);
        final int ay=getIntent().getIntExtra("ay",1);
        final int yil=getIntent().getIntExtra("yil",2020);
        date=String.format("%04d-%02d-%02d",yil,ay+1,gun);
        addEventButton=findViewById(R.id.floatingActionButton);
        dateBetween=findViewById(R.id.dateBetween_Text);
        dateBetween.setText(date);
        Database dbHelper = new Database(this);
        mDatabase = dbHelper.getWritableDatabase();
        recyclerView = findViewById(R.id.list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new EventAdapter(this, getAllItemsByDate(date));
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

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(gun,ay,yil);
            }
        });
    }
    private void showItem(long id){
        Intent showEventIntent =new Intent(ListEvents.this,ShowEventActivity.class);
        showEventIntent.putExtra("id",id);
        startActivity(showEventIntent);
    }

    private void addItem(int gun,int ay,int yil) {
        Intent addEventIntent =new Intent(ListEvents.this,EventActivity.class);
        addEventIntent.putExtra("gun",gun);
        addEventIntent.putExtra("ay",ay);
        addEventIntent.putExtra("yil",yil);
        startActivity(addEventIntent);

    }
    private void removeItem(long id) {
        mDatabase.delete("events",
                "id" + "=" + id, null);
        mAdapter.swapCursor(getAllItemsByDate(date));
    }

    private Cursor getAllItemsByDate(String date){
        String whereClause = "date = ?";
        String[] whereArgs = new String[] {
                date
        };
        return mDatabase.query(
                "events",
                null,
                whereClause,
                whereArgs,
                null,
                null,
                "date" + " DESC"
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter = new EventAdapter(getApplicationContext(), getAllItemsByDate(date));
        recyclerView.setAdapter(mAdapter);

    }

}
