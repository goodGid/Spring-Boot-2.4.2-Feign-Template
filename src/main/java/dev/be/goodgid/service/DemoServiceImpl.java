package dev.be.goodgid.service;

import static dev.be.goodgid.common.consts.DemoConstant.DEFAULT_AGE;
import static dev.be.goodgid.common.consts.DemoConstant.DEFAULT_NAME;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import dev.be.goodgid.common.dto.BaseRequestInfo;
import dev.be.goodgid.common.dto.BaseResponseInfo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DemoServiceImpl implements DemoService {

    private static String PREFIX_CUSTOM_MESSAGE = "[Hello] ";

    @Override
    public BaseResponseInfo testGetMethod() {
        return BaseResponseInfo.builder()
                               .customName(PREFIX_CUSTOM_MESSAGE + DEFAULT_NAME)
                               .customAge(DEFAULT_AGE)
                               .responseDate(LocalDateTime.now())
                               .build();
    }

    @Override
    public BaseResponseInfo testPostMethod(BaseRequestInfo body) {
        return BaseResponseInfo.builder()
                               .customName(PREFIX_CUSTOM_MESSAGE + body.getName())
                               .customAge(10000L + body.getAge())
                               .responseDate(LocalDateTime.now())
                               .build();
    }
}
