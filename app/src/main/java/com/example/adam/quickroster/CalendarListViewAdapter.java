package com.example.adam.quickroster;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

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
        TextView startView = (TextView) v.findViewById(R.id.startTime);
        startView.setText(String.valueOf(shift.getStartTime()));

        TextView endView = (TextView) v.findViewById(R.id.endTime);
        endView.setText(String.valueOf(shift.getEndTime()));

        TextView dateView = (TextView) v.findViewById(R.id.Details);
        startView.setText(String.valueOf(shift.getDetails()));

        return v;
    }

}
