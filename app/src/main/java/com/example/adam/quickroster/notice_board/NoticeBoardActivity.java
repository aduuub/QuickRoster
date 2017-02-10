package com.example.adam.quickroster.notice_board;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adam.quickroster.R;
import com.example.adam.quickroster.misc.ParseUtil;
import com.example.adam.quickroster.model.ParseBusiness;
import com.example.adam.quickroster.model.ParseNotice;
import com.example.adam.quickroster.model.ParseStaffUser;
import com.example.adam.quickroster.staff.AddStaffMemberActivity;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NoticeBoardActivity extends Fragment {

    ListView noticesList;
    List<ParseNotice> notices;
    boolean editable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_notice_board, container, false);

        noticesList = (ListView) view.findViewById(R.id.notice_board_list_view);

        ParseStaffUser currentUser = ParseUtil.getCurrentUser();
        this.editable = currentUser.getBoolean("isManager");

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Notices");
        setHasOptionsMenu(true);

        ParseBusiness business = currentUser.getBusiness();
        notices = ParseNotice.getBusinessNotices(business);
        setAdapter(notices);

        return view;
    }


    public void setAdapter(List<? extends ParseObject> notices) {
        NoticeBoardListAdapter adapter = new NoticeBoardListAdapter(getActivity(),
                R.layout.content_notice_board_list_adapter, notices);
        noticesList.setAdapter(adapter);


        noticesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                editNotice(position);
            }
        });
    }


    private void editNotice(int pos) {
        ParseNotice notice = notices.get(pos);
        if (notice != null) {
            Intent intent = new Intent(getActivity(), NoticeBoardEdit.class);
            intent.putExtra("objectId", notice.getObjectId());
            startActivity(intent);
            updateNoticesAndAdapter();
        }
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        if (ParseUser.getCurrentUser().getBoolean("isManager")) {
            inflater.inflate(R.menu.add_menu, menu);
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_icon_add) {
            addNotice();
            return true;
        }
        return false;
    }


    private void addNotice() {
        Intent intent = new Intent(getContext(), NoticeBoardEdit.class);
        startActivity(intent);
        updateNoticesAndAdapter();
    }

    public void updateNoticesAndAdapter(){
        ParseBusiness business = ParseUtil.getCurrentUser().getBusiness();
        setAdapter(ParseNotice.getBusinessNotices(business));
    }

}
