package project.coursera.dailyselfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SelfieReceiver extends BroadcastReceiver {

    private static final int MY_NOTIFICATION_ID = 1;

    private final CharSequence tickerText = "This is your selfie reminder.";
    private final CharSequence contentTitle = "Selfie Reminder";
    private final CharSequence contentText = "Tap now to take your daily selfie";

    private final long[] vibratePattern = {0, 300, 200, 300};

    @Override
    public void onReceive(Context context, Intent intent ) {
        Intent notificationIntent = new Intent(context, SelfieActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder notificationBuilder = new Notification.Builder(context)
                .setTicker(tickerText)
                .setSmallIcon(android.R.drawable.ic_menu_camera)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setContentIntent(pendingIntent)
                .setVibrate(vibratePattern);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MY_NOTIFICATION_ID, notificationBuilder.build());

    }
}
