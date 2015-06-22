package ase.shared;

import java.text.SimpleDateFormat;

/**
 * Created by Michael on 21.06.2015.
 */
public class Constants {

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String TIME_FORMAT = "HH:mm:ss";

    //yyyy-MM-dd'T'HH:mm:ss.SSSZ
    public static final String DATE_TIME_FORMAT = DATE_FORMAT + "'T'" + TIME_FORMAT + ".SSSZ";

    public static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_THREAD_LOCAL = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
        }
    };
}
