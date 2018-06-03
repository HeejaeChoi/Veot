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

    private static CalendarProvider staticInstance;

    public CalendarProvider(Context context) {
        this.context = context;
        contentResolver = context.getContentResolver();
        createCalendar();
    }

    public static CalendarProvider getStaticInstance(Context context) {
        if (staticInstance == null)
            staticInstance = new CalendarProvider(context);
        return staticInstance;
    }

    public void addTestData() {
        CalendarResolver cr = new CalendarResolver(context);
        //cr.deleteAll();

        Calendar time1 = Calendar.getInstance();
        time1.set(Calendar.DATE, 1);
        time1.set(Calendar.HOUR_OF_DAY, 10);
        time1.set(Calendar.MINUTE, 30);

        Calendar time2 = Calendar.getInstance();
        time2.set(Calendar.DATE, 1);
        time2.set(Calendar.HOUR_OF_DAY, 16);
        time2.set(Calendar.MINUTE, 0);

        TimeData start = new TimeData(time1);
        TimeData end = new TimeData(time2);

        Event event = new Event("1", "TEST", "0", start, end);
        addEvent(event);
    }

    public long addEvent(Event event) {
        checkPermission();
        ContentValues cv = new ContentValues();
        TimeZone timeZone = TimeZone.getDefault();

        cv.put(CalendarContract.Events.TITLE, event.getTitle());
        cv.put(CalendarContract.Events.ALL_DAY, event.getAllDay());
        cv.put(CalendarContract.Events.DTSTART, event.getStartTime());
        cv.put(CalendarContract.Events.DTEND, event.getEndTime());
        cv.put(CalendarContract.Events.CALENDAR_ID, "1");
        cv.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());

        Uri uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, cv);

        String title = event.getTitle();
        String time1 = event.getStartTimeDataString();
        String time2 = event.getEndTimeDataString();
        Log.v("add event", title + " - " + time1 + " - " + time2);

        return Long.parseLong(uri.getLastPathSegment());    // return event ID (long)
    }

    public void setEventReminder(long eventId, int reminderMinutes) {
        checkPermission();
        ContentValues reminder = new ContentValues();
        reminder.put(CalendarContract.Reminders.EVENT_ID, eventId);
        reminder.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        reminder.put(CalendarContract.Reminders.MINUTES, reminderMinutes);
        contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, reminder);
    }

    public long addDiary(Diary diary) {
        checkPermission();
        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Events._ID, diary.getID());
        cv.put(CalendarContract.Events.TITLE, diary.getTitle());
        cv.put(CalendarContract.Events.DESCRIPTION, diary.getDescription());
        cv.put(CalendarContract.Events.DTSTART, diary.getDate());
        cv.put(CalendarContract.Events.CALENDAR_ID, "2");

        Uri uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, cv);
        Log.v("add diary : ", diary.getTitle());

        return Long.parseLong(uri.getLastPathSegment());    // return diary ID (long)
    }

    public void createCalendar() {
        checkPermission();

        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Calendars.ACCOUNT_NAME, "chj.com");
        cv.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        cv.put(CalendarContract.Calendars.OWNER_ACCOUNT, "chj.com");
        cv.put(CalendarContract.Calendars.NAME, "Veot Event Calendar");
        cv.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "Veot Event Calendar");
        cv.put(CalendarContract.Calendars.CALENDAR_COLOR, 0XFFFFFF);
        cv.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        cv.put(CalendarContract.Calendars.VISIBLE, 1);
        cv.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, TimeZone.getDefault().getID());
        cv.put(CalendarContract.Calendars.CAN_MODIFY_TIME_ZONE, 1);
        cv.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        cv.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 1);
        cv.put(CalendarContract.Calendars.MAX_REMINDERS, 8);
        cv.put(CalendarContract.Calendars.ALLOWED_REMINDERS, "0,1,4");
        cv.put(CalendarContract.Calendars.ALLOWED_AVAILABILITY, "0,1,2");

        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        uri = uri
                .buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "chj.com")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
                .build();

        contentResolver.insert(uri, cv);

        Log.v("calendar", "calendar created");
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_CALENDAR}, PERMISSION_REQUEST_CODE);
        }
    }
}