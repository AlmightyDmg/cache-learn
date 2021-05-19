package com.haizhi.databridge.web.controller;

import com.haizhi.databridge.client.XxlJobClient;
import com.haizhi.databridge.web.result.WebResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private XxlJobClient jobClient;

    @GetMapping("/pageList")
    public WebResult pageList(@RequestParam int start, @RequestParam int length) {
        return WebResult.of(jobClient.pageList(XxlJobClient.PageQueryParam.builder()
                .start(start).length(length).jobGroup(2).triggerStatus(-1).jobDesc(null)
                .build()));
    }

    @PostMapping("/add")
    public WebResult addJob(@RequestBody XxlJobClient.XxlJobInfo jobInfo) {
        return WebResult.of(jobClient.add(jobInfo));
    }

    @PostMapping("/remove")
    public WebResult addJob(@RequestParam int jobId) {
        return WebResult.of(jobClient.remove(jobId));
    }

    @PostMapping("/trigger")
    public WebResult triggerJob(@RequestParam int jobId) {
        return WebResult.of(jobClient.trigger(jobId));
    }
}
