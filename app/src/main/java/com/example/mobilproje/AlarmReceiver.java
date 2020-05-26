package com.example.mobilproje;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class AlarmReceiver extends BroadcastReceiver {
    NotificationManager bildirimYoneticisi;
    Vibrator titresim;
    Notification bildirim;
    @Override
    public void onReceive(Context context, Intent intent) {
        final SharedPreferences sp = context.getSharedPreferences("ayarlar", context.MODE_PRIVATE);
        String song = sp.getString("song", "goeswithoutsaying");
        bildirimYoneticisi = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        titresim = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        titresim.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
        bildirim = intent.getParcelableExtra("nesne");
        bildirimYoneticisi.notify(1, bildirim);
        Toast.makeText(context, "Alarm çalıyor", Toast.LENGTH_LONG).show();
        int id=R.raw.goeswithoutsaying;
        if(song.equals("goeswithoutsaying")) id=R.raw.goeswithoutsaying;
        else if(song.equals("gotitdone"))id=R.raw.gotitdone;
        else if(song.equals("hastybadumtss"))id=R.raw.hastybadumtss;
        else if(song.equals("juntos"))id=R.raw.juntos;
        else if(song.equals("pieceofcake"))id=R.raw.pieceofcake;
        else if(song.equals("swiftly"))id=R.raw.swiftly;
        final Ringtone ringtone=RingtoneManager.getRingtone(context,Uri.parse("android.resource://com.example.mobilproje/" + id));
        ringtone.play();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ringtone.stop();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 10000);

    }
}
