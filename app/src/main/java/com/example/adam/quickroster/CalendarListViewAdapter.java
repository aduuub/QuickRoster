package com.example.adam.quickroster;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

/**
 * Created by Adam on 30/04/16.
 */
public class CalendarListViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<Shift> mItems;
    public CalendarListViewAdapter (Context ctx,List<Shift> items){
        mContext = ctx;
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mItems.get(position).getStartTime();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.row_shift, parent, false);
        Shift shift = mItems.get(position);
        SimpleDateFormat formattedTime = new SimpleDateFormat("mm:HH dd/MM/yyyy");

        TextView startView = (TextView) v.findViewById(R.id.startTime);
        long time = shift.getStartTime();
        Date d = new Date(time);
        startView.setText(formattedTime.format(d));

        TextView endView = (TextView) v.findViewById(R.id.endTime);
        time = shift.getEndTime();
        d = new Date(time);
        endView.setText(formattedTime.format(d));

        TextView dateView = (TextView) v.findViewById(R.id.Details);
        dateView.setText(String.valueOf(shift.getDetails()));

        return v;
    }

}
