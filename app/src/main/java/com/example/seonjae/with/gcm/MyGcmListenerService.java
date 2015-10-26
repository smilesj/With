package com.example.seonjae.with.gcm;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.seonjae.with.R;
import com.example.seonjae.with.dialog.DialogGcmActivity;
import com.example.seonjae.with.project.ProjectHomeActivity;
import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by seonjae on 2015-10-22.
 */
public class MyGcmListenerService extends GcmListenerService {
    private static final String TAG = "MyGcmListenerService";
    /**
     *
     * @param from SenderID 값을 받아온다.
     * @param data Set형태로 GCM으로 받은 데이터 payload이다.
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String title = data.getString("title");
        String message = data.getString("message");

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Title: " + title);
        Log.d(TAG, "Message: " + message);

        // GCM으로 받은 메세지를 디바이스에 알려주는 sendNotification()을 호출한다.
        sendNotification(title, message);
        //Toast.makeText(this, "테스트입니다!", Toast.LENGTH_LONG).show();
        popup(title, message);
    }


    /**
     * 실제 디바에스에 GCM으로부터 받은 메세지를 알려주는 함수이다. 디바이스 Notification Center에 나타난다.
     * @param title
     * @param message
     */
    private void sendNotification(String title, String message) {
        Intent intent = new Intent(this, GcmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        //DialogSimple();
    }

    private void popup(String title, String message){
        Bundle bundle = new Bundle();
        bundle.putString("projectName", ProjectHomeActivity.itemProjectName);
        bundle.putString("title", title);
        bundle.putString("message", message);
        Intent popIntent = new Intent(getApplicationContext(), DialogGcmActivity.class);
        popIntent.putExtras(bundle);
        PendingIntent pie = PendingIntent.getActivity(getApplicationContext(), 0, popIntent, PendingIntent.FLAG_ONE_SHOT);
        try{
            pie.send();
        }
        catch(PendingIntent.CanceledException e){
            e.printStackTrace();
        }
    }


//    private void getMessage(Intent intent, Context context){
//        Intent popupIntent = new Intent(context, Popup.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(popupIntent);
//    }
}
