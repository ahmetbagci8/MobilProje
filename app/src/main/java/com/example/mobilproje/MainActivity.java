package com.example.mobilproje;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.content.Intent;

import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    CalendarView calendarView;
    Calendar calendar;
    int cDay,cMonth,cYear;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendarView=(CalendarView) findViewById(R.id.calendarView);
        drawerLayout=findViewById(R.id.drawer_layout);
        toolbar=findViewById(R.id.toolbarMain);
        navigationView=findViewById(R.id.nav_view);
        calendar=Calendar.getInstance();
        cDay=calendar.get(Calendar.DAY_OF_MONTH);
        cMonth=calendar.get(Calendar.MONTH);
        cYear=calendar.get(Calendar.YEAR);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View background=findViewById(R.id.drawer_layout);
        final SharedPreferences sp = getSharedPreferences("ayarlar", MODE_PRIVATE);
        String app_theme = sp.getString("app-theme", "light");
        if(app_theme.equals("light")){
            background.setBackgroundColor(Color.WHITE);
        }
        else{
            background.setBackgroundColor(0xFF383131);
        }
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int yil, int ay, int gun) {
                Intent choosenDateIntent =new Intent(MainActivity.this, ListEvents.class);
                choosenDateIntent.putExtra("gun",gun);
                choosenDateIntent.putExtra("ay",ay);
                choosenDateIntent.putExtra("yil",yil);
                startActivity(choosenDateIntent);


            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_aylik:
                Intent aylikIntent =new Intent(MainActivity.this, MonthlyEvents.class);
                aylikIntent.putExtra("gun",cDay);
                aylikIntent.putExtra("ay",cMonth);
                aylikIntent.putExtra("yil",cYear);

                startActivity(aylikIntent);
                break;
            case R.id.nav_haftalik:
                Intent haftalikIntent =new Intent(MainActivity.this, WeeklyEvents.class);
                haftalikIntent.putExtra("gun",cDay);
                haftalikIntent.putExtra("ay",cMonth);
                haftalikIntent.putExtra("yil",cYear);
                startActivity(haftalikIntent);
                break;
            case R.id.nav_gunluk:
                Intent gunlukIntent =new Intent(MainActivity.this, DailyEvents.class);
                gunlukIntent.putExtra("gun",cDay);
                gunlukIntent.putExtra("ay",cMonth);
                gunlukIntent.putExtra("yil",cYear);
                startActivity(gunlukIntent);
                break;
            case R.id.nav_gorev:
                Intent gorevIntent =new Intent(MainActivity.this, CategoryEvents.class);
                gorevIntent.putExtra("gun",cDay);
                gorevIntent.putExtra("ay",cMonth);
                gorevIntent.putExtra("yil",cYear);
                gorevIntent.putExtra("kategori","Görev");
                startActivity(gorevIntent);
                break;
            case R.id.nav_dogumgunu:
                Intent dogumGunuIntent =new Intent(MainActivity.this, CategoryEvents.class);
                dogumGunuIntent.putExtra("gun",cDay);
                dogumGunuIntent.putExtra("ay",cMonth);
                dogumGunuIntent.putExtra("yil",cYear);
                dogumGunuIntent.putExtra("kategori","Doğumgünü");
                startActivity(dogumGunuIntent);
                break;
            case R.id.nav_toplanti:
                Intent toplantiIntent =new Intent(MainActivity.this, CategoryEvents.class);
                toplantiIntent.putExtra("gun",cDay);
                toplantiIntent.putExtra("ay",cMonth);
                toplantiIntent.putExtra("yil",cYear);
                toplantiIntent.putExtra("kategori","Toplantı");
                startActivity(toplantiIntent);
                break;
            case R.id.nav_bulusma:
                Intent bulusmaIntent =new Intent(MainActivity.this, CategoryEvents.class);
                bulusmaIntent.putExtra("gun",cDay);
                bulusmaIntent.putExtra("ay",cMonth);
                bulusmaIntent.putExtra("yil",cYear);
                bulusmaIntent.putExtra("kategori","Buluşma");
                startActivity(bulusmaIntent);
                break;
            case R.id.nav_ders:
                Intent dersIntent =new Intent(MainActivity.this, CategoryEvents.class);
                dersIntent.putExtra("gun",cDay);
                dersIntent.putExtra("ay",cMonth);
                dersIntent.putExtra("yil",cYear);
                dersIntent.putExtra("kategori","Ders Çalışma");
                startActivity(dersIntent);
                break;
            case R.id.nav_settings:
                Intent settingsIntent =new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
        }
        return false;
    }



}
