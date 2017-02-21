package com.example.adam.quickroster.misc;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Adam on 22/02/17.
 */

public class Util {

    public static Calendar getCurrentDayCalendar(){
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal;
    }
}
