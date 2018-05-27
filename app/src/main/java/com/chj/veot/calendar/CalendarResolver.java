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
import java.util.TimeZone;

public class CalendarResolver {
    private static final int PERMISSION_REQUEST_CODE = 1;

    private ContentResolver contentResolver;
    private Context context;

    public CalendarResolver(Context context) {
        this.context = context;
        contentResolver = context.getContentResolver();
    }

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

    public long addEvent(Event event) {
        checkPermission();
        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Events.TITLE, event.getTitle());
        cv.put(CalendarContract.Events.ALL_DAY, event.getAllDay());
        cv.put(CalendarContract.Events.DTSTART, event.getStartTime());
        cv.put(CalendarContract.Events.DTEND, event.getEndTime());
        cv.put(CalendarContract.Events.CALENDAR_ID, "1");
        cv.put(CalendarContract.Events.EVENT_TIMEZONE, String.valueOf(TimeZone.getDefault()));

        Uri uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, cv);
        Log.v("calendar : ", "add event");

        return Long.parseLong(uri.getLastPathSegment());    // return event ID (long)
    }

    public ArrayList<Event> getEventsOnDate(TimeData timeData) {
        checkPermission();
        ArrayList<Event> eventlist = new ArrayList<>();
        Uri uri = CalendarContract.Events.CONTENT_URI;

        TimeData begin = new TimeData(timeData.getYear(), timeData.getMonth(), timeData.getDate());
        String beginTime = Long.toString(begin.getTimeMills());

        TimeData end = new TimeData(begin.getYear(), begin.getMonth(), begin.getDate()+1);
        String endTime = Long.toString(end.getTimeMills());

        String selection1 = CalendarContract.Events.DTSTART + " >= ? ";
        String selection2 = CalendarContract.Events.DTEND + " < ? ";
        String selection3 = CalendarContract.Events.CALENDAR_ID + " = 1";
        String [] selectionArgs = new String[] {beginTime, endTime};

        Cursor cur = contentResolver.query(
                uri, EVENTS_FIELDS, selection1 + " AND " + selection2 + " AND (" + selection3 + ")", selectionArgs, null);
        if (cur.getCount() > 0){
            while (cur.moveToNext()){
                eventlist.add(new Event().populate(cur));
            }
        }
        cur.close();

        return eventlist;
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

    public ArrayList<Diary> getDiaryOnDate(TimeData timeData) {
        checkPermission();
        ArrayList<Diary> diarylist = new ArrayList<>();
        Uri uri = CalendarContract.Events.CONTENT_URI;

        TimeData begin = new TimeData(timeData.getYear(), timeData.getMonth(), timeData.getDate());
        String beginTime = Long.toString(begin.getTimeMills());

        TimeData end = new TimeData(begin.getYear(), begin.getMonth(), begin.getDate()+1);
        String endTime = Long.toString(end.getTimeMills());

        String selection1 = CalendarContract.Events.DTSTART + " >= ? ";
        String selection2 = CalendarContract.Events.DTSTART + " < ? ";
        String selection3 = CalendarContract.Events.CALENDAR_ID + " = 2 ";
        String [] selectionArgs = new String[] {beginTime, endTime};

        Cursor cur = contentResolver.query(
                uri, DIARY_FIELDS, selection1 + " AND " + selection2 + " AND (" + selection3 + ")", selectionArgs, null);
        if (cur.getCount() > 0){
            while (cur.moveToNext()){
                diarylist.add(new Diary().populate(cur));
            }
        }
        cur.close();

        return diarylist;
    }

    public void deleteAll() {
        checkPermission();
        Uri uri = CalendarContract.Events.CONTENT_URI;
        String selection = "(" + CalendarContract.Events.CALENDAR_ID + " = ?)";
        String[] selectionArgs = new String[] { "1" };
        int updCount = contentResolver.delete(uri, selection, selectionArgs);
        Log.v("delete all : ", "delete result " + updCount);
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_CALENDAR}, PERMISSION_REQUEST_CODE);
        }
    }
}
