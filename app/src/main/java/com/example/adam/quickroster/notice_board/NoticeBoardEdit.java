package com.example.adam.quickroster.notice_board;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.adam.quickroster.R;

public class NoticeBoardEdit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board_edit);
        fillTextFields();
    }

    private void fillTextFields() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");

        if(title != null || message != null){
            EditText titleEditText = (EditText) findViewById(R.id.notice_edit_title);
            titleEditText.setText(title);

            EditText messageEditText = (EditText) findViewById(R.id.notice_edit_message);
            messageEditText.setText(message);
        }
    }


}
