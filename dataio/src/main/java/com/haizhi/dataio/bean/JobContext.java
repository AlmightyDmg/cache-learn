package com.haizhi.dataio.bean;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

import com.haizhi.dataio.utils.IdUtils;

@Data
public class JobContext {
    Integer success = 0;
    Integer total = 0;
    String jobTaskId = IdUtils.genKey("ntask");
    Map<String, String> nextMaxValue = new HashMap<>();

    public void addSuccess() {
        success += 1;
    }
}
