package dev.be.goodgid.service;

import dev.be.goodgid.common.dto.BaseRequestInfo;
import dev.be.goodgid.common.dto.BaseResponseInfo;

public interface DemoService {

    BaseResponseInfo testGetMethod();

    BaseResponseInfo testPostMethod(BaseRequestInfo body);
}
