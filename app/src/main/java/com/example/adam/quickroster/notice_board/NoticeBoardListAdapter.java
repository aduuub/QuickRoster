package com.example.adam.quickroster.notice_board;

import android.content.Context;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.adam.quickroster.R;
import com.example.adam.quickroster.model.ParseNotice;
import com.parse.Parse;

import java.util.List;

public class NoticeBoardListAdapter extends ArrayAdapter {

    private List<ParseNotice> notices;
    private Context context;

    public NoticeBoardListAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.notices = objects;
        this.context = context;
    }

    @Override
    public int getCount() {
        return notices.size();
    }

    @Override
    public Object getItem(int position) {
        return notices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.content_notice_board_list_adapter, parent, false);
        ParseNotice notice = notices.get(position);

        // set title
        String title = notice.getTitle();
        TextView titleBox = (TextView) view.findViewById(R.id.notice_title);
        titleBox.setText(title);

        // set message
        String message = notice.getMessage();
        TextView messageBox = (TextView) view.findViewById(R.id.notice_message);
        if(message.length() > 100){
            message = message.substring(0, 100);
            message += " ...";
        }
        messageBox.setText(message);
        return view;
    }
}
