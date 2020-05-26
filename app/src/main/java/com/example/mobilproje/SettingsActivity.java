package com.example.mobilproje;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    ArrayList<String> hatirlatmaTipler = new ArrayList<>();
    ArrayAdapter<String> hatirlatmaAdapter;
    ArrayList<String> tekrarTipler = new ArrayList<>();
    ArrayAdapter<String> tekrarAdapter;
    ArrayList<String> ringtones = new ArrayList<>();
    ArrayAdapter<String> ringtonesAdapter;
    Spinner hatirlatmaTipSpinner,tekrarTipSpinner,ringtoneSpinner;
    EditText tekrarSayiText,hatirlatmaSayiText;
    Switch darkModeSwitch;
    Button saveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final SharedPreferences sp = getSharedPreferences("ayarlar", MODE_PRIVATE);
        String ringtone = sp.getString("ringtone", "goeswithoutsaying");
        String hatirlatmaSayi = sp.getString("hatirlatmaSayi", "1");
        String hatirlatmaTip = sp.getString("hatirlatmaTipi", "saat");
        String tekrarSayi = sp.getString("tekrarSayi", "1");
        String tekrarTip = sp.getString("tekrarTipi", "gün");
        String app_theme = sp.getString("app-theme", "light");
        tekrarSayiText=findViewById(R.id.tekrarSayi_Text);
        hatirlatmaSayiText=findViewById(R.id.hatirlatmaSayi_Text);
        ringtoneSpinner=findViewById(R.id.ringtone_spinner);
        ringtones.add("goeswithoutsaying");
        ringtones.add("gotitdone");
        ringtones.add("hastybadumtss");
        ringtones.add("juntos");
        ringtones.add("pieceofcake");
        ringtones.add("swiftly");
        ringtonesAdapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1, ringtones);
        ringtoneSpinner.setAdapter(ringtonesAdapter);
        if(ringtone.equals("goeswithoutsaying")){
            ringtoneSpinner.setSelection(0);
        }
        else if(ringtone.equals("gotitdone")){
            ringtoneSpinner.setSelection(1);
        }
        else if(ringtone.equals("hastybadumtss")){
            ringtoneSpinner.setSelection(2);
        }
        else if(ringtone.equals("juntos")){
            ringtoneSpinner.setSelection(3);
        }
        else if(ringtone.equals("pieceofcake")){
            ringtoneSpinner.setSelection(4);
        }
        else if(ringtone.equals("swiftly")){
            ringtoneSpinner.setSelection(5);
        }
        hatirlatmaTipSpinner=findViewById(R.id.hatirlatmaTip_spinner);
        tekrarTipSpinner=findViewById(R.id.tekrarTip_spinner);
        hatirlatmaTipler.add("Dakika önce");
        hatirlatmaTipler.add("Saat önce");
        hatirlatmaTipler.add("Gün önce");
        hatirlatmaTipler.add("Hafta önce");
        hatirlatmaTipler.add("Ay önce");
        hatirlatmaAdapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1, hatirlatmaTipler);
        hatirlatmaTipSpinner.setAdapter(hatirlatmaAdapter);
        hatirlatmaSayiText.setText(hatirlatmaSayi);
        if(hatirlatmaTip.equals("dakika")){
            hatirlatmaTipSpinner.setSelection(0);
        }
        else if(hatirlatmaTip.equals("saat")){
            hatirlatmaTipSpinner.setSelection(1);
        }
        else if(hatirlatmaTip.equals("gün")){
            hatirlatmaTipSpinner.setSelection(2);
        }
        else if(hatirlatmaTip.equals("hafta")){
            hatirlatmaTipSpinner.setSelection(3);
        }
        else if(hatirlatmaTip.equals("ay")){
            hatirlatmaTipSpinner.setSelection(4);
        }
        tekrarSayiText.setText(tekrarSayi);
        tekrarTipler.add("Saatte bir");
        tekrarTipler.add("Günde bir");
        tekrarTipler.add("Haftada bir");
        tekrarTipler.add("Ayda bir");
        tekrarTipler.add("Yılda bir");
        tekrarAdapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1, tekrarTipler);
        tekrarTipSpinner.setAdapter(tekrarAdapter);

        if(tekrarTip.equals("saat")){
            tekrarTipSpinner.setSelection(0);
        }
        else if(tekrarTip.equals("gün")){
            tekrarTipSpinner.setSelection(1);
        }
        else if(tekrarTip.equals("hafta")){
            tekrarTipSpinner.setSelection(2);
        }
        else if(tekrarTip.equals("ay")){
            tekrarTipSpinner.setSelection(3);
        }
        else if(tekrarTip.equals("yıl")){
            tekrarTipSpinner.setSelection(4);
        }
        darkModeSwitch=findViewById(R.id.darkModeSwitch);
        View background=findViewById(R.id.background);
        if(app_theme.equals("light")){
            darkModeSwitch.setChecked(false);
            background.setBackgroundColor(Color.WHITE);
        }
        else{
            darkModeSwitch.setChecked(true);
            background.setBackgroundColor(0xFF383131);
        }
        saveButton=findViewById(R.id.save_Button);
        saveButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                SharedPreferences.Editor editor = sp.edit();


                int selectedPosition = ringtoneSpinner.getSelectedItemPosition();
                if(selectedPosition==0){
                    editor.putString("ringtone","goeswithoutsaying");
                }
                else if(selectedPosition==1){
                    editor.putString("ringtone","gotitdone");
                }
                else if(selectedPosition==2){
                    editor.putString("ringtone","hastybadumtss");
                }
                else if(selectedPosition==3){
                    editor.putString("ringtone","juntos");
                }
                else if(selectedPosition==4){
                    editor.putString("ringtone","pieceofcake");
                }
                else if(selectedPosition==5){
                    editor.putString("ringtone","swiftly");
                }

                int selectedPosition2 = hatirlatmaTipSpinner.getSelectedItemPosition();
                if(selectedPosition2==0){
                    editor.putString("hatirlatmaTipi","dakika");
                }
                else if(selectedPosition2==1){
                    editor.putString("hatirlatmaTipi","saat");
                }
                else if(selectedPosition2==2){
                    editor.putString("hatirlatmaTipi","gün");
                }
                else if(selectedPosition2==3){
                    editor.putString("hatirlatmaTipi","hafta");
                }
                else if(selectedPosition2==4){
                    editor.putString("hatirlatmaTipi","ay");
                }

                int selectedPosition3 = tekrarTipSpinner.getSelectedItemPosition();
                if(selectedPosition3==0){
                    editor.putString("tekrarTipi","saat");
                }
                else if(selectedPosition3==1){
                    editor.putString("tekrarTipi","gün");
                }
                else if(selectedPosition3==2){
                    editor.putString("tekrarTipi","hafta");
                }
                else if(selectedPosition3==3){
                    editor.putString("tekrarTipi","ay");
                }
                else if(selectedPosition3==4){
                    editor.putString("tekrarTipi","yıl");
                }

                if(darkModeSwitch.isChecked()==true){
                    editor.putString("app-theme","dark");
                    //sensorView.setBackgroundColor(0xFF383131);
                }
                else{
                    editor.putString("app-theme","light");
                    //sensorView.setBackgroundColor(Color.WHITE);
                }
                editor.putString("hatirlatmaSayi",hatirlatmaSayiText.getText().toString());
                editor.putString("tekrarSayi",tekrarSayiText.getText().toString());
                editor.commit();
                Snackbar.make(v,"Ayarlar kaydedildi",Snackbar.LENGTH_SHORT).show();
            }
        });


    }
}
