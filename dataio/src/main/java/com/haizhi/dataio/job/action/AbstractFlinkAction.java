// CHECKSTYLE:OFF
package com.haizhi.dataio.job.action;

import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;


/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年06月02日 13:59:58
 */
@Slf4j
public abstract class AbstractFlinkAction<T, D, U> implements IAction<T> {
    protected abstract void begin(D info);
    protected abstract void end(D info, boolean success, String errmsg);

    protected abstract void beforeExec(U unit) throws Exception;
    protected abstract String execute(U unit);
    protected abstract String checkResult(U unit);
    protected abstract void afterExec(U unit, boolean success);

    protected abstract List<U> getExecUnitList(D info);
    protected abstract D getJobDetail(T jobParam);

    @Override
    public void doAction(T actionInfo) throws Exception {
        // 获取job详情
        D detail = getJobDetail(actionInfo);
        LinkedList<U> runningUnit = new LinkedList<>();
        try {
            // 准备执行job
            begin(detail);

            // 获取job中包含的执行单元列表
            List<U> execUnits = getExecUnitList(detail);
            int unitCount = execUnits.size();

            // 遍历执行
            execUnits.forEach(unit -> {
                try {
                    // 每个单元执行前的准备
                    beforeExec(unit);

                    // 执行
                    execute(unit);
                } catch (Exception e) {
                    log.error("execute flink task error.", e);
                } finally {
                    // 加入正在执行的列表中
                    runningUnit.offer(unit);
                }
            });


            // 定时检查更新执行的结果
            boolean totalSuccess = true;
            while (true) {
                LinkedList<U> unfinished = new LinkedList<>();
                while (!runningUnit.isEmpty()) {
                    String state = "";
                    try {
                        U unit = runningUnit.poll();

                        // 调用接口去检查执行的task的状态
                        state = checkResult(unit);
                        boolean isSuccess = true;

                        if ("finished".equalsIgnoreCase(state) || "failed".equalsIgnoreCase(state)) {
                            // task结束后的处理
                            afterExec(unit, isSuccess);
                            isSuccess = "finished".equalsIgnoreCase(state);
                            unitCount--;
                        } else {
                            unfinished.add(unit);
                        }

                        if (!isSuccess) {
                            totalSuccess = false;
                        }
                    } catch (Exception e) {
                        log.error("", e);
                        unitCount--;
                    }
                }

                // 若全部执行单元都执行完成后，推出定时检查流程
                if (unitCount <= 0) {
                    break;
                }

                runningUnit.addAll(unfinished);
                Thread.sleep(FlinkAction.SLEEP_TIME);
            }

            // 整个Job结束后的处理
            end(detail, totalSuccess, "success");
        } catch (Exception e) {
            log.error("sync failed", e);
            end(detail, false, e.getMessage());
        }
    }
}
