package dev.be.goodgid.feign.interceptor;

import static java.nio.charset.StandardCharsets.UTF_8;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.be.goodgid.common.dto.BaseRequestInfo;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(staticName = "of")
public final class DemoFeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {

        if (template.body() == null) {
            return;
        }

        String oldMessage = StringUtils.toEncodedString(template.body(), UTF_8);
        log.info("[DemoFeignInterceptor] Old Message. {}", oldMessage);

        ObjectMapper objectMapper = new ObjectMapper();

        BaseRequestInfo oldInfo = null;
        String newMessage = null;

        try {
            oldInfo = objectMapper.readValue(oldMessage, BaseRequestInfo.class);
            BaseRequestInfo newInfo = BaseRequestInfo.builder()
                                                     .name("[DemoFeignInterceptor] " + oldInfo.getName())
                                                     .age(oldInfo.getAge())
                                                     .requestDate(oldInfo.getRequestDate())
                                                     .build();
            newMessage = objectMapper.writeValueAsString(newInfo);
        } catch (JsonProcessingException e) {
            log.warn("Error occurred while parsing objectMapper. ", e);
            newMessage = oldMessage;
        }
        log.info("[DemoFeignInterceptor] New Message. {}", newMessage);
        template.body(newMessage); // Change :: Old Body -> New Body
    }
}
