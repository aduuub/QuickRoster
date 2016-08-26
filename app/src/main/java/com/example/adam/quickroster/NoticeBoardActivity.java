package com.example.adam.quickroster;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NoticeBoardActivity extends AppCompatActivity {

    ListView noticesList;
    FloatingActionButton addDetailFab;

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

        ParseObject business = currentUser.getParseObject("Business");
        List<String> notices = business.getList("Notices");
        if (notices == null)
            notices = new ArrayList<>();
        notices.addAll(Arrays.asList("New"));
        business.saveInBackground();
        noticesList.setAdapter(new NoticeBoardListAdapter(this, notices));
    }


    private void addNotice() {
        List<String> notices = getAllCurrentNotices();
        notices.add("");
        noticesList.setAdapter(new NoticeBoardListAdapter(this, notices));
    }


    private List<String> getAllCurrentNotices() {
        NoticeBoardListAdapter adapter = (NoticeBoardListAdapter) noticesList.getAdapter();
        List<String> notices = adapter.getAllNotices();
        if (notices == null)
            notices = new ArrayList<>();
        return notices;
    }


    /**
     * Saves the current notices
     */
    @Override
    public void onBackPressed() {

        List<String> notices = getAllCurrentNotices();

        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseObject business = currentUser.getParseObject("Business");
        business.addAll("Notices", notices);
        business.saveInBackground();
        finish();
    }

}
