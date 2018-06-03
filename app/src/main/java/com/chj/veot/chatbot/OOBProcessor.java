package com.chj.veot.chatbot;

import android.content.Context;
import android.util.Log;

import com.chj.veot.calendar.CalendarProvider;
import com.chj.veot.calendar.CalendarResolver;
import com.chj.veot.calendar.Diary;
import com.chj.veot.calendar.Event;
import com.chj.veot.calendar.TimeData;

import java.util.ArrayList;
import java.util.Calendar;

public class OOBProcessor {
    String response;
    String[] process;
    String newResponse;
    String action;
    String newRequest;

    Context context;
    CalendarResolver mCalendarResolver;

    OOBProcessor(String response, Context context) {
        this.response = response;
        this.context = context;
        mCalendarResolver = new CalendarResolver(context);
    }

    public boolean checkOOB() {
        if (response.contains("OOB")) {
            process = response.split("\\#");
            Log.v("OOB check ", response);
            return true;
        } else return false;
    }

    public String OOBAction() {
        switch (process[1]) {
            case "SCHEDULE":
                ScheduleAction schedule = new ScheduleAction();
                break;
            case "SEARCH":
                SearchAction search = new SearchAction();
                break;
            case "DIARY":
                DiaryAction diary = new DiaryAction();
                break;
        }
        action = process[1];
        return newResponse;
    }

    public String OOBActionRequest() {
        newRequest = "XDONE";
        return newRequest;
    }

    private class ScheduleAction {
        private String title;
        private int month, date, day = 0;
        private int starthour, startminute, endhour, endminute, notihour, notiminute;
        private int thismonth, thisweek, allday = 0;
        private TimeData start, end;

        private ScheduleAction() {
            Log.v("oob ", "schedule action");
            listener();
            setTimeData();
            setEvent();
            returnResponse();
        }

        private void returnResponse() {
            newResponse = "일정 등록을 마쳤어요.";
        }

        private void setEvent() {
            Event event = new Event();
            event.setID("1");
            event.setStartTime(start);
            event.setEndTime(end);
            event.setAllDay(String.valueOf(allday));
            event.setTitle(title);

            long id = CalendarProvider.getStaticInstance(context).addEvent(event);
            Log.v("oob ", "schedule action - setEvent");

            if (notihour != 0 && notiminute != 0) {
                int noti_in_minute = (notihour * 60) + notiminute;
                CalendarProvider.getStaticInstance(context).setEventReminder(id, noti_in_minute);
            }
        }

        private void setTimeData() {
            Calendar current = Calendar.getInstance();
            int currentday = current.get(Calendar.DAY_OF_WEEK);
            int month_range = current.getActualMaximum(Calendar.DAY_OF_MONTH);
            start = TimeData.getCurrentTimeData();
            end = start;

            if (month != 0 && date != 0) {
                start.setMonth(month);
                if (thismonth != 0) {
                    start.setMonth(start.getMonth() + thismonth - 1);
                }
                start.setDate(date);
            }
            if (thisweek != 0 && day != 0) {
                int d = currentday - day;
                int w = (thisweek - 1) * 7;
                int newDate = d + w;
                if (newDate > month_range) {
                    start.setMonth(start.getMonth() + 1);
                    start.setDate(start.getDate() + (newDate - month_range));
                } else {
                    start.setDate(newDate);
                }
            }
            if (allday == 0) {
                start.setHour(starthour);
                start.setMinute(startminute);

                if (endhour != 0 && endminute != 0) {
                    end = start;
                    end.setHour(endhour);
                    end.setMinute(endminute);
                }
            }
        }

        private void listener() {
            if (process.length == 13) {
                if (!process[2].equals("NULL")) {
                    switch (process[2]) {
                        case "this":
                            thismonth = 1;
                            break;
                        case "next":
                            thismonth = 2;
                            break;
                        case "afternext":
                            thismonth = 3;
                            break;
                    }
                    month = Integer.parseInt(process[2]);
                }
                if (!process[3].equals("NULL")) {
                    date = Integer.parseInt(process[3]);
                }
                if (!process[4].equals("NULL")) {
                    day = Integer.parseInt(process[4]);
                }
                if (!process[5].equals("NULL")) {
                    switch (process[5]) {
                        case "this":
                            thisweek = 1;
                            break;
                        case "next":
                            thisweek = 2;
                            break;
                        case "afternext":
                            thisweek = 3;
                            break;
                    }
                }
                if (!process[6].equals("NULL")) {
                    if (process[6].equals("ALLDAY")) {
                        allday = 1;
                    } else {
                        starthour = Integer.parseInt(process[6]);
                        startminute = Integer.parseInt(process[7]);
                        if (!process[8].equals("NULL")) {
                            endhour = Integer.parseInt(process[8]);
                            endminute = Integer.parseInt(process[9]);
                        }
                    }
                }
                if (!process[10].equals("NULL")) {
                    title = process[10];
                }
                if (!process[11].equals("NULL")) {
                    notihour = Integer.parseInt(process[11]);
                    notiminute = Integer.parseInt(process[12]);
                }
            }
        }
    }

    private class SearchAction {
        private String newResponse;
        private int thismonth, thisweek, thisday = 0;
        private int date, day = 0;
        private TimeData search;
        private ArrayList<Event> result;


        private SearchAction() {
            Log.v("oob ", "search action");
            listener();
            setTimeData();
            searchEvent();
        }

        private void searchEvent() {
            result = mCalendarResolver.getEventsOnDate(search);
            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (Event event : result) {
                sb.append(event.getTitle());
                count++;
            }
            newResponse = count + "개의 일정을 찾았어요." + sb.toString();
            Log.v("oob ", "search action - search event");
        }

        private void setTimeData() {
            Calendar current = Calendar.getInstance();
            int currentday = current.get(Calendar.DAY_OF_WEEK);
            int month_range = current.getActualMaximum(Calendar.DAY_OF_MONTH);
            search = TimeData.getCurrentTimeData();

            if (thismonth != 0 && date != 0) {
                search.setMonth(search.getMonth() + thismonth - 1);
                search.setDate(date);
            }
            else if (thisweek != 0 && day != 0) {
                int d = currentday - day;
                int w = (thisweek - 1) * 7;
                int newDate = d + w;
                if (newDate > month_range) {
                    search.setMonth(search.getMonth() + 1);
                    search.setDate(search.getDate() + (newDate - month_range));
                } else {
                    search.setDate(newDate);
                }
            }
            else if (thismonth != 0 && thisday != 0) {
                search.setMonth(search.getMonth() + thismonth - 1);
                search.setDate(search.getDate() + thisday - 1);
            }
        }

        private void listener() {
            if (process.length == 6) {
                if (!process[2].equals("NULL")) {
                    switch (process[2]) {
                        case "this":
                            thismonth = 1;
                            break;
                        case "next":
                            thismonth = 2;
                            break;
                        case "afternext":
                            thismonth = 3;
                            break;
                    }
                }
                if (!process[3].equals("NULL")) {
                    date = Integer.parseInt(process[3]);
                }
                if (!process[4].equals("NULL")) {
                    switch (process[4]) {
                        case "this":
                            thisweek = 1;
                            break;
                        case "next":
                            thisweek = 2;
                            break;
                        case "afternext":
                            thisweek = 3;
                            break;
                    }
                }
                if (!process[5].equals("NULL")) {
                    switch (process[5]) {
                        case "this":
                            thisday = 1;
                            break;
                        case "next":
                            thisday = 2;
                            break;
                        case "afternext":
                            thisday = 3;
                            break;
                        default:
                            day = Integer.parseInt(process[5]);
                            break;
                    }
                }
            }
        }
    }

    private class DiaryAction {
        private String newResponse;
        private String hardtimes, goodtimes;
        private TimeData today;

        private DiaryAction() {
            Log.v("oob ", "diary action");
            listener();
            setTimeData();
            setDiary();
            returnResponse();
        }

        private void returnResponse() {
            newResponse = "";
        }

        private void setDiary() {
            Diary hard = new Diary("HARD", hardtimes);
            hard.setDate(today);
            Diary good = new Diary("GOOD", goodtimes);
            good.setDate(today);

            CalendarProvider.getStaticInstance(context).addDiary(hard);
            CalendarProvider.getStaticInstance(context).addDiary(good);
            Log.v("oob ", "diary action - set diary");
        }

        private void setTimeData() {
            today = TimeData.getCurrentTimeData();
        }

        private void listener() {
            if (!process[2].equals("NULL")) {
                hardtimes = process[2];
            }
            if (!process[3].equals("NULL")) {
                goodtimes = process[3];
            }
        }
    }
}
