package com.example.keng.settime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyBroadcastRecv extends BroadcastReceiver {
    public MyBroadcastRecv() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.e("Build:",intent.getStringExtra("id"));
        Intent it=new Intent(context,MyService.class);
        it.putExtra("id",intent.getStringExtra("id"));
        context.startService(it);
    }
}
