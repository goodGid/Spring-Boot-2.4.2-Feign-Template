package dev.be.goodgid.controller;

import static dev.be.goodgid.common.consts.DemoConstant.CUSTOM_HEADER_NAME;
import static dev.be.goodgid.common.consts.DemoConstant.CUSTOM_HEADER_VALUE;
import static dev.be.goodgid.common.consts.DemoConstant.DEFAULT_AGE;
import static dev.be.goodgid.common.consts.DemoConstant.DEFAULT_NAME;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import dev.be.goodgid.common.dto.BaseRequestInfo;
import dev.be.goodgid.common.dto.BaseResponseInfo;
import dev.be.goodgid.feign.client.DemoFeignClient;
import dev.be.goodgid.service.DemoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DemoController {

    private final Environment env;

    private final DemoFeignClient client;
    private final DemoService demoService;

    @Value("${aaa.bbb.ccc:local}") // {aaa.bbb.ccc} property가 없으면 'local'로 default 값을 설정한다.
    private String aaaBbbCcc;

    @GetMapping("/test/feign")
    public ResponseEntity<BaseResponseInfo> testFeign() {
        ResponseEntity<BaseResponseInfo> response = null;

        BaseRequestInfo body = BaseRequestInfo.builder()
                                              .name(DEFAULT_NAME)
                                              .age(DEFAULT_AGE)
                                              .requestDate(LocalDateTime.now())
                                              .build();

        response = client.testGetMethod(CUSTOM_HEADER_VALUE);
//        response = client.testPostMethod(CUSTOM_HEADER_VALUE, body);
//        response = client.testErrorDecoder(CUSTOM_HEADER_VALUE);

        return response;
    }

    @GetMapping("/test/get")
    public ResponseEntity<BaseResponseInfo> testGetMethod(
            @RequestHeader(CUSTOM_HEADER_NAME) String customHeader) {
        validateHeader(customHeader);
        BaseResponseInfo responseInfo = demoService.testGetMethod();
        return ResponseEntity.ok(responseInfo);
    }

    @PostMapping("/test/post")
    public ResponseEntity<BaseResponseInfo> testPostMethod(
            @RequestHeader(CUSTOM_HEADER_NAME) String customHeader,
            @RequestBody BaseRequestInfo body) {
        validateHeader(customHeader);
        printBody(body);
        BaseResponseInfo responseInfo = demoService.testPostMethod(body);
        return ResponseEntity.ok(responseInfo);
    }

    @GetMapping("/test/errorDecoder")
    public ResponseEntity<BaseResponseInfo> testErrorDecoder(
            @RequestHeader(CUSTOM_HEADER_NAME) String customHeader) {
        validateHeader(customHeader);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    private void validateHeader(String customHeader) {
        Objects.requireNonNull(customHeader, "`customHeader` must not be null");
        Objects.equals(customHeader, CUSTOM_HEADER_VALUE);

        if (Objects.equals(aaaBbbCcc, env.getActiveProfiles()[0])) {
            log.info("aaaBbbCcc == {}", env.getActiveProfiles());
        } else {
            log.info("aaaBbbCcc != {}", env.getActiveProfiles());
        }
    }

    private void printBody(BaseRequestInfo body) {
        log.info(body.getName());
        log.info(String.valueOf(body.getAge()));
        log.info(body.getRequestDate().toString());
    }
}
