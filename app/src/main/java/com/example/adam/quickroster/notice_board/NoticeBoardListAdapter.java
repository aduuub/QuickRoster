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

import com.example.adam.quickroster.R;

import java.util.List;

public class NoticeBoardListAdapter extends ArrayAdapter {

    private List<String> notices;
    private Context context;
    private boolean editable;

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
        String notice = notices.get(position);
        EditText textBox = (EditText) view.findViewById(R.id.noticeMessage);
        textBox.setEnabled(editable);
        textBox.onWindowFocusChanged(true);
        textBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                notices.set(position, s.toString());
            }
        });
        textBox.setText(notice);
        return view;
    }


    public List<String> getNotices() {
        return this.notices;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}
