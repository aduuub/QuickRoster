package com.example.adam.quickroster;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Adam on 30/04/16.
 */
public class Shift implements Parcelable{

    long date;
    long startTime;
    long endTime;
    String details;

    public Shift(long d, long s, long e, String details){
        this.date = d;
        this.startTime = s;
        this.endTime = e;
        this.details = details;
    }

    protected Shift(Parcel in) {
        date = in.readLong();
        startTime = in.readLong();
        endTime = in.readLong();
        details = in.readString();
    }

    public static final Creator<Shift> CREATOR = new Creator<Shift>() {
        @Override
        public Shift createFromParcel(Parcel in) {
            return new Shift(in);
        }

        @Override
        public Shift[] newArray(int size) {
            return new Shift[size];
        }
    };

    public boolean onDay(int day, int month, int year){
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date(date));

        return (calendar.get(Calendar.DAY_OF_MONTH) == day)
                && (calendar.get(Calendar.MONTH) == month)
                && (calendar.get(Calendar.YEAR) == year);

    }
    public long getStartTime(){return this.startTime;}

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(date);
        dest.writeLong(startTime);
        dest.writeLong(endTime);
        dest.writeString(details);
    }
}
