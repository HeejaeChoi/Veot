package com.chj.veot;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.content.Intent;

import com.chj.veot.calendar.CalendarProvider;
import com.chj.veot.calendar.CalendarResolver;
import com.chj.veot.calendar.Event;
import com.chj.veot.calendar.EventAdapter;
import com.chj.veot.calendar.TimeData;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    CollapsingToolbarLayout mCollapsToolbar;
    FloatingActionButton mFAB;
    CalendarView mCalendarView;
    TextView mTodayBtn;
    RecyclerView mRecyclerView;

    EventAdapter mEventAdapter;
    CalendarProvider mCalendarProvider;
    Calendar selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFAB = (FloatingActionButton) findViewById(R.id.fab_main);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });

        selectedDate = Calendar.getInstance();
        selectedDate.set(Calendar.HOUR_OF_DAY,0);
        selectedDate.set(Calendar.MINUTE,0);

        mCalendarProvider = new CalendarProvider(this);
        //mCalendarProvider.addTestData();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main);
        mRecyclerView.getRecycledViewPool().clear();
        mRecyclerView.setLayoutManager(null);
        mRecyclerView.setAdapter(null);
        mRecyclerView.removeAllViewsInLayout();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(false);
        mRecyclerView.setLayoutManager(layoutManager);

        mEventAdapter = new EventAdapter(this, selectedDate);
        mRecyclerView.setAdapter(mEventAdapter);
        mEventAdapter.notifyDataSetChanged();


        mCalendarView = (CalendarView) findViewById(R.id.calendar);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate.set(Calendar.YEAR,year);
                selectedDate.set(Calendar.MONTH,month);
                selectedDate.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                mEventAdapter.setViewDate(selectedDate);
                mEventAdapter.notifyDataSetChanged();
            }
        });

        mTodayBtn = (TextView) findViewById(R.id.button_today_calendar);
        mTodayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //캘린더뷰에서 오늘로 설정함
                mCalendarView.setDate(System.currentTimeMillis());
            }
        });
    }
}
