package personal.mila.coconutreminder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";
    private static final String CHANNEL_ID = "MiLa";
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        mContext = context;
        Log.d(TAG, "action: " + action);
        if (action.equals(Reminder.ACTION_COCONUT_REMIND)) {
            //send notification
            notice();
        }
    }

    private void notice() {
        Log.d(TAG, "notice: ");

        CharSequence name = "mila";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.createNotificationChannel(channel);
        Intent notificationIntent = new Intent(mContext, DateActivity.class);

        int requestID = (int) System.currentTimeMillis();
        PendingIntent contentIntent = PendingIntent
                .getActivity(mContext, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(mContext, CHANNEL_ID)
                        .setSmallIcon(R.drawable.heart)
                        .setContentTitle("Hey MiLa, My Sunshine")
                        .setContentText("Don't forget drink coconut water")
                        .setAutoCancel(true)
                        .setContentIntent(contentIntent);

        notificationManager.notify(0, builder.build());
    }
}
