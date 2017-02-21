package com.example.adam.quickroster.shifts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.adam.quickroster.R;
import com.example.adam.quickroster.model.ParseShift;
import com.example.adam.quickroster.model.ParseStaffUser;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * This is a view adapter for displaying information on the shift
 */
public class ShiftViewAdapterFragment extends BaseAdapter {
    private Context mContext;
    private List<? extends Object> mItems;
    private static final int SHIFT = 0;
    private static final int HEADER = 1;
    private LayoutInflater inflater;

    public ShiftViewAdapterFragment(Context ctx, List<? extends Object> items) {
        mContext = ctx;
        mItems = items;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems.get(position) instanceof ParseShift) {
            return SHIFT;
        } else {
            return HEADER;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
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

        Object object = mItems.get(position);

        switch (getItemViewType(position)) {
            case SHIFT:
                return setShift(parent, (ParseShift) object);

            case HEADER:
                return setHeader(parent, (String) object);

            default:
                throw new RuntimeException("Invalid view object");
        }
    }


    private View setHeader(ViewGroup parent, String text) {
        View view = inflater.inflate(R.layout.adapter_shift_view_header, parent, false);
        TextView headingTextView = (TextView) view.findViewById(R.id.shift_segment_header_text_view);
        headingTextView.setText(text);
        return view;
    }


    /**
     * @param parent
     * @param shift
     * @return
     */
    private View setShift(ViewGroup parent, ParseShift shift) {
        View view = inflater.inflate(R.layout.adapter_shift_view, parent, false);

        SimpleDateFormat formattedTime = new SimpleDateFormat("HH:mm");

        // Set staff member
        TextView staffMember = (TextView) view.findViewById(R.id.staffMember);
        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser.getBoolean("isManager")) {
            // Find and set shifts staff member's name
            ParseStaffUser staff = (ParseStaffUser) shift.getStaff();

            if (staff.getObjectId().equals(currentUser.getObjectId())) { // same user
                staffMember.setText("Self");
            } else {
                String staffName = staff.getFullName();
                staffMember.setText(staffName);
            }

        } else {
            // Hide
            staffMember.setVisibility(View.GONE);
        }

        // Set dates
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
