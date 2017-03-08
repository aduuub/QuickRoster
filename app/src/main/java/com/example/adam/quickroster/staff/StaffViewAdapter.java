package com.example.adam.quickroster.staff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.adam.quickroster.R;
import com.parse.ParseUser;

import java.util.List;

/**
 * An adapter for displaying the staff member details.
 */
class StaffViewAdapter extends BaseAdapter {

    private List<ParseUser> users;
    private Context mContext;

    public StaffViewAdapter(Context ctx, List<ParseUser> users) {
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
        // Get the view
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_staff_list_view_adapter, parent, false);

        // Get the staff member and their name
        ParseUser selectedUser = users.get(position);

        String firstName = selectedUser.getString("firstName");
        firstName = firstName == null ? "" : firstName;

        String lastName = selectedUser.getString("lastName");
        lastName = lastName == null ? "" : lastName;
        String combinedName = firstName + " " + lastName;

        // Set the text edits with the new name
        TextView staffNameTextView = (TextView) view.findViewById(R.id.staffName);
        staffNameTextView.setText(combinedName);

        return view;
    }
}
