package dev.be.goodgid.feign.logger;

import static feign.Util.UTF_8;
import static feign.Util.decodeOrDefault;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;

import dev.be.goodgid.feign.annotation.FeignSlowApiThreshold;
import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class FeignCustomLogger extends Logger {
    private static final String SUCCESS_LOG_MSG =
            "[FEIGN CLIENT] [REQUEST] (Method : %s) (Header : %s) (URI : %s) (Body : %s) [RESPONSE : \"OK\"] (Body : %s) [Elapsed : %dms%s]";
    private static final String ERROR_LOG_MSG =
            "[FEIGN CLIENT] [REQUEST] (Method : %s) (Header : %s) (URI : %s) (Body : %s) [RESPONSE : \"FAIL\"] (Body : %s) [Elapsed : %dms%s]";
    private static final int DEFAULT_SLOW_API_TIME = 5_000;
    private static final String SLOW_API_NOTICE = ", Slow Api";
    private static final String DASH = "-";
    private static final Map<String, Integer> slowApiThresholdMap = new ConcurrentHashMap<>();

    private final ApplicationContext applicationContext;

    @Override
    protected void log(String configKey, String format, Object... args) {
        log.info(String.format(methodTag(configKey) + format, args));
    }

    @Override
    protected void logRequest(String configKey, Logger.Level logLevel, Request request) {
        // Do nothing when request
        // request info was logged in `logAndRebufferResponse`
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Logger.Level logLevel,
                                              Response response, long elapsedTime) throws IOException {
        int slowApiThreshold = findThresholdByClientName(configKey);

        if (response.body() != null) {
            byte[] bodyData = Util.toByteArray(response.body().asInputStream());

            List<Object> arguments = new LinkedList<>();
            arguments.add(response.request().httpMethod());
            arguments.add(response.request().headers());
            arguments.add(response.request().url());
            arguments.add(getRequestBodyString(response.request()));
            arguments.add(getResponseBodyString(bodyData));
            arguments.add(elapsedTime);
            arguments.add(elapsedTime >= slowApiThreshold ? SLOW_API_NOTICE : StringUtils.EMPTY);

            if (HttpStatus.OK.value() == response.status()) {
                log(configKey, SUCCESS_LOG_MSG, arguments.toArray());
            } else {
                log(configKey, ERROR_LOG_MSG, arguments.toArray());
            }

            return response.toBuilder().body(bodyData).build();
        }
        return response;
    }

    private int findThresholdByClientName(String configKey) {
        String clientName = configKey.split("#")[0];

        if (!slowApiThresholdMap.containsKey(clientName)) {
            int threshold = Arrays.stream(applicationContext.getBeanDefinitionNames())
                                  .filter(s -> s.contains(clientName))
                                  .map(s -> applicationContext.findAnnotationOnBean(s, FeignSlowApiThreshold.class))
                                  .filter(s -> s != null)
                                  .map(s -> s.value())
                                  .findFirst().orElse(DEFAULT_SLOW_API_TIME);
            slowApiThresholdMap.put(clientName, threshold);
        }
        return slowApiThresholdMap.getOrDefault(clientName, DEFAULT_SLOW_API_TIME);
    }

    private String getRequestBodyString(Request request) {
        if (request.body() == null || !(request.body().length > 0)) {
            return DASH;
        }
        return decodeOrDefault(request.body(), UTF_8, DASH);
    }

    private String getResponseBodyString(byte[] bodyData) {
        return decodeOrDefault(bodyData, UTF_8, DASH);
    }

}
