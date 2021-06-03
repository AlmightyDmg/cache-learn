package com.haizhi.dataio.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.haizhi.dataio.bean.TableTaskInfo;
import com.haizhi.dataio.utils.JsonUtils;

/**
 * @author duanxiaoyi
 * @Description send message to mq
 * @createTime 2021年05月20日 17:03:56
 */
@Component
public class MqSender {

    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;

    @Value("${mq.topics.data-table-sync-status}")
    private String tableStatusSyncTopic;

    public void syncTableStatus(TableTaskInfo tableTaskInfo) {
        kafkaTemplate.send(tableStatusSyncTopic, tableTaskInfo.getTableName(), JsonUtils.toJson(tableTaskInfo));
    }
}
