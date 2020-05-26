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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class CategoryEvents extends AppCompatActivity {

    FloatingActionButton addEventButton;
    TextView dateBetween;
    private SQLiteDatabase mDatabase;
    private EventAdapter mAdapter;
    String category;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_events);
        final int gun=getIntent().getIntExtra("gun",1);
        final int ay=getIntent().getIntExtra("ay",1);
        final int yil=getIntent().getIntExtra("yil",2020);
        category=getIntent().getStringExtra("kategori");
        addEventButton=findViewById(R.id.floatingActionButton);
        dateBetween=findViewById(R.id.dateBetween_Text);
        dateBetween.setText(category);
        Database dbHelper = new Database(this);
        mDatabase = dbHelper.getWritableDatabase();
        recyclerView = findViewById(R.id.list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new EventAdapter(this, getAllItemsByCategory(category));
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
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(gun,ay,yil);
            }
        });
    }
    private void showItem(long id){
        Intent showEventIntent =new Intent(CategoryEvents.this,ShowEventActivity.class);
        showEventIntent.putExtra("id",id);
        startActivity(showEventIntent);
    }

    private void addItem(int gun,int ay,int yil) {
        Intent addEventIntent =new Intent(CategoryEvents.this,EventActivity.class);
        addEventIntent.putExtra("gun",gun);
        addEventIntent.putExtra("ay",ay);
        addEventIntent.putExtra("yil",yil);
        startActivity(addEventIntent);

    }
    private void removeItem(long id) {
        mDatabase.delete("events",
                "id" + "=" + id, null);
        mAdapter.swapCursor(getAllItemsByCategory(category)); //burayı düzelt silince filtre kalkıyor
    }

    private Cursor getAllItemsByCategory(String category){
        String whereClause = "category = ?";
        String[] whereArgs = new String[] {
                category
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
        mAdapter = new EventAdapter(getApplicationContext(), getAllItemsByCategory(category));
        recyclerView.setAdapter(mAdapter);

    }
}
