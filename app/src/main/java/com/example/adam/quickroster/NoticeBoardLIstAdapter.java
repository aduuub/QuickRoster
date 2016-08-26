package com.example.adam.quickroster;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class NoticeBoardListAdapter extends BaseAdapter {

    private List<String> notices;
    private Context context;

    public NoticeBoardListAdapter(Context context, List<String> notices){
        this.notices = notices;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.content_notice_board_list_adapter, parent, false);

        String notice = notices.get(position);
        EditText textBox = (EditText) view.findViewById(R.id.noticeMessage);
        if(notice != null)
            textBox.setText(notice);
        else
            throw new RuntimeException("lol");
        return view;
    }

    public List<String> getAllNotices(){
        List<String> notices = new ArrayList<>();
        return notices;
    }
}
