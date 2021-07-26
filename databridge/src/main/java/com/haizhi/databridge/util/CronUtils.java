package com.haizhi.databridge.util;

import org.springframework.util.ObjectUtils;

public class CronUtils {
    public static String toQuartsCron(String linuxCron) {
        if (ObjectUtils.isEmpty(linuxCron)) {
            return linuxCron;
        }

        String linuxCronNoSpace = linuxCron.trim();
        char lastChar = linuxCronNoSpace.charAt(linuxCronNoSpace.length()-1);
        if (lastChar == '*') {
            return "0 " + linuxCronNoSpace.substring(0, linuxCronNoSpace.length()-1) + "?";
        }

        return "0 " + linuxCronNoSpace;
    }
}
