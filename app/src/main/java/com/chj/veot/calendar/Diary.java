package com.chj.veot.calendar;

import android.database.Cursor;
import android.provider.CalendarContract;

public class Diary {
    private String id;
    private static String DIARY_TITLE = "DIARY";
    private String description;
    private String date;

    public Diary() {

    }

    public Diary(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public Diary(String id, String description, String date) {
        this.id = id;
        this.description = description;
        this.date = date;
    }

    public String getID() {
        return id;
    }
    public void setID(String id) {
        this.id = id;
    }

    public String getTitle() {
        return DIARY_TITLE;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setDate(TimeData date) {
        TimeData timeData = new TimeData(date.getYear(), date.getMonth(), date.getDate());
        this.date = Long.toString(timeData.getTimeMills());
    }

    public Diary populate(Cursor cursor) {
        this.setID(cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Events._ID)));
        this.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Events.DESCRIPTION)));
        this.setDate(cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Events.DTSTART)));

        return this;
    }
}
