// CHECKSTYLE:OFF
package com.haizhi.dataio.client.databridge;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;

import com.github.lianjiatech.retrofit.spring.boot.interceptor.BasePathMatchInterceptor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;
import com.haizhi.dataclient.exception.SDKException;
import com.haizhi.dataio.utils.JsonUtils;

/**
 * @author duanxiaoyi
 * @Description xxl login
 * @createTime 2021年05月19日 15:24:55
 */
@Slf4j
public class WebResultInterceptor extends BasePathMatchInterceptor {
    public static final String LOGIN_IDENTITY_KEY = "XXL_JOB_LOGIN_IDENTITY";
    private static final String HEADER_COOKIE = "Cookie";
    private static final String SET_COOKIE_HEADER = "Set-Cookie";

    @Override
    protected okhttp3.Response doIntercept(Chain chain) throws IOException {
        okhttp3.Response response = chain.proceed(chain.request());

        if (response.code() == 200) {
            ResponseBody responseBody = response.body();
            MediaType contentType = responseBody != null ? responseBody.contentType() : null;
            if (contentType != null && contentType.subtype().equals("json")) {
                String bodyString = getBody(response);

                WebResult<String> demo = new WebResult<>();
                WebResult<String> result = JsonUtils.toObject(bodyString, demo.getClass());
                if (!"0".equals(result.getStatus())) {
                    throw new SDKException(result.getErrstr());
                }

                String newBodyStr = null;
                try {
                    newBodyStr = JsonUtils.toJson(result.getResult());
                } catch (Exception e) {

                }

                if (newBodyStr == null) {
                    if (result.getResult() == null) {
                        newBodyStr = "";
                    } else {
                        newBodyStr = String.valueOf(result.getResult());
                    }
                }
                ResponseBody body = ResponseBody.create(contentType, newBodyStr);
                response = response.newBuilder().body(body).build();
            }
        }

        return response;
    }

    /**
     * 获取body内容，部分代码引用HttpLoggingInterceptor
     */
    private String getBody(Response response) throws IOException {
        ResponseBody responseBody = response.body();
        Headers headers = response.headers();
        BufferedSource source = responseBody.source();
        source.request(9223372036854775807L);
        Buffer buffer = source.getBuffer();
        if ("gzip".equalsIgnoreCase(headers.get("Content-Encoding"))) {
            GzipSource gzippedResponseBody = new GzipSource(buffer.clone());
            Throwable var24 = null;

            try {
                buffer = new Buffer();
                buffer.writeAll(gzippedResponseBody);
            } catch (Throwable var34) {
                var24 = var34;
                throw var34;
            } finally {
                if (var24 != null) {
                    try {
                        gzippedResponseBody.close();
                    } catch (Throwable var33) {
                        var24.addSuppressed(var33);
                    }
                } else {
                    gzippedResponseBody.close();
                }

            }
        }

        Charset charset = Charset.forName("UTF-8");
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(Charset.forName("UTF-8"));
        }

        if (!isPlaintext(buffer)) {
            // Binary
            return "";
        }
        if (responseBody.contentLength() != 0L) {
            return buffer.clone().readString(charset);
        }

        return "";
    }

    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64L ? buffer.size() : 64L;
            buffer.copyTo(prefix, 0L, byteCount);

            for (int i = 0; i < 16 && !prefix.exhausted(); ++i) {
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }

            return true;
        } catch (EOFException var6) {
            return false;
        }
    }
}
