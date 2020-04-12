package personal.mila.coconutreminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DBManager extends SQLiteOpenHelper {
    private static final String TAG = "DBManager";

    public static final String DATABASE_NAME = "MiLa_Coconut";
    private static final String TABLE_NAME = "Date";
    private static final String ID = "id";
    private static final String DATE = "date";
    private static final String MONTH = "month";
    private static final String YEAR = "year";


    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, 1);
        Log.d(TAG, "DBManager: ");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " integer primary key, " +
                DATE + " integer, " +
                MONTH + " integer, " +
                YEAR + " integer)";
        db.execSQL(sqlQuery);
        Log.d(TAG, "onCreate: successfully");
//        addNewDate(2019, 11, 26);
//        addNewDate(2020, 01, 07);
//        addNewDate(2020, 02, 05);
//        addNewDate(2020, 03, 13);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Add new date
    public void addNewDate(int year, int month, int date) {
        Log.d(TAG, "addNewDate: " + date + "/" + month + "/" + year);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATE, date);
        values.put(MONTH, month);
        values.put(YEAR, year);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public int update(int year, int month, int date) {
        Log.d(TAG, "update: in month: " + month);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DATE, date);
        values.put(MONTH, month);
        values.put(YEAR, year);

        int ret = db.update(TABLE_NAME, values, ID + "=?", new String[]{String.valueOf(getCount())});
        db.close();
        return ret;
    }

    public Date getLastDate() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{ID, DATE, MONTH, YEAR}, ID + "=?",
                new String[]{String.valueOf(getCount())}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        int date = cursor.getInt(1);
        int month = cursor.getInt(2);
        int year = cursor.getInt(3);
        db.close();
        return new Date(year - 1900, month, date);
    }

    public List<Date> getAllDate() {
        List<Date> listDate = new ArrayList<Date>();
        String cmd = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(cmd, null);
        if (cursor.moveToFirst()) {
            do {
                int date = cursor.getInt(1);
                int month = cursor.getInt(2);
                int year = cursor.getInt(3);
                listDate.add(new Date(year - 1900, month, date));
            } while (cursor.moveToNext());
        }
        Log.d(TAG, "getAllDate: \n" + listDate);
        return listDate;
    }

    public int getCount() {
        String cmd = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(cmd, null);
        int count = cursor.getCount();
        Log.d(TAG, "getCount: " + count);
        return count;
    }
}
