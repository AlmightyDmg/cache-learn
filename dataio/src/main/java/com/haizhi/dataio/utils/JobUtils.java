package com.haizhi.dataio.utils;

import com.haizhi.dataio.bean.JobContext;

public final class JobUtils {
    private JobUtils() { }
    private static ThreadLocal<JobContext> contextThreadLocal = new ThreadLocal<>();

    public static JobContext cntx() {
        return contextThreadLocal.get();
    }

    public static void initContext() {
        contextThreadLocal.set(new JobContext());
    }

    public static void finiContext() {
        contextThreadLocal.remove();
    }
}
