package com.ccabank.signservice.provider.file.util;

/**
 * @author : <a href="mailto:patrick.simo@cca-bank.com">Patrick SIMO</a>
 * @project : entity-service
 * @Package : com.ccabank.userservice.provider.file.util
 * <p>
 * @date: 19/06/2023
 * @time: 09:51
 * <p>
 * Created with IntelliJ IDEA To change this template use File | Settings | File Templates.
 */
public class DateUtil {

    /**
     * The function returns the current time in milliseconds.
     *
     * @return The method `GetCurrentTimeMillis()` returns the current time in milliseconds since
     * January 1, 1970, 00:00:00 GMT, as a `Long` data type.
     */
    public static Long GetCurrentTimeMillis() {
        return System.currentTimeMillis();
    }
}
