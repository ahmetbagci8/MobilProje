package com.example.mobilproje;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class ShowEventActivity extends AppCompatActivity {
    EditText dateText,timeText,eventNameText,eventDetailsText;
    Spinner categorySpinner,hatirlatmaTipSpinner,tekrarTipSpinner;
    Button eventUpdate,eventShare,eventDelete;
    ArrayList<String> categories = new ArrayList<>();
    ArrayAdapter<String> categoryAdapter;
    private SQLiteDatabase mDatabase;
    ImageView mapsButton,alarmButton;
    Double latitude,longitude;
    TextView locationText;
    String hatirlatmaTipi,hatirlatmaSayi,tekrarTipi,tekrarSayi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);
        View background=findViewById(R.id.background);
        final SharedPreferences sp = getSharedPreferences("ayarlar", MODE_PRIVATE);
        String app_theme = sp.getString("app-theme", "light");
        if(app_theme.equals("light")){
            background.setBackgroundColor(Color.WHITE);
        }
        else{
            background.setBackgroundColor(0xFF383131);
        }
        final long id=getIntent().getLongExtra("id",1);
        Database dbHelper = new Database(this);
        mDatabase = dbHelper.getWritableDatabase();
        Cursor mCursor=getAllItemsById(id);
        ArrayList<String> a=new ArrayList<>();
        String name=null,category=null,detail=null,date=null,time=null,location=null;
        while (mCursor.moveToNext()){
            name = mCursor.getString(mCursor.getColumnIndex("name"));
            category = mCursor.getString(mCursor.getColumnIndex("category"));
            detail = mCursor.getString(mCursor.getColumnIndex("detail"));
            date = mCursor.getString(mCursor.getColumnIndex("date"));
            time = mCursor.getString(mCursor.getColumnIndex("time"));
            hatirlatmaSayi = mCursor.getString(mCursor.getColumnIndex("hatirlatmaSure"));
            hatirlatmaTipi = mCursor.getString(mCursor.getColumnIndex("hatirlatmaTip"));
            tekrarSayi = mCursor.getString(mCursor.getColumnIndex("tekrarSure"));
            tekrarTipi = mCursor.getString(mCursor.getColumnIndex("tekrarTip"));
            location=mCursor.getString(mCursor.getColumnIndex("location"));
        }
        timeText =findViewById(R.id.eventTime_Text);
        dateText =findViewById(R.id.eventDate_Text);
        dateText.setText(date);
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        timeText.setText(time);
        eventNameText=findViewById(R.id.eventName_Text);
        eventDetailsText=findViewById(R.id.eventDetails_Text);
        eventNameText.setText(name);
        eventDetailsText.setText(detail);
        categorySpinner=findViewById(R.id.category_spinner);
        hatirlatmaTipSpinner=findViewById(R.id.hatirlatmaTip_spinner);
        tekrarTipSpinner=findViewById(R.id.tekrarTip_spinner);
        eventUpdate=findViewById(R.id.eventUpdate_button);
        eventShare=findViewById(R.id.eventShare_button);
        eventDelete=findViewById(R.id.eventDelete_button);
        categories.add("Görev");
        categories.add("Doğumgünü");
        categories.add("Toplantı");
        categories.add("Buluşma");
        categories.add("Ders Çalışma");
        categoryAdapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1, categories);
        categorySpinner.setAdapter(categoryAdapter);

        if(category.equals("Görev")){
            categorySpinner.setSelection(0);
        }
        else if(category.equals("Doğumgünü")){
            categorySpinner.setSelection(1);
        }
        else if(category.equals("Toplantı")){
            categorySpinner.setSelection(2);
        }
        else if(category.equals("Buluşma")){
            categorySpinner.setSelection(3);
        }
        else if(category.equals("Ders Çalışma")){
            categorySpinner.setSelection(4);
        }

        locationText=findViewById(R.id.location_Text);
        locationText.setText(location);
        mapsButton=findViewById(R.id.mapsView_Button);
        alarmButton=findViewById(R.id.alarmView);
        mapsButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivityForResult(intent, 1000);
            }
        });
        alarmButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), RemainderActivity.class);
                intent.putExtra("yeniMi","hayir");//yeni kayıt
                intent.putExtra("tekrarTipi",tekrarTipi);
                intent.putExtra("hatirlatmaTipi",hatirlatmaTipi);
                intent.putExtra("tekrarSayi",tekrarSayi);
                intent.putExtra("hatirlatmaSayi",hatirlatmaSayi);
                startActivityForResult(intent, 1001);
            }
        });
        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(ShowEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeText.setText( String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, true);
                timePicker.setTitle("Saat Seçiniz");
                timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", timePicker);
                timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", timePicker);
                timePicker.show();
            }
        });

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day=getIntent().getIntExtra("gun",1);
                int month=getIntent().getIntExtra("ay",1);
                int year=getIntent().getIntExtra("yil",2020);

                DatePickerDialog datePicker;
                datePicker = new DatePickerDialog(ShowEventActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        monthOfYear+=1;
                        dateText.setText( String.format("%04d-%02d-%02d",year,monthOfYear,dayOfMonth));

                    }
                },year,month,day);
                datePicker.setTitle("Tarih Seçiniz");
                datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", datePicker);
                datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePicker);
                datePicker.show();



            }
        });


        eventUpdate.setOnClickListener(new View.OnClickListener() {  //DÜZENLE SADECE KOPYALADIM
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                cv.put("name", eventNameText.getText().toString());
                cv.put("detail", eventDetailsText.getText().toString());
                cv.put("date", dateText.getText().toString());
                cv.put("time", timeText.getText().toString());
                cv.put("hatirlatmaSure", hatirlatmaSayi);
                cv.put("tekrarSure", tekrarSayi);
                cv.put("hatirlatmaTip", hatirlatmaTipi);
                cv.put("tekrarTip", tekrarTipi);
                cv.put("location",locationText.getText().toString());
                String[] hatirlatmaTipiParts = hatirlatmaTipi.split(" ");
                String[] hatirlatmaSayiParts = hatirlatmaSayi.split(" ");
                String[] tekrarTipiParts = tekrarTipi.split(" ");
                String[] tekrarSayiParts = tekrarSayi.split(" ");
                String[] dateParts = dateText.getText().toString().split("-");
                String[] timeParts = timeText.getText().toString().split(":");
                int alarmId=(int)id;
                for(int i = 0; i < 3; i++){
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
                    alarmId=(int)id*3+i;
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), alarmId, intent, 0);
                    alarmManager.cancel(pendingIntent);
                }
                int selectedPosition = categorySpinner.getSelectedItemPosition();
                if(selectedPosition==0){
                    cv.put("category","Görev");
                }
                else if(selectedPosition==1){
                    cv.put("category","Doğumgünü");
                }
                else if(selectedPosition==2){
                    cv.put("category","Toplantı");
                }
                else if(selectedPosition==3){
                    cv.put("category","Buluşma");
                }
                else if(selectedPosition==4){
                    cv.put("category","Ders Çalışma");
                }
                for (int i = 0; i < 3; i++) { //3 alarmın bilgisini yolla
                    if(hatirlatmaSayiParts[i].equals("xxx")==false){//alarm varsa
                        int olayOncesi=Integer.parseInt(hatirlatmaSayiParts[i]);
                        Calendar calendarAlarm=Calendar.getInstance();
                        calendarAlarm.setTimeInMillis(System.currentTimeMillis());
                        calendarAlarm.set(Calendar.DAY_OF_MONTH,Integer.parseInt(dateParts[2]));
                        calendarAlarm.set(Calendar.MONTH,Integer.parseInt(dateParts[1])-1);
                        calendarAlarm.set(Calendar.HOUR_OF_DAY,Integer.parseInt(timeParts[0]));
                        calendarAlarm.set(Calendar.MINUTE,Integer.parseInt(timeParts[1]));
                        calendarAlarm.set(Calendar.SECOND,0);
                        if(hatirlatmaTipiParts[i].equals("dakika"))calendarAlarm.add(Calendar.MINUTE,-olayOncesi);
                        else if(hatirlatmaTipiParts[i].equals("saat"))calendarAlarm.add(Calendar.HOUR_OF_DAY,-olayOncesi);
                        else if(hatirlatmaTipiParts[i].equals("gün"))calendarAlarm.add(Calendar.DAY_OF_MONTH,-olayOncesi);
                        else if(hatirlatmaTipiParts[i].equals("hafta"))calendarAlarm.add(Calendar.DAY_OF_MONTH,-7*olayOncesi);
                        else if(hatirlatmaTipiParts[i].equals("ay"))calendarAlarm.add(Calendar.MONTH,-olayOncesi);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        NotificationCompat.Builder builder;
                        NotificationManager bildirimYoneticisi =
                                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                        Intent showEventIntent =new Intent(getApplicationContext(),ShowEventActivity.class);
                        showEventIntent.putExtra("id",id);
                        alarmId=(int)id*3+i;
                        PendingIntent gidilecekIntent = PendingIntent.getActivity(getApplicationContext(),alarmId,showEventIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                        String kanalId = "kanalId";
                        String kanalAd = "kanalAd";
                        String kanalTanım = "kanalTanım";int kanalOnceligi = NotificationManager.IMPORTANCE_LOW;
                        NotificationChannel kanal = bildirimYoneticisi.getNotificationChannel(kanalId);
                        if (kanal == null) {
                            kanal = new NotificationChannel(kanalId, kanalAd, kanalOnceligi);
                            kanal.setDescription(kanalTanım);
                            bildirimYoneticisi.createNotificationChannel(kanal);
                        }
                        builder = new NotificationCompat.Builder(getApplicationContext(), kanalId);
                        builder.setContentTitle(eventNameText.getText().toString())
                                .setContentText(eventDetailsText.getText().toString())
                                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                                .setAutoCancel(true)
                                .setContentIntent(gidilecekIntent);

                        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
                        intent.putExtra("nesne",builder.build());
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), alarmId, intent, 0);
                        if(tekrarSayiParts[i].equals("xxx")==false)   {//tekrarlı alarm
                            int tekrarSikligi=Integer.parseInt(tekrarSayiParts[i]);
                            tekrarSikligi*=1000;
                            if(tekrarTipiParts[i].equals("saat"))tekrarSikligi*=3600;
                            else if(tekrarTipiParts[i].equals("gün"))tekrarSikligi*=86400;
                            else if(tekrarTipiParts[i].equals("hafta"))tekrarSikligi*=604800;
                            else if(tekrarTipiParts[i].equals("ay"))tekrarSikligi*=2592000;
                            else if(tekrarTipiParts[i].equals("yıl"))tekrarSikligi*=31536000;
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendarAlarm.getTimeInMillis(), tekrarSikligi, pendingIntent);
                        }
                        else if(tekrarSayiParts[i].equals("xxx"))
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendarAlarm.getTimeInMillis(),pendingIntent);
                    }
                }

                mDatabase.update("events", cv, "id = ?",
                        new String[] { String.valueOf(id) });
                Snackbar.make(v,"Etkinlik güncellendi",Snackbar.LENGTH_SHORT).show();
            }
        });

        eventDelete.setOnClickListener(new View.OnClickListener() {  //DÜZENLE SADECE KOPYALADIM
            @Override
            public void onClick(View v) {
                mDatabase.delete("events", "id = ?",
                        new String[] { String.valueOf(id) });
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
                for(int i = 0; i < 3; i++){
                    int alarmId=(int)id*3+i;
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), alarmId, intent, 0);
                    alarmManager.cancel(pendingIntent);
                }
                Snackbar.make(v,"Etkinlik silindi",Snackbar.LENGTH_SHORT).show();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        finish();
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 2000);
            }
        });
        eventShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder();
                sb.append("Etkinlik adı: ");
                sb.append(eventNameText.getText().toString());
                sb.append("\nEtkinlik detayı: ");
                sb.append(eventDetailsText.getText().toString());
                sb.append("\nEtkinlik tarihi: ");
                sb.append(dateText.getText().toString());
                sb.append("\nEtkinlik saati: ");
                sb.append(timeText.getText().toString());
                sb.append("\nEtkinlik konumu: ");
                sb.append(locationText.getText().toString());
                String message=sb.toString();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                Snackbar.make(v,"Etkinlik paylaşıldı",Snackbar.LENGTH_SHORT).show();

            }
        });
    }
    private Cursor getAllItemsById(long id) {
        Database dbHelper = new Database(getApplicationContext());
        mDatabase = dbHelper.getWritableDatabase();
        String whereClause = "id = ?";
        String[] whereArgs = new String[] {
                String.valueOf(id)
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
                latitude=data.getDoubleExtra("latitude",0);
                longitude=data.getDoubleExtra("longitude",0);
                String konum=String.format("http://maps.google.com/?q=%f,%f",latitude,longitude);
                locationText.setText(konum);
            }
        }
        if (requestCode == 1001) {
            if (resultCode == RESULT_OK) {
                String TAG = "MainActivityasdasd";
                tekrarSayi=data.getStringExtra("tekrarSayi");
                tekrarTipi=data.getStringExtra("tekrarTipi");
                hatirlatmaSayi=data.getStringExtra("hatirlatmaSayi");
                hatirlatmaTipi=data.getStringExtra("hatirlatmaTipi");
            }
        }
    }
}
