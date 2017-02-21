package com.example.adam.quickroster.notice_board;

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
import com.example.adam.quickroster.misc.ParseUtil;
import com.example.adam.quickroster.model.ParseBusiness;
import com.example.adam.quickroster.model.ParseNotice;
import com.example.adam.quickroster.model.ParseStaffUser;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

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
        this.editable = currentUser.isManager();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Notices");
        setHasOptionsMenu(true);

        ParseBusiness business = currentUser.getBusiness();
        notices = ParseNotice.getBusinessNotices(business);
        setAdapter(notices);
        setHasOptionsMenu(true);

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
            Intent intent = new Intent(getActivity(), NoticeView.class);
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
            Intent intent = new Intent(getContext(), NoticeEdit.class);
            startActivity(intent);
            return true;
        }
        return false;
    }


    public void updateNoticesAndAdapter(){
        ParseBusiness business = ParseUtil.getCurrentUser().getBusiness();
        setAdapter(ParseNotice.getBusinessNotices(business));
    }

}
