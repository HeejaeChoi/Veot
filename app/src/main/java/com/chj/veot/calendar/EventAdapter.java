package com.chj.veot.calendar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chj.veot.R;

import org.alicebot.ab.ParseState;

import java.util.ArrayList;
import java.util.Calendar;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private ArrayList<Event> mDataset;
    private Calendar viewDate;
    private Context context;


    public static class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView mStartTime, mEndTime, mTitle;

        public EventViewHolder(View view) {
            super(view);
            mStartTime = (TextView) view.findViewById(R.id.text_start_event);
            mEndTime = (TextView) view.findViewById(R.id.text_end_event);
            mTitle = (TextView) view.findViewById(R.id.text_title_event);
        }
    }

    public EventAdapter(Context context, Calendar date) {
        this.context = context;
        this.viewDate = date;
        this.mDataset = new ArrayList<Event>();
        this.mDataset = new CalendarResolver(context).getEventsOnDate(new TimeData(viewDate));
        Log.v("event adapter ",  "event list " + mDataset.size());
        notifyItemRangeRemoved(0, mDataset.size());
    }

    public EventAdapter setViewDate(Calendar date) {
        this.viewDate = date;
        this.mDataset = new ArrayList<Event>();
        this.mDataset = new CalendarResolver(context).getEventsOnDate(new TimeData(viewDate));
        Log.v("event adapter ",  "event list " + mDataset.size());
        notifyItemRangeRemoved(0, mDataset.size());
        return this;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_event, parent, false);
        EventViewHolder vh = new EventViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = mDataset.get(position);
        holder.mTitle.setText(event.getTitle());
        holder.mStartTime.setText(event.getStartTimeString());
        holder.mEndTime.setText(event.getEndTimeString());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void clear() {
        final int size = mDataset.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                mDataset.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }
}
