package com.example.adam.quickroster.notice_board;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.adam.quickroster.R;
import com.example.adam.quickroster.model.ParseBusiness;
import com.example.adam.quickroster.model.ParseNotice;
import com.example.adam.quickroster.model.ParseStaffUser;
import com.parse.ParseException;
import com.parse.ParseUser;

public class NoticeEdit extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText mTitleEditText;
    private EditText mMessageEditText;

    private ParseNotice notice;
    private boolean addingNotice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board_edit);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitleEditText = (EditText) findViewById(R.id.notice_edit_title);
        mMessageEditText = (EditText) findViewById(R.id.notice_edit_message);

        fillTextFields();
        setSupportActionBar(mToolbar);
    }

    private void fillTextFields() {
        Intent intent = getIntent();
        String objectId = intent.getStringExtra("objectId");

        if (objectId != null) {
            // We must be editing a notice
            addingNotice = false;
            mToolbar.setTitle("Edit Notice");

            // Find the title and message
            notice = ParseNotice.getNoticeFromID(objectId);
            String title = notice.getTitle();
            String message = notice.getMessage();

            if (title != null)
                mTitleEditText.setText(title);

            if (message != null)
                mMessageEditText.setText(message);

        } else {
            // Adding notice
            addingNotice = true;
            mToolbar.setTitle("Add Notice");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (ParseUser.getCurrentUser().getBoolean("isManager")) {
            getMenuInflater().inflate(R.menu.done_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_icon_done) {
            saveNotice();
        }
        return true;
    }


    private void saveNotice() {
        String title = mTitleEditText.getText().toString();
        String message = mMessageEditText.getText().toString();

        if (title.equals("") || message.equals("")) {
            displayInputAlert();
            return;
        }

        ParseBusiness business = ((ParseStaffUser)ParseUser.getCurrentUser()).getBusiness();
        Log.i("businessID", business.getObjectId());

        if (addingNotice) {
            notice = new ParseNotice();
            notice.put("business", business);
        }
        notice.setTitle(title);
        notice.setMessage(message);
        try {
            notice.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        finish();
    }

    /**
     * Alerts the user that the input is invalid.
     */
    private void displayInputAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(NoticeEdit.this).create();
        alertDialog.setTitle(getString(R.string.invalid_input_title));
        alertDialog.setMessage(getString(R.string.invalid_input_message));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
