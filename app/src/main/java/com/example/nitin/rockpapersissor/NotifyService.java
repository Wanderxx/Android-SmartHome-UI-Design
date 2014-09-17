package com.example.nitin.rockpapersissor;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class NotifyService extends Service {
    public NotifyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       // String message=intent.getExtras().getString(Intent.EXTRA_TEXT);
        //sendMessage(message);
        sendMessage("Hard coded message");
        Log.d("debug","Service started");
//        stopSelf();
        return START_STICKY;

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    private void sendMessage(String message)
    {
        Toast.makeText(getApplication(),message,Toast.LENGTH_LONG).show();

    }
}
