package com.chj.veot.calendar;

import android.database.Cursor;
import android.provider.CalendarContract;

import java.sql.Time;

public class Event {
    private String eventId;
    private String title;
    private String allday;
    private String startTime;
    private String endTime;

    public Event() {

    }

    public Event(String id, String title, String allday, String start_time, String end_time) {
        this.eventId = id;
        this.title = title;
        this.allday = allday;
        this.startTime = start_time;
        if(allday=="1" && start_time.equals(end_time)) {
            this.endTime = start_time;
        }
        else {
            this.endTime = end_time;
        }
    }

    public Event(String id, String title, String allday, TimeData start_time, TimeData end_time) {
        this.eventId = id;
        this.title = title;
        this.allday = allday;
        this.startTime = String.valueOf(start_time.getTimeMills());
        if(allday=="1" && start_time.equals(end_time)) {
            this.endTime = startTime;
        }
        else {
            this.endTime = String.valueOf(end_time.getTimeMills());
        }
    }

    public String getID() {
        return eventId;
    }
    public void setID(String id) {
        this.eventId = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getAllDay() {
        return allday == "0";
    }
    public void setAllDay(String allday) {
        this.allday = allday;
    }

    public String getStartTime() {
        return startTime;
    }
    public String getStartTimeString() {
        TimeData data = new TimeData(Long.parseLong(startTime));
        String time = data.getTimeString();
        return time;
    }
    public void setStartTime(String time) {
        this.startTime = time;
    }
    public void setStartTime(TimeData time) {
        this.startTime = String.valueOf(time.getTimeMills());
    }

    public String getEndTime() {
        return endTime;
    }
    public String getEndTimeString() {
        TimeData data = new TimeData(Long.parseLong(endTime));
        String time = data.getTimeString();
        return time;
    }
    public void setEndTime(String time) {
        this.endTime = time;
    }
    public void setEndTime(TimeData time) {
        this.endTime = String.valueOf(time.getTimeMills());
    }

    public Event populate(Cursor cursor) {
     this.setID(cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Events._ID)));
     this.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Events.TITLE)));
     this.setAllDay(cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Events.ALL_DAY)));
     this.setStartTime(cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Events.DTSTART)));
     this.setEndTime(cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Events.DTEND)));

     return this;
    }


}
