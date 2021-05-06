/**
 * Created by Avishek Gurung on 2021-05-05
 */

package com.covimyn.search.services;

import com.covimyn.search.exceptions.DateFormatNotSupportedException;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Component
public class DateUtil {
    public static final String SUPPORTED_DATE_FORMAT = "yyyy_MM_dd_HH_mm_ss";
    public static final String DISPLAY_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DE_LIMITTER = "_";
    private static final String TIME_ZONE = "Asia/Kolkata";

    public Long dateToEpoch(String dateString) throws DateFormatNotSupportedException, ParseException {
        String dateSplit[] = dateString.split(DE_LIMITTER);
        if(dateSplit.length != 6) {
            throw new DateFormatNotSupportedException();
        }
        DateFormat dateFormat = new SimpleDateFormat(SUPPORTED_DATE_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        Long milliseconds = dateFormat.parse(dateString).getTime();
        return milliseconds;
    }

    public String epochToDate(Long milliseconds) {
        Date date = new Date(milliseconds);
        DateFormat dateFormat = new SimpleDateFormat(DISPLAY_DATE_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        String displayDate = dateFormat.format(date);
        return displayDate;
    }
}
