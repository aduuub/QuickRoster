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

/**
 * Lets the user edit or add a notice.
 *
 * StringExtra - objectId - The objectId of the ParseNotice to edit. If null, it will go into adding a new notice, rather than editing.
 *
 * @author Adam Wareing
 */
public class AddEditNoticeActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText mTitleEditText;
    private EditText mMessageEditText;

    private ParseNotice notice; // notice we are editing

    /**
     * Are we editing or adding a notice?
     */
    private MODIFYING_OPTIONS state;


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

    /**
     * Sets the fields for the existing ParseNotice sets whether we are adding or editing the notice.
     *
     * If editing, set the old title/message into the EditTexts.
     */
    private void fillTextFields() {
        Intent intent = getIntent();
        String objectId = intent.getStringExtra("objectId");

        if (objectId != null) {
            // We must be editing a notice
            state = MODIFYING_OPTIONS.EDITING;
            mToolbar.setTitle("Edit Notice");

            // Find the title and message
            notice = ParseNotice.getNoticeFromId(objectId);
            String title = notice.getTitle();
            String message = notice.getMessage();

            if (title != null)
                mTitleEditText.setText(title);

            if (message != null)
                mMessageEditText.setText(message);

        } else {
            // Adding notice
            state = MODIFYING_OPTIONS.ADDING;
            mToolbar.setTitle("Add Notice");
        }
    }


    /**
     * Save the notice to Parse.
     * Saves the notice to the current users business.
     */
    private void saveNotice() {
        String title = mTitleEditText.getText().toString();
        String message = mMessageEditText.getText().toString();

        if (title.equals("") || message.equals("")) {
            displayInputAlert();
            return;
        }

        ParseBusiness business = ((ParseStaffUser)ParseUser.getCurrentUser()).getBusiness();
        Log.i("businessID", business.getObjectId());

        if (state == MODIFYING_OPTIONS.ADDING) {
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
        AlertDialog alertDialog = new AlertDialog.Builder(AddEditNoticeActivity.this).create();
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

    /**
     * State the activity can be in.
     */
    private enum MODIFYING_OPTIONS {
        EDITING, ADDING
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
}
