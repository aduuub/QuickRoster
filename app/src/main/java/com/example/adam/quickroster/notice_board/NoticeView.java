package com.example.adam.quickroster.notice_board;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adam.quickroster.R;
import com.example.adam.quickroster.misc.ParseUtil;
import com.example.adam.quickroster.model.ParseBusiness;
import com.example.adam.quickroster.model.ParseNotice;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by Adam on 22/02/17.
 */

public class NoticeView extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mTitleEditText;
    private TextView mMessageEditText;

    private ParseNotice notice;
    private String objectId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board_view);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitleEditText = (TextView) findViewById(R.id.notice_edit_title);
        mMessageEditText = (TextView) findViewById(R.id.notice_edit_message);

        fillTextFields();
        mToolbar.setTitle("Notice");
        setSupportActionBar(mToolbar);
    }

    private void fillTextFields() {
        Intent intent = getIntent();
        objectId = intent.getStringExtra("objectId");
        if (objectId != null) {
            // Find the title and message
            notice = ParseNotice.getNoticeFromID(objectId);
            String title = notice.getTitle();
            String message = notice.getMessage();

            if (title != null)
                mTitleEditText.setText(title);

            if (message != null)
                mMessageEditText.setText(message);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (ParseUser.getCurrentUser().getBoolean("isManager")) {
            getMenuInflater().inflate(R.menu.edit_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_icon_edit) {
            editNotice();
        }
        return true;
    }

    private void editNotice() {
        Intent intent = new Intent(this, NoticeEdit.class);
        intent.putExtra("objectId", objectId);
        startActivity(intent);
    }
}
