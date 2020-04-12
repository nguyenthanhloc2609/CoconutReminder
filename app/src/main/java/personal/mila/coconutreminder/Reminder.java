package personal.mila.coconutreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class Reminder {
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
    }

    public void setAlarm() {

    }

    public void cancelAlarm() {
        PendingIntent pi = PendingIntent.getBroadcast(mContext,
                REQUEST_CODE,
                mIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pi);
    }
}
