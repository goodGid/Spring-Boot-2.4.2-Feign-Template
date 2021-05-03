package dev.be.goodgid.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class DemoControllerSpringTest {

    // MockMvc를 사용하기 위해선
    // @AutoConfigureMockMvc 선언이 필요하다.
    // https://goodgid.github.io/Spring-Test-SpringBootTest-Annotation/#webenvironmentmock--mockmvc
    @Autowired
    MockMvc mockMvc;

    // GoodgidApplication를 실행시킨 후 테스트를 시켜야하다.
    @Test
    public void test() throws Exception {

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/test/feign")
                                                                     .param("testNumber", "2"))
                                      .andExpect(status().isOk());

    }

}