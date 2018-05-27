package com.chj.veot.calendar;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class CalendarProvider {
    private static final int PERMISSION_REQUEST_CODE = 1;

    private ContentResolver contentResolver;
    private Context context;

    public static final String[] ACCOUNT_FIELDS = {
            CalendarContract.Calendars.NAME,
            CalendarContract.Calendars.ACCOUNT_NAME,
            CalendarContract.Calendars.ACCOUNT_TYPE,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CalendarContract.Calendars.CALENDAR_COLOR,
            CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
            CalendarContract.Calendars.OWNER_ACCOUNT};

    public CalendarProvider(Context context) {
        this.context = context;
        contentResolver = context.getContentResolver();
        new CalendarResolver(context).deleteAll();
        createCalendar();
    }

    public void createCalendar() {
        checkPermission();
        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Calendars.NAME, "Veot Calendar");
        cv.put(CalendarContract.Calendars.CALENDAR_COLOR, "");
        cv.put(CalendarContract.Calendars.OWNER_ACCOUNT, "chj.com");
        cv.put(CalendarContract.Calendars.ACCOUNT_NAME, "chj.com");
        cv.put(CalendarContract.Calendars.ACCOUNT_TYPE, "chj.com");
        cv.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        Log.v("calendar", "calendar created");

        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        uri = uri.buildUpon().appendQueryParameter(android.provider.CalendarContract.CALLER_IS_SYNCADAPTER,"true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "chj.com")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, "chj.com").build();
        context.getContentResolver().insert(uri, cv);
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_CALENDAR}, PERMISSION_REQUEST_CODE);
        }
    }

    public void addTestData() {
        CalendarResolver cr = new CalendarResolver(context);
        cr.deleteAll();

        Calendar time1 = Calendar.getInstance();
        time1.set(Calendar.DATE, 28);
        time1.set(Calendar.HOUR_OF_DAY, 10);
        time1.set(Calendar.MINUTE, 30);

        Calendar time2 = Calendar.getInstance();
        time1.set(Calendar.DATE, 28);
        time1.set(Calendar.HOUR_OF_DAY, 16);
        time1.set(Calendar.MINUTE, 0);

        TimeData start = new TimeData(time1);
        TimeData end = new TimeData(time2);

        Event event = new Event("1", "TEST", "0", start, end);
        cr.addEvent(event);
    }

}
