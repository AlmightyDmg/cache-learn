package com.haizhi.databridge.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.haizhi.databridge.exception.DatabridgeException;

public final class CronUtils {
    private static long maxCronDiff = 3600000L;
    private static int weekPos = 4;

    private CronUtils() { }

    /**
     * 转为spring quarts的格式
     * @param linuxCrons 支持多个，以";"分割
     * @return
     */
    public static String toQuartsCron(String linuxCrons) {
        List<String> cronList = genCronList(linuxCrons).stream().map(CronUtils::toQuarts).collect(Collectors.toList());
        for (int i = 0; i < cronList.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (cronDiff(cronList.get(i), cronList.get(j)) <= maxCronDiff) {
                    throw new DatabridgeException("调度间隔必须要大于一个小时");
                }
            }
        }
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

            cronPartList.set(weekPos, String.valueOf(Integer.parseInt(cronPartList.get(weekPos)) + 1));
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

    private static long cronDiff(String cron1, String cron2) {
        if (ObjectUtils.nullSafeEquals(cron1, cron2)) {
            return 0L;
        }

        CronSequenceGenerator generator1 = new CronSequenceGenerator(cron1);
        CronSequenceGenerator generator2 = new CronSequenceGenerator(cron2);

        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(1);
        ZonedDateTime zdt = localDateTime.atZone(zoneId);

        Date fromDate = new Date();
        Date firstDate = generator1.next(fromDate);
        Date secondDate = generator2.next(firstDate);
        Date thirdDate = generator1.next(secondDate);
        //间隔毫秒数

        Long firstTime = firstDate.getTime();
        Long secondTime = secondDate.getTime();
        Long thirdTime = thirdDate.getTime();

        //间隔秒数
        long diff1 = secondTime - firstTime;
        long diff2 = thirdTime - secondTime;

        return Math.min(diff1, diff2);
    }
}
