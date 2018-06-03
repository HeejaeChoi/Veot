package com.chj.veot.calendar;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class TimeData {
    private int year;
    private int month;
    private int date;
    private int hour = 0;
    private int minute = 0;

    public TimeData() {

    }

    public TimeData(int year, int month, int date) {
        this.date = date;
        this.month = month;
        this.year = year;
        this.hour = 0;
        this.minute = 0;
    }

    public TimeData(int year, int month, int date, int hour, int minute) {
        this.date = date;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
    }

    public TimeData(Calendar calendar) {
        this.date = calendar.get(Calendar.DATE);
        this.month = calendar.get(Calendar.MONTH) + 1;
        this.year = calendar.get(Calendar.YEAR);
        this.hour = calendar.get(Calendar.HOUR_OF_DAY);
        this.minute = calendar.get(Calendar.MINUTE);
    }

    public TimeData(long millTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millTime);
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.date = calendar.get(Calendar.DATE);
        this.hour = calendar.get(Calendar.HOUR_OF_DAY);
        this.minute = calendar.get(Calendar.MINUTE);
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public String getYearString() {
        String year = String.valueOf(this.year);
        return year;
    }

    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public String getMonthString() {
        String month = String.valueOf(this.month);
        return month;
    }

    public int getDate() {
        return date;
    }
    public void setDate(int date) {
        this.date = date;
    }
    public String getDateString() {
        String date = String.valueOf(this.date);
        return date;
    }

    public int getHour() {
        return hour;
    }
    public void setHour(int hour) {
        this.hour = hour;
    }
    public String getHourString() {
        String hour = "";
        if (this.hour > 12) {
            int newHour = this.hour - 12;
            if(newHour < 10) {
                hour = "PM " + String.format("0%d", newHour);
            }
            else {
                hour = "PM " + String.format("%d", newHour);
            }
        } else {
            int newHour = this.hour;
            if(newHour < 10) {
                hour = "AM " + String.format("0%d", newHour);
            }
            else {
                hour = "AM " + String.format("%d", newHour);
            }
        }
        return hour;
    }

    public int getMinute() {
        return minute;
    }
    public void setMinute(int minute) {
        this.minute = minute;
    }
    public String getMinuteString() {
        String minute;
        if (this.minute < 10) {
            minute = String.format("0%d", this.minute);
        } else {
            minute = String.format("%d", this.minute);
        }
        return minute;
    }

    public String getTimeString() {
        String time = "";
        time = getHourString() + " : " + getMinuteString();
        return time;
    }

    public String getTimeDataString() {
        String time = "";
        time = this.year + "/" + this.month + "/" + this.date + " " + getHourString() + " : " + getMinuteString();
        return time;
    }

    public long getTimeMills() {
        Calendar time = Calendar.getInstance();
        time.set(this.year, this.month, this.date, this.hour, this.minute);

        return time.getTimeInMillis();
    }

    public static TimeData getCurrentTimeData(){
        Calendar calendar = Calendar.getInstance();
        return new TimeData(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH) + 1, calendar.get(calendar.DAY_OF_MONTH));
    }

    @Override
    public boolean equals(Object o) {
        TimeData data = (TimeData) o;
        return ((data.year == this.year) && (data.month == this.month) && (data.date == this.date));
    }

}
