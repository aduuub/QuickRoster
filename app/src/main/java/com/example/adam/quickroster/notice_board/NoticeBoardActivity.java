package com.example.adam.quickroster.notice_board;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adam.quickroster.R;
import com.parse.GetCallback;
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
    FloatingActionButton addDetailFab;
    List<String> notices;
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
        addDetailFab = (FloatingActionButton) view.findViewById(R.id.addNoticeFab);
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

        return view;
    }

    public void setAdapter(List<String> notices) {
        NoticeBoardListAdapter adapter = new NoticeBoardListAdapter(getActivity(),
                R.layout.content_notice_board_list_adapter, notices);
        adapter.setEditable(editable);
        this.noticesList.setAdapter(adapter);
    }


    private void addNotice() {
        notices = getAllCurrentNotices();
        notices.add("New Notice");
        //TODO
        // noticesList.setAdapter(new NoticeBoardListAdapter(getActivity(), notices, editable));
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
     *
     * // TODO make this work in the activity
     */
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
                            Toast.makeText(getActivity().getApplicationContext(), "Sucessfully saved", Toast.LENGTH_LONG);
                        else
                            Toast.makeText(getActivity().getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG);
                        getActivity().finish();
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
