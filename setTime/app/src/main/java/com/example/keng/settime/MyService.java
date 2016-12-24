package com.example.keng.settime;

import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e("Start :","service");
        Log.e("Build:",intent.getStringExtra("id"));
        //SETUP RINGTONE
        RingtoneManager Ring_manager=new RingtoneManager(this);
        //get the uri of sound
        Uri uri=Ring_manager.getActualDefaultRingtoneUri(this,RingtoneManager.TYPE_ALARM);
        //set up ringtone
        Ringtone ringtone = Ring_manager.getRingtone(getApplicationContext(),uri);
        //play sound!
        ringtone.play();
        return START_NOT_STICKY;
    }
}
