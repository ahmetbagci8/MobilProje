package com.example.mobilproje;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class RemainderActivity extends AppCompatActivity {
    EditText tekrarSayiText1,hatirlatmaSayiText1,tekrarSayiText2,hatirlatmaSayiText2,tekrarSayiText3,hatirlatmaSayiText3;
    Spinner hatirlatmaTipSpinner1,tekrarTipSpinner1,hatirlatmaTipSpinner2,tekrarTipSpinner2,hatirlatmaTipSpinner3,tekrarTipSpinner3;
    Button remainderUpdateButton;
    CheckBox checkboxAlarm1,checkboxTekrar1,checkboxAlarm2,checkboxTekrar2,checkboxAlarm3,checkboxTekrar3;
    ArrayList<String> hatirlatmaTipler = new ArrayList<>();
    ArrayAdapter<String> hatirlatmaAdapter;
    ArrayList<String> tekrarTipler = new ArrayList<>();
    ArrayAdapter<String> tekrarAdapter;
    String hatirlatmaTipi1,hatirlatmaTipi2,hatirlatmaTipi3,hatirlatmaTipi;
    String hatirlatmaSayi1,hatirlatmaSayi2,hatirlatmaSayi3,hatirlatmaSayi;
    String tekrarTipi1,tekrarTipi2,tekrarTipi3,tekrarTipi;
    String tekrarSayi1,tekrarSayi2,tekrarSayi3,tekrarSayi;
    private SQLiteDatabase mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remainder);
        View background=findViewById(R.id.background);
        final SharedPreferences sp = getSharedPreferences("ayarlar", MODE_PRIVATE);
        String app_theme = sp.getString("app-theme", "light");
        String hatirlatmaSayiSp = sp.getString("hatirlatmaSayi", "1");
        String hatirlatmaTipSp = sp.getString("hatirlatmaTipi", "saat");
        String tekrarSayiSp = sp.getString("tekrarSayi", "1");
        String tekrarTipSp = sp.getString("tekrarTipi", "gün");
        if(app_theme.equals("light")){
            background.setBackgroundColor(Color.WHITE);
        }
        else{
            background.setBackgroundColor(0xFF383131);
        }

        tekrarSayiText1=findViewById(R.id.tekrarSayi_Text1);
        hatirlatmaSayiText1=findViewById(R.id.hatirlatmaSayi_Text1);
        hatirlatmaTipSpinner1=findViewById(R.id.hatirlatmaTip_spinner1);
        tekrarTipSpinner1=findViewById(R.id.tekrarTip_spinner1);
        tekrarSayiText2=findViewById(R.id.tekrarSayi_Text2);
        hatirlatmaSayiText2=findViewById(R.id.hatirlatmaSayi_Text2);
        hatirlatmaTipSpinner2=findViewById(R.id.hatirlatmaTip_spinner2);
        tekrarTipSpinner2=findViewById(R.id.tekrarTip_spinner2);
        tekrarSayiText3=findViewById(R.id.tekrarSayi_Text3);
        hatirlatmaSayiText3=findViewById(R.id.hatirlatmaSayi_Text3);
        hatirlatmaTipSpinner3=findViewById(R.id.hatirlatmaTip_spinner3);
        tekrarTipSpinner3=findViewById(R.id.tekrarTip_spinner3);
        remainderUpdateButton=findViewById(R.id.remainderUpdate_button);
        hatirlatmaSayiText1.setText(hatirlatmaSayiSp);
        hatirlatmaSayiText2.setText(hatirlatmaSayiSp);
        hatirlatmaSayiText3.setText(hatirlatmaSayiSp);
        tekrarSayiText1.setText(tekrarSayiSp);
        tekrarSayiText2.setText(tekrarSayiSp);
        tekrarSayiText3.setText(tekrarSayiSp);
        checkboxAlarm1=findViewById(R.id.checkBox1);
        checkboxTekrar1=findViewById(R.id.checkBox2);
        checkboxAlarm2=findViewById(R.id.checkBox3);
        checkboxTekrar2=findViewById(R.id.checkBox4);
        checkboxAlarm3=findViewById(R.id.checkBox5);
        checkboxTekrar3=findViewById(R.id.checkBox6);
        hatirlatmaTipler.add("Dakika önce");
        hatirlatmaTipler.add("Saat önce");
        hatirlatmaTipler.add("Gün önce");
        hatirlatmaTipler.add("Hafta önce");
        hatirlatmaTipler.add("Ay önce");
        hatirlatmaAdapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1, hatirlatmaTipler);
        hatirlatmaTipSpinner1.setAdapter(hatirlatmaAdapter);

        tekrarTipler.add("Saatte bir");
        tekrarTipler.add("Günde bir");
        tekrarTipler.add("Haftada bir");
        tekrarTipler.add("Ayda bir");
        tekrarTipler.add("Yılda bir");
        tekrarAdapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1, tekrarTipler);
        tekrarTipSpinner1.setAdapter(tekrarAdapter);
        hatirlatmaTipSpinner2.setAdapter(hatirlatmaAdapter);
        tekrarTipSpinner2.setAdapter(tekrarAdapter);
        hatirlatmaTipSpinner3.setAdapter(hatirlatmaAdapter);
        tekrarTipSpinner3.setAdapter(tekrarAdapter);
        if(tekrarTipSp.equals("saat")) tekrarTipSpinner1.setSelection(0);
        else if(tekrarTipSp.equals("gün")) tekrarTipSpinner1.setSelection(1);
        else if(tekrarTipSp.equals("hafta")) tekrarTipSpinner1.setSelection(2);
        else if(tekrarTipSp.equals("ay")) tekrarTipSpinner1.setSelection(3);
        else if(tekrarTipSp.equals("yıl")) tekrarTipSpinner1.setSelection(4);
        if(tekrarTipSp.equals("saat")) tekrarTipSpinner2.setSelection(0);
        else if(tekrarTipSp.equals("gün")) tekrarTipSpinner2.setSelection(1);
        else if(tekrarTipSp.equals("hafta")) tekrarTipSpinner2.setSelection(2);
        else if(tekrarTipSp.equals("ay")) tekrarTipSpinner2.setSelection(3);
        else if(tekrarTipSp.equals("yıl")) tekrarTipSpinner2.setSelection(4);
        if(tekrarTipSp.equals("saat")) tekrarTipSpinner3.setSelection(0);
        else if(tekrarTipSp.equals("gün")) tekrarTipSpinner3.setSelection(1);
        else if(tekrarTipSp.equals("hafta")) tekrarTipSpinner3.setSelection(2);
        else if(tekrarTipSp.equals("ay")) tekrarTipSpinner3.setSelection(3);
        else if(tekrarTipSp.equals("yıl")) tekrarTipSpinner3.setSelection(4);
        if(hatirlatmaTipSp.equals("dakika")) hatirlatmaTipSpinner1.setSelection(0);
        else if(hatirlatmaTipSp.equals("saat")) hatirlatmaTipSpinner1.setSelection(1);
        else if(hatirlatmaTipSp.equals("gün")) hatirlatmaTipSpinner1.setSelection(2);
        else if(hatirlatmaTipSp.equals("hafta")) hatirlatmaTipSpinner1.setSelection(3);
        else if(hatirlatmaTipSp.equals("ay")) hatirlatmaTipSpinner1.setSelection(4);
        if(hatirlatmaTipSp.equals("dakika")) hatirlatmaTipSpinner2.setSelection(0);
        else if(hatirlatmaTipSp.equals("saat")) hatirlatmaTipSpinner2.setSelection(1);
        else if(hatirlatmaTipSp.equals("gün")) hatirlatmaTipSpinner2.setSelection(2);
        else if(hatirlatmaTipSp.equals("hafta")) hatirlatmaTipSpinner2.setSelection(3);
        else if(hatirlatmaTipSp.equals("ay")) hatirlatmaTipSpinner2.setSelection(4);
        if(hatirlatmaTipSp.equals("dakika")) hatirlatmaTipSpinner3.setSelection(0);
        else if(hatirlatmaTipSp.equals("saat")) hatirlatmaTipSpinner3.setSelection(1);
        else if(hatirlatmaTipSp.equals("gün")) hatirlatmaTipSpinner3.setSelection(2);
        else if(hatirlatmaTipSp.equals("hafta")) hatirlatmaTipSpinner3.setSelection(3);
        else if(hatirlatmaTipSp.equals("ay")) hatirlatmaTipSpinner3.setSelection(4);

        String yeniMi=getIntent().getStringExtra("yeniMi");
        if(yeniMi.equals("hayir")){
            hatirlatmaSayi=getIntent().getStringExtra("hatirlatmaSayi");
            hatirlatmaTipi=getIntent().getStringExtra("hatirlatmaTipi");
            tekrarSayi=getIntent().getStringExtra("tekrarSayi");
            tekrarTipi=getIntent().getStringExtra("tekrarTipi");
            String[] hatirlatmaTipiParts = hatirlatmaTipi.split(" ");
            String[] hatirlatmaSayiParts = hatirlatmaSayi.split(" ");
            String[] tekrarTipiParts = tekrarTipi.split(" ");
            String[] tekrarSayiParts = tekrarSayi.split(" ");

            if(hatirlatmaSayiParts[0].equals("xxx")){
                checkboxAlarm1.setChecked(false);
                checkboxTekrar1.setChecked(false);
            }
            else{
                checkboxAlarm1.setChecked(true);
                hatirlatmaSayiText1.setText(hatirlatmaSayiParts[0]);
                if(hatirlatmaTipiParts[0].equals("dakika")) hatirlatmaTipSpinner1.setSelection(0);
                else if(hatirlatmaTipiParts[0].equals("saat")) hatirlatmaTipSpinner1.setSelection(1);
                else if(hatirlatmaTipiParts[0].equals("gün")) hatirlatmaTipSpinner1.setSelection(2);
                else if(hatirlatmaTipiParts[0].equals("hafta")) hatirlatmaTipSpinner1.setSelection(3);
                else if(hatirlatmaTipiParts[0].equals("ay")) hatirlatmaTipSpinner1.setSelection(4);
                if(tekrarSayiParts[0].equals("xxx")){
                    checkboxTekrar1.setChecked(false);
                }
                else{
                    checkboxTekrar1.setChecked(true);
                    tekrarSayiText1.setText(tekrarSayiParts[0]);
                    if(tekrarTipiParts[0].equals("saat")) tekrarTipSpinner1.setSelection(0);
                    else if(tekrarTipiParts[0].equals("gün")) tekrarTipSpinner1.setSelection(1);
                    else if(tekrarTipiParts[0].equals("hafta")) tekrarTipSpinner1.setSelection(2);
                    else if(tekrarTipiParts[0].equals("ay")) tekrarTipSpinner1.setSelection(3);
                    else if(tekrarTipiParts[0].equals("yıl")) tekrarTipSpinner1.setSelection(4);
                }
            }


            if(hatirlatmaSayiParts[1].equals("xxx")){
                checkboxAlarm2.setChecked(false);
                checkboxTekrar2.setChecked(false);
            }
            else{
                checkboxAlarm2.setChecked(true);
                hatirlatmaSayiText2.setText(hatirlatmaSayiParts[1]);
                if(hatirlatmaTipiParts[1].equals("dakika")) hatirlatmaTipSpinner2.setSelection(0);
                else if(hatirlatmaTipiParts[1].equals("saat")) hatirlatmaTipSpinner2.setSelection(1);
                else if(hatirlatmaTipiParts[1].equals("gün")) hatirlatmaTipSpinner2.setSelection(2);
                else if(hatirlatmaTipiParts[1].equals("hafta")) hatirlatmaTipSpinner2.setSelection(3);
                else if(hatirlatmaTipiParts[1].equals("ay")) hatirlatmaTipSpinner2.setSelection(4);
                if(tekrarSayiParts[1].equals("xxx")){
                    checkboxTekrar2.setChecked(false);
                }
                else{
                    checkboxTekrar2.setChecked(true);
                    tekrarSayiText2.setText(tekrarSayiParts[1]);
                    if(tekrarTipiParts[1].equals("saat")) tekrarTipSpinner2.setSelection(0);
                    else if(tekrarTipiParts[1].equals("gün")) tekrarTipSpinner2.setSelection(1);
                    else if(tekrarTipiParts[1].equals("hafta")) tekrarTipSpinner2.setSelection(2);
                    else if(tekrarTipiParts[1].equals("ay")) tekrarTipSpinner2.setSelection(3);
                    else if(tekrarTipiParts[1].equals("yıl")) tekrarTipSpinner2.setSelection(4);
                }
            }
            if(hatirlatmaSayiParts[2].equals("xxx")){
                checkboxAlarm3.setChecked(false);
                checkboxTekrar3.setChecked(false);
            }
            else{
                checkboxAlarm3.setChecked(true);
                hatirlatmaSayiText3.setText(hatirlatmaSayiParts[2]);
                if(hatirlatmaTipiParts[2].equals("dakika")) hatirlatmaTipSpinner3.setSelection(0);
                else if(hatirlatmaTipiParts[2].equals("saat")) hatirlatmaTipSpinner3.setSelection(1);
                else if(hatirlatmaTipiParts[2].equals("gün")) hatirlatmaTipSpinner3.setSelection(2);
                else if(hatirlatmaTipiParts[2].equals("hafta")) hatirlatmaTipSpinner3.setSelection(3);
                else if(hatirlatmaTipiParts[2].equals("ay")) hatirlatmaTipSpinner3.setSelection(4);
                if(tekrarSayiParts[2].equals("xxx")){
                    checkboxTekrar3.setChecked(false);
                }
                else{
                    checkboxTekrar3.setChecked(true);
                    tekrarSayiText3.setText(tekrarSayiParts[2]);
                    if(tekrarTipiParts[2].equals("saat")) tekrarTipSpinner3.setSelection(0);
                    else if(tekrarTipiParts[2].equals("gün")) tekrarTipSpinner3.setSelection(1);
                    else if(tekrarTipiParts[2].equals("hafta")) tekrarTipSpinner3.setSelection(2);
                    else if(tekrarTipiParts[2].equals("ay")) tekrarTipSpinner3.setSelection(3);
                    else if(tekrarTipiParts[2].equals("yıl")) tekrarTipSpinner3.setSelection(4);
                }
            }
        }
        remainderUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkboxAlarm1.isChecked()==false){
                    hatirlatmaSayi1="xxx";
                    hatirlatmaTipi1="xxx";
                    tekrarSayi1="xxx";
                    tekrarTipi1="xxx";
                }
                else if(checkboxAlarm1.isChecked()==true){
                    if(hatirlatmaSayiText1.getText().toString().equals("")) hatirlatmaSayiText1.setText("0");
                    if(tekrarSayiText1.getText().toString().equals("")) tekrarSayiText1.setText("0");
                    hatirlatmaSayi1=hatirlatmaSayiText1.getText().toString();
                    int selectedPosition2 = hatirlatmaTipSpinner1.getSelectedItemPosition();
                    if(selectedPosition2==0) hatirlatmaTipi1="dakika";
                    else if(selectedPosition2==1) hatirlatmaTipi1="saat";
                    else if(selectedPosition2==2) hatirlatmaTipi1="gün";
                    else if(selectedPosition2==3) hatirlatmaTipi1="hafta";
                    else if(selectedPosition2==4) hatirlatmaTipi1="ay";
                    if(checkboxTekrar1.isChecked()==false){
                        tekrarSayi1="xxx";
                        tekrarTipi1="xxx";
                    }
                    else if (checkboxTekrar1.isChecked()==true){
                        tekrarSayi1=tekrarSayiText1.getText().toString();
                        int selectedPosition3 = tekrarTipSpinner1.getSelectedItemPosition();
                        if(selectedPosition3==0) tekrarTipi1="saat";
                        else if(selectedPosition3==1) tekrarTipi1="gün";
                        else if(selectedPosition3==2) tekrarTipi1="hafta";
                        else if(selectedPosition3==3) tekrarTipi1="ay";
                        else if(selectedPosition3==4) tekrarTipi1="yıl";
                    }
                }
                if(checkboxAlarm2.isChecked()==false){
                    hatirlatmaSayi2="xxx";
                    hatirlatmaTipi2="xxx";
                    tekrarSayi2="xxx";
                    tekrarTipi2="xxx";
                }
                else if(checkboxAlarm2.isChecked()==true){
                    if(hatirlatmaSayiText2.getText().toString().equals("")) hatirlatmaSayiText2.setText("0");
                    if(tekrarSayiText2.getText().toString().equals("")) tekrarSayiText2.setText("0");
                    hatirlatmaSayi2=hatirlatmaSayiText2.getText().toString();
                    int selectedPosition2 = hatirlatmaTipSpinner2.getSelectedItemPosition();
                    if(selectedPosition2==0) hatirlatmaTipi2="dakika";
                    else if(selectedPosition2==1) hatirlatmaTipi2="saat";
                    else if(selectedPosition2==2) hatirlatmaTipi2="gün";
                    else if(selectedPosition2==3) hatirlatmaTipi2="hafta";
                    else if(selectedPosition2==4) hatirlatmaTipi2="ay";
                    if(checkboxTekrar2.isChecked()==false){
                        tekrarSayi2="xxx";
                        tekrarTipi2="xxx";
                    }
                    else if (checkboxTekrar2.isChecked()==true){
                        tekrarSayi2=tekrarSayiText2.getText().toString();
                        int selectedPosition3 = tekrarTipSpinner2.getSelectedItemPosition();
                        if(selectedPosition3==0) tekrarTipi2="saat";
                        else if(selectedPosition3==1) tekrarTipi2="gün";
                        else if(selectedPosition3==2) tekrarTipi2="hafta";
                        else if(selectedPosition3==3) tekrarTipi2="ay";
                        else if(selectedPosition3==4) tekrarTipi2="yıl";
                    }
                }
                if(checkboxAlarm3.isChecked()==false){
                    hatirlatmaSayi3="xxx";
                    hatirlatmaTipi3="xxx";
                    tekrarSayi3="xxx";
                    tekrarTipi3="xxx";
                }
                else if(checkboxAlarm3.isChecked()==true){
                    if(hatirlatmaSayiText3.getText().toString().equals("")) hatirlatmaSayiText3.setText("0");
                    if(tekrarSayiText3.getText().toString().equals("")) tekrarSayiText3.setText("0");
                    hatirlatmaSayi3=hatirlatmaSayiText3.getText().toString();
                    int selectedPosition2 = hatirlatmaTipSpinner3.getSelectedItemPosition();
                    if(selectedPosition2==0) hatirlatmaTipi3="dakika";
                    else if(selectedPosition2==1) hatirlatmaTipi3="saat";
                    else if(selectedPosition2==2) hatirlatmaTipi3="gün";
                    else if(selectedPosition2==3) hatirlatmaTipi3="hafta";
                    else if(selectedPosition2==4) hatirlatmaTipi3="ay";
                    if(checkboxTekrar1.isChecked()==false){
                        tekrarSayi3="xxx";
                        tekrarTipi3="xxx";
                    }
                    else if (checkboxTekrar3.isChecked()==true){
                        tekrarSayi3=tekrarSayiText3.getText().toString();
                        int selectedPosition3 = tekrarTipSpinner3.getSelectedItemPosition();
                        if(selectedPosition3==0) tekrarTipi3="saat";
                        else if(selectedPosition3==1) tekrarTipi3="gün";
                        else if(selectedPosition3==2) tekrarTipi3="hafta";
                        else if(selectedPosition3==3) tekrarTipi3="ay";
                        else if(selectedPosition3==4) tekrarTipi3="yıl";
                    }
                }
                tekrarSayi=tekrarSayi1+" "+tekrarSayi2+" "+tekrarSayi3;
                hatirlatmaSayi=hatirlatmaSayi1+" "+hatirlatmaSayi2+" "+hatirlatmaSayi3;
                tekrarTipi=tekrarTipi1+" "+tekrarTipi2+" "+tekrarTipi3;
                hatirlatmaTipi=hatirlatmaTipi1+" "+hatirlatmaTipi2+" "+hatirlatmaTipi3;
                Intent intent = new Intent();
                intent.putExtra("tekrarSayi", tekrarSayi);
                intent.putExtra("hatirlatmaSayi", hatirlatmaSayi);
                intent.putExtra("tekrarTipi", tekrarTipi);
                intent.putExtra("hatirlatmaTipi", hatirlatmaTipi);
                setResult(RESULT_OK, intent);
                Snackbar.make(v,"Hatırlatıcı ayarlandı",Snackbar.LENGTH_SHORT).show();
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
}
