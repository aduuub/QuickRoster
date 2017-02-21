package com.example.adam.quickroster;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.adam.quickroster.misc.ParseUtil;
import com.example.adam.quickroster.model.ParseStaffUser;


public class AccountFragment extends Fragment {

    private TextInputEditText mHourlyWageEditText;
    private Switch mAutoAddToCalSwitch;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Account");
        View view = inflater.inflate(R.layout.activity_account, container, false);
        setInputs(view);
        return view;
    }

    /**
     * Sets the hourly wage and switch to contain the correct values that are stored in the db.
     * Also sets the listeners and updates them when clicked.
     *
     * @param view
     */
    private void setInputs(View view){
        final ParseStaffUser currentUser = ParseUtil.getCurrentUser();

        // set the hourly wage box
        mHourlyWageEditText = (TextInputEditText) view.findViewById(R.id.hourly_wage_text_box);
        mHourlyWageEditText.setText(ParseUtil.getCurrentUser().getHourlyWage());
        mHourlyWageEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String wage = v.getText().toString();
                    currentUser.setHourlyWage(wage);
                    return true;
                }
                return false;
            }
        });

        // set the auto add to calendar switch
        mAutoAddToCalSwitch = (Switch) view.findViewById(R.id.auto_add_to_cal_switch);
        boolean autoAdd = currentUser.getAutoAddToCalendar();
        mAutoAddToCalSwitch.setChecked(autoAdd);
        mAutoAddToCalSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currentUser.setAutoAddToCalendar(isChecked);
            }
        });
    }
}
