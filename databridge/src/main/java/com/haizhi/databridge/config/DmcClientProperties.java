package com.haizhi.databridge.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.haizhi.dataclient.dataconfig.dmc.DmcConfig;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年05月27日 10:59:01
 */
@Component
@ConfigurationProperties(prefix = "localdmc")
@Data
public class DmcClientProperties {
    private DmcConfig.ServicePropertieBean mobius;
    private DmcConfig.ServicePropertieBean overlord;
    private DmcConfig.ServicePropertieBean behemoth;
    private DmcConfig.ServicePropertieBean pentagon;
    private DmcConfig.ServicePropertieBean noah;
}
