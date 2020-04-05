package com.hfad.joke;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DelayedMessageService extends IntentService {
    public static final String EXTRA_MESSAGE = "message";
    public DelayedMessageService() {
        super("DelayedMessageService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        synchronized (this) {
            try {
                wait(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String text = intent.getStringExtra(EXTRA_MESSAGE);
        showText(text);
    }
    private void showText(final String text) {
        notification(getString(R.string.app_name),text,this);
    }
    public void notification(String title, String message, Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 5354;
        String channelId = "channel-id";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(message)
                .setVibrate(new long[]{100, 250})
                .setLights(Color.YELLOW, 500, 5000)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(new Intent(context, MainActivity.class));
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }

    public int createID() {
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss", Locale.FRENCH).format(now));
        return id;
    }
}
