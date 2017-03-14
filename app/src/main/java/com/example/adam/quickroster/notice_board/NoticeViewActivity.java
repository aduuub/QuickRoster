package com.example.adam.quickroster.notice_board;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.adam.quickroster.R;
import com.example.adam.quickroster.model.ParseNotice;
import com.parse.ParseUser;


/**
 * Displays the ParseNotice Title and Message.
 * Option to click on the MenuItem to edit the Notice in AddEditNoticeActivity.
 *
 * @NonNull StringExtra - objectId - The objectId of the ParseNotice to display
 * @see ParseNotice
 * @author Adam Wareing
 */
public class NoticeViewActivity extends AppCompatActivity {

    private TextView mTitleEditText;
    private TextView mMessageEditText;

    /**
     * ParseNotices objectId
     */
    private String objectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitleEditText = (TextView) findViewById(R.id.notice_edit_title);
        mMessageEditText = (TextView) findViewById(R.id.notice_edit_message);

        fillTextFields();
        toolbar.setTitle("Notice");
        setSupportActionBar(toolbar);
    }

    /**
     * Get the objectId of the notice, fetch from Parse and set the title and message in the EditTexts.
     */
    private void fillTextFields() {
        Intent intent = getIntent();
        objectId = intent.getStringExtra("objectId");
        if (objectId != null) {
            // Find the title and message
            ParseNotice notice = ParseNotice.getNoticeFromId(objectId);
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

    /**
     * Start the new Activity to edit the notice.
     */
    private void editNotice() {
        Intent intent = new Intent(this, AddEditNoticeActivity.class);
        intent.putExtra("objectId", objectId);
        startActivity(intent);
    }
}
