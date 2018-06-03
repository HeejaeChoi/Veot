package com.chj.veot.calendar;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;

public class CalendarResolver {
    private static final int PERMISSION_REQUEST_CODE = 1;

    private static CalendarResolver staticInstance;
    private ContentResolver contentResolver;
    private Context context;

    public static final Uri EVENTS_URI = Uri.parse("content://com.android.calendar/events");

    public static final String[] EVENTS_FIELDS = {
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.ALL_DAY,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.CALENDAR_ID
    };
    public static final String[] DIARY_FIELDS = {
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.CALENDAR_ID
    };

    public CalendarResolver(Context context) {
        this.context = context;
        contentResolver = context.getContentResolver();
    }

    public static CalendarResolver getStaticInstance(Context context) {
        if (staticInstance == null){
            staticInstance = new CalendarResolver(context);
        }
        return staticInstance;
    }

    public ArrayList<Event> getEventsOnDate(TimeData timeData) {
        checkPermission();
        ArrayList<Event> eventlist = new ArrayList<>();

        TimeData begin = new TimeData(timeData.getYear(), timeData.getMonth(), timeData.getDate());
        String beginTime = Long.toString(begin.getTimeMills());

        TimeData end = new TimeData(begin.getYear(), begin.getMonth(), begin.getDate() + 1);
        String endTime = Long.toString(end.getTimeMills());

        String selection1 = CalendarContract.Events.DTSTART + " >= ? ";
        String selection2 = CalendarContract.Events.DTEND + " < ? ";
        String selection3 = CalendarContract.Events.CALENDAR_ID + " = 1";
        String[] selectionArgs = new String[]{beginTime, endTime};

        Cursor cur = contentResolver.query(
                EVENTS_URI, EVENTS_FIELDS, selection1 + "AND " + selection2 + "AND (" + selection3 + ")", selectionArgs, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                eventlist.add(new Event().populate(cur));
            }
        }
        Log.v("get events on date ", "" + cur.getCount());
        cur.close();

        return eventlist;
    }

    public ArrayList<Diary> getDiaryOnDate(TimeData timeData) {
        checkPermission();
        ArrayList<Diary> diarylist = new ArrayList<>();

        TimeData begin = new TimeData(timeData.getYear(), timeData.getMonth(), timeData.getDate());
        String beginTime = Long.toString(begin.getTimeMills());

        TimeData end = new TimeData(begin.getYear(), begin.getMonth(), begin.getDate() + 1);
        String endTime = Long.toString(end.getTimeMills());

        String selection1 = CalendarContract.Events.DTSTART + " >= ? ";
        String selection2 = CalendarContract.Events.DTSTART + " < ? ";
        String selection3 = CalendarContract.Events.CALENDAR_ID + " = 2 ";
        String[] selectionArgs = new String[]{beginTime, endTime};

        Cursor cur = contentResolver.query(
                EVENTS_URI, DIARY_FIELDS, selection1 + " AND " + selection2 + " AND (" + selection3 + ")", selectionArgs, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                diarylist.add(new Diary().populate(cur));
            }
        }
        cur.close();

        return diarylist;
    }

    public void checkEventList() {
        checkPermission();

        String[] where = new String[]{ CalendarContract.Events.CALENDAR_ID };

        String selection = CalendarContract.Events.CALENDAR_ID + " = ? ";
        String[] selectionArgs = new String[]{"1", "2"};

        Cursor cur = contentResolver.query(
                EVENTS_URI, where, null, null, null);

        int count = 0;
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                count++;
            }
        }
        cur.close();

        Log.v("check event list", "" + count);
    }

    public void deleteAll() {
        checkPermission();
        try {
            String selection = CalendarContract.Events.CALENDAR_ID + " = ? ";
            String[] selectionArgs = new String[]{"1", "2"};

            int count = contentResolver.delete(EVENTS_URI, selection + "OR " + selection, selectionArgs);
            Log.v("delete all events ", "" + count);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_CALENDAR}, PERMISSION_REQUEST_CODE);
        }
    }
}
