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

public class EventActivity extends AppCompatActivity {
    EditText dateText,timeText,eventNameText,eventDetailsText;
    Spinner categorySpinner;
    Button eventAdd,eventShare;
    ArrayList<String> categories = new ArrayList<>();
    ArrayAdapter<String> categoryAdapter;
    private SQLiteDatabase mDatabase;
    private EventAdapter mAdapter;
    ImageView mapsButton,alarmButton;
    Double latitude,longitude;
    TextView locationText;
    String hatirlatmaTipi,hatirlatmaSayi,tekrarTipi,tekrarSayi;
    int alarmId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        int day=getIntent().getIntExtra("gun",1);
        int month=getIntent().getIntExtra("ay",1);
        month+=1;
        int year=getIntent().getIntExtra("yil",2020);
        View background=findViewById(R.id.background);
        final SharedPreferences sp = getSharedPreferences("ayarlar", MODE_PRIVATE);
        String app_theme = sp.getString("app-theme", "light");
        if(app_theme.equals("light")){
            background.setBackgroundColor(Color.WHITE);
        }
        else{
            background.setBackgroundColor(0xFF383131);
        }
        hatirlatmaSayi="xxx xxx xxx xxx";
        hatirlatmaTipi="xxx xxx xxx xxx";
        tekrarSayi="xxx xxx xxx xxx xxx";
        tekrarTipi="xxx xxx xxx xxx";
        timeText =findViewById(R.id.eventTime_Text);
        dateText =findViewById(R.id.eventDate_Text);
        dateText.setText( String.format("%04d-%02d-%02d",year,month,day));
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        timeText.setText( String.format("%02d:%02d", hour, minute));
        eventNameText=findViewById(R.id.eventName_Text);
        eventDetailsText=findViewById(R.id.eventDetails_Text);
        categorySpinner=findViewById(R.id.category_spinner);
        eventAdd=findViewById(R.id.eventAdd_button);
        eventShare=findViewById(R.id.eventShare_button);
        categories.add("Görev");
        categories.add("Doğumgünü");
        categories.add("Toplantı");
        categories.add("Buluşma");
        categories.add("Ders Çalışma");
        categoryAdapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1, categories);
        categorySpinner.setAdapter(categoryAdapter);
        locationText=findViewById(R.id.location_Text);
        alarmButton=findViewById(R.id.alarmView);
        mapsButton=findViewById(R.id.mapsView_Button);
        mapsButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivityForResult(intent, 1000);
            }
        });
        alarmButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), RemainderActivity.class);
                intent.putExtra("yeniMi","evet");//yeni kayıt
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
                timePicker = new TimePickerDialog(EventActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                datePicker = new DatePickerDialog(EventActivity.this, new DatePickerDialog.OnDateSetListener() {

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


        eventAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database dbHelper = new Database(EventActivity.this);
                mDatabase = dbHelper.getWritableDatabase();
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
                categories.add("Görev");
                categories.add("Doğumgünü");
                categories.add("Toplantı");
                categories.add("Buluşma");
                categories.add("Ders Çalışma");
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
                long id=mDatabase.insert("events", null, cv);
                Snackbar.make(v,"Etkinlik eklendi",Snackbar.LENGTH_SHORT).show();

                String[] hatirlatmaTipiParts = hatirlatmaTipi.split(" ");
                String[] hatirlatmaSayiParts = hatirlatmaSayi.split(" ");
                String[] tekrarTipiParts = tekrarTipi.split(" ");
                String[] tekrarSayiParts = tekrarSayi.split(" ");
                String[] dateParts = dateText.getText().toString().split("-");
                String[] timeParts = timeText.getText().toString().split(":");
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
