package com.haizhi.databridge.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

public final class CronUtils {
    private CronUtils() { }

    /**
     * 转为spring quarts的格式
     * @param linuxCrons 支持多个，以";"分割
     * @return
     */
    public static String toQuartsCron(String linuxCrons) {
        List<String> cronList = genCronList(linuxCrons).stream().map(CronUtils::toQuarts).collect(Collectors.toList());
        return String.join(";", cronList);
    }

    private static String toQuarts(String linuxCron) {
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

    /**
     * check spring quarts String
     * @param cronArray 支持多个，以";"分割
     * @return
     */
    public static boolean isValidExpression(String cronArray) {
        List<String> cronList = genCronList(cronArray);
        if (cronList == null || cronList.isEmpty()) {
            return false;
        }

        for (String cron : cronList) {
            if (!CronSequenceGenerator.isValidExpression(cron)) {
                return false;
            }
        }
        return true;
    }

    private static List<String> genCronList(String cronArray) {
        return Arrays.stream(cronArray.split(";"))
                .filter(x -> x.trim().length() > 0).collect(Collectors.toList());
    }
}
