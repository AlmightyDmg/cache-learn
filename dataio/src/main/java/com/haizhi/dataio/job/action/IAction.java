package com.haizhi.dataio.job.action;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年05月27日 10:44:00
 */
public interface IAction<T> {
    void doAction(T actionInfo) throws Exception;
}
