package com.haizhi.databridge.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

public final class CronUtils {
    private CronUtils() { }
    public static String toQuartsCron(String linuxCron) {
        if (ObjectUtils.isEmpty(linuxCron)) {
            return linuxCron;
        }

        String linuxCronNoSpace = linuxCron.trim();
        char lastChar = linuxCronNoSpace.charAt(linuxCronNoSpace.length() - 1);
        if (lastChar == '*') {
            return "0 " + linuxCronNoSpace.substring(0, linuxCronNoSpace.length() - 1) + "?";
        } else {
            List<String> cronPartList = Arrays.stream(linuxCronNoSpace.split(" ")).map(String::trim)
                    .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.toList());
            cronPartList.set(4, String.valueOf(Integer.parseInt(cronPartList.get(4)) + 1));
            cronPartList.set(2, "?");
            cronPartList.add(0, "0");

            return String.join(" ", cronPartList);
        }
    }
}
