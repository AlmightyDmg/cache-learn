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
    protected abstract void end(D info, int success, String errmsg);

    protected abstract void beforeExec(U unit) throws Exception;
    protected abstract String execute(U unit);
    protected abstract boolean checkResult(U unit);
    protected abstract void afterExec(U unit, boolean success, String errorMsg);
    protected abstract String cancel(U unit);

    protected abstract List<U> getExecUnitList(D info);
    protected abstract D getJobDetail(T jobParam);

    @Override
    public void doAction(T actionInfo) throws Exception {
        // 获取job详情
        D detail = getJobDetail(actionInfo);
        LinkedList<U> runningUnit = new LinkedList<>();
        try {
            StringBuilder error = new StringBuilder();
            // 准备执行job
            begin(detail);

            // 获取job中包含的执行单元列表
            List<U> execUnits = getExecUnitList(detail);
            int unitCount = execUnits.size();

            // 遍历执行启动flink任务
            for (U unit : execUnits) {
                try {
                    // 每个单元执行前的准备
                    beforeExec(unit);

                    // 启动任务
                    execute(unit);

                    // 加入正在执行的列表中
                    runningUnit.offer(unit);
                } catch (InterruptedException e) {
                    throw e;
                } catch (Exception e) {
                    log.error("execute flink task error.", e);
                    unitCount--;
                    afterExec(unit, false, e.getMessage());
                    error.append(e.getMessage()).append("\r\n");
                }
            }

            // 定时检查更新执行的结果
            while (true) {
                LinkedList<U> unfinished = new LinkedList<>();
                while (!runningUnit.isEmpty()) {
                    U unit = runningUnit.poll();
                    try {
                        // 若执行成功退出，否则继续加入进行检查
                        if (checkResult(unit)) {
                            unitCount--;
                            afterExec(unit, true, "success");
                        } else {
                            unfinished.add(unit);
                        }

                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        throw e;
                    } catch (Exception e) {
                        log.error("", e);
                        String errorMsg = e.getMessage();
                        error.append(errorMsg).append("\r\n");
                        unitCount--;

                        afterExec(unit, false, errorMsg);
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
            end(detail, error.length() == 0 ? 2 : 1, error.length() == 0 ? "success" : error.toString());
        } catch (InterruptedException e) {
            for (U u : runningUnit) {
                cancel(u);
            }
            end(detail, 3, "cancel the job");
            throw e;
        } catch (Exception e) {
            log.error("sync failed", e);
            end(detail, 1, e.getMessage());
        }
    }
}
