package personal.mila.coconutreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class Reminder {
    private static final String TAG = "Reminder";

    private Calendar calendar;
    private AlarmManager alarmManager;
    private Context mContext;
    private Intent mIntent;
    private static final int REQUEST_CODE = 222;
    public static final String ACTION_COCONUT_REMIND = "mila.liam.coconut";

    public Reminder() {

    }

    public Reminder(Context context, Calendar calendar) {
        this.calendar = calendar;
        mContext = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mIntent = new Intent(ACTION_COCONUT_REMIND);
        ComponentName componentName = new ComponentName(mContext, MyReceiver.class);
        mIntent.setComponent(componentName);
    }

    public void setAlarm() {
        Log.d(TAG, "setAlarm: ");
        calendar.set(Calendar.HOUR, 6);
        PendingIntent pi = PendingIntent.getBroadcast(mContext,
                REQUEST_CODE,
                mIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d(TAG, "calendar: " + calendar.getTime());
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
        calendar.set(Calendar.HOUR, 18);
        Log.d(TAG, "calendar: " + calendar.getTime());
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
    }

    public void cancelAlarm() {
        Log.d(TAG, "cancelAlarm: ");
        PendingIntent pi = PendingIntent.getBroadcast(mContext,
                REQUEST_CODE,
                mIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pi);
    }
}
