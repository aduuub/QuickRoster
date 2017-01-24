package com.example.adam.quickroster;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NoticeBoardActivity extends AppCompatActivity {

    ListView noticesList;
    FloatingActionButton addDetailFab;
    List<String> notices;
    boolean editable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);

        noticesList = (ListView) findViewById(R.id.notice_board_list_view);
        addDetailFab = (FloatingActionButton) findViewById(R.id.addNoticeFab);
        addDetailFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNotice();
            }
        });
        ParseUser currentUser = ParseUser.getCurrentUser();
        this.editable = currentUser.getBoolean("isManager");

        // Only managers can add notices
        if(!currentUser.getBoolean("isManager"))
            addDetailFab.setVisibility(View.INVISIBLE);

        currentUser.getParseObject("Business").fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject business, ParseException e) {
                notices = business.getList("Notices");
                // If no notices in the cloud already init a new list
                if (notices == null)
                    notices = new ArrayList<>();

                business.put("Notices", notices);
                business.saveInBackground();
                setAdapter(notices);
            }
        });
    }

    public void setAdapter(List<String> notices) {
        this.noticesList.setAdapter(new NoticeBoardListAdapter(this, notices, editable));
    }


    private void addNotice() {
        notices = getAllCurrentNotices();
        notices.add("New Notice");
        noticesList.setAdapter(new NoticeBoardListAdapter(this, notices, editable));
    }


    private List<String> getAllCurrentNotices() {
        NoticeBoardListAdapter adapter = (NoticeBoardListAdapter) noticesList.getAdapter();
        notices = adapter.getNotices();
        if (notices == null)
            notices = new ArrayList<>();
        return notices;
    }


    /**
     * Saves the current notices
     */
    @Override
    public void onBackPressed() {
        notices = getAllCurrentNotices();
        final Set<String> noticesSet = new HashSet<>();
        noticesSet.addAll(notices);

        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.getParseObject("Business").fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject business, ParseException e) {
                business.addAllUnique("Notices", noticesSet);
                business.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null)
                            Toast.makeText(getApplicationContext(), "Sucessfully saved", Toast.LENGTH_LONG);
                        else
                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG);
                        finish();
                    }
                });
            }
        });
    }

//    /**
//     * Saves the current notices
//     */
//    @Override
//    public void onBackPressed() {
//        notices = getAllCurrentNotices();
//        final Set<String> noticesSet = new HashSet<>();
//        noticesSet.addAll(notices);
//
//        ParseUser currentUser = ParseUser.getCurrentUser();
//        currentUser.getParseObject("Business").fetchInBackground(new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject business, ParseException e) {
//                business.addAllUnique("Notices", noticesSet);
//                business.saveInBackground(new SaveCallback() {
//                    @Override
//                    public void done(ParseException e) {
//                        if (e == null)
//                            Toast.makeText(getApplicationContext(), "Sucessfully saved", Toast.LENGTH_LONG);
//                        else
//                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG);
//                        finish();
//                    }
//                });
//            }
//        });
//    }
}
