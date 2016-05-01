package com.example.adam.quickroster;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Adam on 30/04/16.
 */
public class User implements Parcelable {
    private String userName;
    private String password;
    private String name;
    private boolean isManager;
    private ArrayList<Shift> shifts;

    /**
     *
     */
    public User(String un, String pw, String name, boolean manager, ArrayList<Shift> shifts) {
        this.userName = un;
        this.password = pw;
        this.name = name;
        this.isManager = manager;
        if (shifts != null)
            this.shifts = shifts;
        else
            this.shifts = new ArrayList<Shift>();
    }


    protected User(Parcel in) {
        userName = in.readString();
        password = in.readString();
        name = in.readString();
        isManager = in.readByte() != 0;
        shifts = new ArrayList<>();
        in.readTypedList(shifts, Shift.CREATOR);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public boolean validLogin(String un, String pw) {
        return un.equals(userName) && pw.equals(password);
    }

    public ArrayList<Shift> getShiftsOnDay(int day, int month, int year) {

        ArrayList<Shift> shiftsOnDay = new ArrayList<Shift>();

        for (Shift s : shifts) {
            if (s.onDay(day, month, year)) {
                shiftsOnDay.add(s);
            }
        }

        return shiftsOnDay;

    }

    public void addShift(long date, long startTime, long endTime, String details) {
        Shift newShift = new Shift(date, startTime, endTime, details);
        shifts.add(newShift);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(password);
        dest.writeString(name);
        dest.writeByte((byte) (isManager ? 1 : 0));
        dest.writeTypedList(shifts);

    }

    public boolean isManager() {
        return this.isManager;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Shift> getShifts() {
        return shifts;
    }

    public void removeShift(Shift s) {
        if (shifts.contains(s))
            shifts.remove(s);
    }
}
