package com.haizhi.dataclient.dataconfig.dmc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.haizhi.dataclient.dataconfig.DataConfig;
import com.haizhi.dataclient.datasource.DataSource;
import com.haizhi.dataclient.datasource.dmc.DmcDataSource;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年06月05日 17:08:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DmcConfig implements DataConfig<DataSource> {
    private ServicePropertieBean mobiusProp;
    private ServicePropertieBean pentagonProp;
    private ServicePropertieBean noahProp;
    private ServicePropertieBean tassadarProp;
    private ServicePropertieBean pandoraProp;


    @Override
    public DmcDataSource buildDataSource() {
        return new DmcDataSource(this);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ServicePropertieBean {

        private String url;
        private Integer connectTimeoutMs;
        private Integer callTimeoutMs;
        private Integer readTimeoutMs;
        private Boolean enabled = true;
        private Boolean logging = false;
        //调用返回的status状态不符合要求的 抛出异常
        private Boolean errorThrow = true;
    }

    public enum ServiceType {
        Noah,
        Pentagon,
        Mobius,
        Hora
    }
}
