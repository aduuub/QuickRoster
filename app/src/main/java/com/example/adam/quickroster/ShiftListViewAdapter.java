package com.example.adam.quickroster;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

/**
 * Created by Adam on 30/04/16.
 */
public class ShiftListViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<ParseShift> mItems;
    public ShiftListViewAdapter(Context ctx, List<ParseShift> items){
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.row_shift, parent, false);
        ParseShift shift = mItems.get(position);

        SimpleDateFormat formattedTime = new SimpleDateFormat("HH:mm");

        TextView startView = (TextView) view.findViewById(R.id.startTime);
        Date startDate = shift.getStartDate();
        startView.setText(formattedTime.format(startDate));

        TextView endView = (TextView) view.findViewById(R.id.endTime);
        Date endDate = shift.getEndDate();
        endView.setText(formattedTime.format(endDate));

        TextView dateView = (TextView) view.findViewById(R.id.Details);
        dateView.setText(String.valueOf(shift.getDetails()));

        return view;
    }
}
