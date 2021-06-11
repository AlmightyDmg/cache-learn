package com.haizhi.dataclient.connection.dmc.client;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Retrofit;
import retrofit2.converter.JacksonParamConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import com.haizhi.dataclient.connection.dmc.client.common.SynchronousCallAdapterFactory;
import com.haizhi.dataclient.connection.dmc.client.endpoint.EndpointClient;
import com.haizhi.dataclient.connection.dmc.client.mobius.MobiusClient;
import com.haizhi.dataclient.connection.dmc.client.noah.NoahClient;
import com.haizhi.dataclient.connection.dmc.client.pentagon.PentagonClient;
import com.haizhi.dataclient.dataconfig.dmc.DmcConfig;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年06月07日 14:02:31
 */
@Slf4j
public final class DmcClientUtils {
    private static final long DEFAULT_CONNECT_TIMEOUT = 60000;
    private static final long DEFAULT_CALL_TIMEOUT = 60000;
    private static final long DEFAULT_READ_TIMEOUT = 60000;

    private DmcClientUtils() {

    }

    public static EndpointClient endpointClient(DmcConfig.ServicePropertieBean properties) {
        return createClient("endpoint", properties, EndpointClient.class);
    }

    public static MobiusClient mobiusClient(DmcConfig.ServicePropertieBean properties) {
        return createClient("mobius", properties, MobiusClient.class);
    }

    public static NoahClient noahClient(DmcConfig.ServicePropertieBean properties) {
        return createClient("noah", properties, NoahClient.class);
    }

    public static PentagonClient pentagonClient(DmcConfig.ServicePropertieBean properties) {
        return createClient("pentagon", properties, PentagonClient.class);
    }

    public static <T> T createClient(String serviceName, DmcConfig.ServicePropertieBean properties, Class<T> clazz) {
        Retrofit retrofit = checkAndBuildRetrofit(serviceName, properties);
        return create(retrofit, clazz);
    }

    public static <T> T create(Retrofit retrofit, Class<T> serviceClazz) {
        return retrofit.create(serviceClazz);
    }

    private static Retrofit checkAndBuildRetrofit(String name, DmcConfig.ServicePropertieBean prop) {
//        LOGGER.info("config {} client:{}", name, prop);
        // check 参数
        if (prop != null) {
            if (StringUtils.isEmpty(prop.getUrl())) {
                log.error(name + " url required");
            }
        }

        OkHttpClient.Builder okHttpBuider = new OkHttpClient.Builder()
                .connectTimeout(
                        prop.getConnectTimeoutMs() != null ? prop.getConnectTimeoutMs() : DEFAULT_CONNECT_TIMEOUT,
                        TimeUnit.MILLISECONDS)
                .callTimeout(prop.getCallTimeoutMs() != null ? prop.getCallTimeoutMs() : DEFAULT_CALL_TIMEOUT,
                        TimeUnit.MILLISECONDS)
                .readTimeout(prop.getReadTimeoutMs() != null ? prop.getReadTimeoutMs() : DEFAULT_READ_TIMEOUT,
                        TimeUnit.MILLISECONDS);
        okHttpBuider.addInterceptor(new ResultTransferInterceptor());
        if (Boolean.TRUE.equals(prop.getLogging())) {
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpBuider.addInterceptor(logInterceptor);
        }

        return new Retrofit.Builder().baseUrl(prop.getUrl()).client(okHttpBuider.build())
                .addConverterFactory(JacksonConverterFactory.create())
                .addConverterFactory(JacksonParamConverterFactory.create())
                .addCallAdapterFactory(SynchronousCallAdapterFactory.create(prop.getErrorThrow())).build();
    }
}
