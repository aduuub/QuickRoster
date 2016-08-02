package com.example.adam.quickroster;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.List;

public class StaffListViewAdapter extends BaseAdapter {

    private List<ParseUser> users;
    private Context mContext;

    public StaffListViewAdapter(Context ctx, List<ParseUser> users) {
        this.users = users;
        this.mContext = ctx;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the view
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_staff_list_view_adapter, parent, false);

        // get the staff member and their name
        ParseUser selectedUser = users.get(position);
        String firstName = selectedUser.getString("firstName");
        firstName = firstName == null ? "" : firstName;
        String lastName = selectedUser.getString("lastName");
        lastName = lastName == null ? "" : lastName;
        String combinedName = firstName + " " + lastName;

        // set the text edits
        TextView staffNameTextView = (TextView) view.findViewById(R.id.staffName);
        staffNameTextView.setText(combinedName);

        return view;
    }
}
