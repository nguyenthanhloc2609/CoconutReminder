package personal.mila.coconutreminder;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateActivity extends AppCompatActivity {
    private static final String TAG = "DateActivity";

    TextView tvDate, tvCycle;
    Button btnSetDate;
    //    Calendar calendar = Calendar.getInstance();
    DateFormatSymbols dfs = new DateFormatSymbols();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    DBManager dbManager;
    Reminder reminder;
    Date planDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        dbManager = new DBManager(this);
        if (dbManager.getCount() == 0) {
            dbManager.addNewDate(2019, 10, 26);
            dbManager.addNewDate(2020, 00, 07);
            dbManager.addNewDate(2020, 01, 05);
            dbManager.addNewDate(2020, 02, 13);
        }

        initComponent();

        btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });
    }

    private void pickDate() {
        final Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);
        int curMonth = calendar.get(Calendar.MONTH);
        int curDate = calendar.get(Calendar.DATE);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d(TAG, "onDateSet: " + month);
                calendar.set(year, month, dayOfMonth);
                btnSetDate.setText(sdf.format(calendar.getTime()));
                if (month != dbManager.getLastDate().getMonth())
                    dbManager.addNewDate(year, month, dayOfMonth);
                else
                    dbManager.update(year, month, dayOfMonth);
                updateCycleAndDate();
                calendar.setTime(planDate);
                calendar.add(Calendar.DATE, -7);
                reminder = new Reminder(DateActivity.this, calendar);
                //cancel all old alarms
                reminder.cancelAlarm();
                //make new alarms
                reminder.setAlarm();

            }
        }, curYear, curMonth, curDate);
        datePickerDialog.show();
    }

    private void initComponent() {
        tvDate = findViewById(R.id.tvDate);
        tvCycle = findViewById(R.id.tvCycle);
        btnSetDate = findViewById(R.id.btnSetDate);
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int lastMonth = dbManager.getLastDate().getMonth();
        if (month != lastMonth) {
            String[] months = dfs.getMonths();
            btnSetDate.setText(getResources().getString(R.string.str_button_set_date) + " " + months[month]);
        } else {
            btnSetDate.setText(sdf.format(dbManager.getLastDate()));
        }

        updateCycleAndDate();
    }

    private void updateCycleAndDate() {
        int cycle = calculateCycle();
        if (cycle == 0) {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        } else {
            tvCycle.setText(cycle + " days");
            planDate = planDate(cycle);
            tvDate.setText(sdf.format(planDate));
        }
    }

    private Date planDate(int cycle) {
        Date lastDate = dbManager.getLastDate();
        Log.d(TAG, "lastDate: " + lastDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(lastDate);
        cal.add(Calendar.DATE, cycle);
        Date planDate = cal.getTime();
        return planDate;
    }

    private int calculateCycle() {
        int total = 0;
        int count = 0;
        List<Date> listDate = dbManager.getAllDate();
        for (int i = 1; i < listDate.size(); i++) {
            count++;
            total += dayBetween(listDate.get(i - 1), listDate.get(i));
            Log.d(TAG, "total: " + total + " count: " + count);
        }
        if (count == 0)
            return 0;
        return total / count;
    }

    private long dayBetween(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();

        return diff / (1000 * 60 * 60 * 24);
    }
}
