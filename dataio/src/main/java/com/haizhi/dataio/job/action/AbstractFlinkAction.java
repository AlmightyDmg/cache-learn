package com.haizhi.dataio.job.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;


/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年06月02日 13:59:58
 */
@Slf4j
public abstract class AbstractFlinkAction<T, D, U> implements IAction<T> {
    Map<U, String> resultMap = new HashMap<>();

    protected abstract void begin(D info);
    protected abstract void end(D info, boolean success);

    protected abstract void beforeExec(U unit) throws Exception;
    protected abstract String execute(U unit);
    protected abstract boolean checkResult(U unit, String jobId);
    protected abstract void afterExec(U unit, boolean success);


    protected abstract List<U> getExecUnitList(D info);
    protected abstract D getJobDetail(T jobParam);

    @Override
    public void doAction(T actionInfo) throws Exception {
        D detail = getJobDetail(actionInfo);
        try {
            begin(detail);

            List<U> execUnits = getExecUnitList(detail);
            execUnits.forEach(unit -> {
                String flinkTaskId = "";
                try {
                    beforeExec(unit);
                    flinkTaskId = execute(unit);
                } catch (Exception e) {
                    log.error("execute flink task error.", e);
                }

                resultMap.put(unit, flinkTaskId);
            });

            resultMap.forEach((key, value) -> {
                boolean success = false;
                try {
                    success = checkResult(key, value);
                } catch (Exception e) {
                    log.error("");
                }
                afterExec(key, success);
            });

            end(detail, true);
        } catch (Exception e) {
            log.error("sync failed", e);
            end(detail, true);
            throw e;
        }
    }
}
