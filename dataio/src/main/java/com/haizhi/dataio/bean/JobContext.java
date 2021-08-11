package com.haizhi.dataio.bean;

import lombok.Data;

import com.haizhi.dataio.utils.IdUtils;

@Data
public class JobContext {
    Integer success = 0;
    Integer total = 0;
    String jobTaskId = IdUtils.genKey("ntask");

    public void addSuccess() {
        success += 1;
    }
}
