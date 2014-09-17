package com.example.nitin.rockpapersissor;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class NotificationSenderActivity extends ActionBarActivity {

   public static NotificationManager notificationManager=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_sender);
        notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.notification_sender, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {


        public  EditText title=null;
        public  EditText subject=null;
        public  EditText message=null;


        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_notification_sender, container, false);
            Button sendNotification=(Button)rootView.findViewById(R.id.button_notify_send);
            title= (EditText)rootView.findViewById(R.id.editText_title_notify);
         final   EditText subject= (EditText)rootView.findViewById(R.id.editText_subject_notify);
        final            EditText message= (EditText)rootView.findViewById(R.id.editText_message_notify);


            sendNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String msgTitle=title.getText().toString();
                    String msgSubject= subject.getText().toString();
                    String msgContent=message.getText().toString();

                    Log.d("debug","Send Notification button clicked");

                    sendNotification(getActivity(), msgTitle, msgSubject, msgContent);


                }
            });
            return rootView;
        }
        public void sendNotification(Context context,String msgTitle,String msgSubject,String msgContent){

            //create an intent for the activity that will receive this notification. That is when notification is clicked that activity will be shown.
            // Make this intent a pendingIntent. We give a pending intent to another application(eg. notification manage/ alarm manager) that uses our
            //intent to execute a predefined piece of code and use our intent. This intent won't be invoked by us but by another application

            Intent intent= new Intent(context,NotificationReceiver.class);
            intent.putExtra(Intent.EXTRA_TEXT,msgContent);
            PendingIntent pendingIntent= PendingIntent.getActivity(context,0, intent,0);

            Log.d("debug","In send notification pending intent prepared");

            NotificationCompat.Builder builder= new NotificationCompat.Builder(context);
            Notification notification= builder.
                    setContentTitle(msgTitle)
                    .setContentText(msgSubject)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_action_email).build();

            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            Log.d("debug","notification build");




            notificationManager.notify(0,notification);

            Log.d("debug","Notified");

            //From API 16
            //.setStyle(new Notification.BigTextStyle().bigText(msgContent))
        }
    }

}
