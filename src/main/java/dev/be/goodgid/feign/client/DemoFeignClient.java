package dev.be.goodgid.feign.client;

import static dev.be.goodgid.common.consts.DemoConstant.CUSTOM_HEADER_NAME;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import dev.be.goodgid.common.dto.BaseRequestInfo;
import dev.be.goodgid.common.dto.BaseResponseInfo;
import dev.be.goodgid.feign.config.DemoFeignConfig;

@FeignClient(
        name = "demo-name",
        url = "${feign.api.demo.url}",
        configuration = DemoFeignConfig.class)
public interface DemoFeignClient {

    @PostMapping(value = "/post",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<BaseResponseInfo> testPostMethod(@RequestHeader(CUSTOM_HEADER_NAME) String customHeader,
                                                    @RequestBody BaseRequestInfo baseRequestInfo);

    @GetMapping(value = "/get",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<BaseResponseInfo> testGetMethod(@RequestHeader(CUSTOM_HEADER_NAME) String customHeader);

    @GetMapping(value = "/errorDecoder",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<BaseResponseInfo> testErrorDecoder(@RequestHeader(CUSTOM_HEADER_NAME) String customHeader);
}