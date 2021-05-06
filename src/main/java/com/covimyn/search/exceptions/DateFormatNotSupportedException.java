/**
 * Created by Avishek Gurung on 2021-05-05
 */

package com.covimyn.search.exceptions;

import com.covimyn.search.services.DateUtil;

public class DateFormatNotSupportedException extends RuntimeException {
    public DateFormatNotSupportedException() {
        super("Date format not supported. Supported format is " + DateUtil.SUPPORTED_DATE_FORMAT);
    }
}
