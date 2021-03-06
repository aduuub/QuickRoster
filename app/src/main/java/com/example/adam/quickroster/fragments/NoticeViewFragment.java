package com.example.adam.quickroster.fragments;

import android.content.Intent;
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
import android.widget.ListView;

import com.example.adam.quickroster.R;
import com.example.adam.quickroster.model.ParseBusiness;
import com.example.adam.quickroster.model.ParseNotice;
import com.example.adam.quickroster.model.ParseStaffUser;
import com.example.adam.quickroster.notice_board.AddEditNoticeActivity;
import com.example.adam.quickroster.notice_board.NoticeAdapter;
import com.example.adam.quickroster.notice_board.NoticeViewActivity;
import com.parse.ParseUser;

import java.util.List;

public class NoticeViewFragment extends Fragment {

    private ListView noticesList;
    private List<ParseNotice> notices;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_notice_board, container, false);

        noticesList = (ListView) view.findViewById(R.id.notice_board_list_view);

        ParseStaffUser currentUser = (ParseStaffUser) ParseUser.getCurrentUser();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Notices");
        setHasOptionsMenu(true);

        ParseBusiness business = currentUser.getBusiness();
        notices = ParseNotice.getBusinessNotices(business);
        setAdapter(notices);
        setHasOptionsMenu(true);

        return view;
    }


    private void setAdapter(List<ParseNotice> notices) {
        NoticeAdapter adapter = new NoticeAdapter(getActivity(),
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
            Intent intent = new Intent(getActivity(), NoticeViewActivity.class);
            intent.putExtra("objectId", notice.getObjectId());
            startActivity(intent);
            updateNoticesAndAdapter();
        }
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        if(ParseUser.getCurrentUser().getBoolean("isManager")) {
            inflater.inflate(R.menu.add_menu, menu);
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_icon_add){
            Intent intent = new Intent(getContext(), AddEditNoticeActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }


    private void updateNoticesAndAdapter(){
        ParseBusiness business = ((ParseStaffUser)ParseUser.getCurrentUser()).getBusiness();
        setAdapter(ParseNotice.getBusinessNotices(business));
    }

}
