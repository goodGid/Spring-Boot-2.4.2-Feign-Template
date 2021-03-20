package dev.be.goodgid.common.dto;

import static dev.be.goodgid.common.consts.DemoConstant.PATTERN;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import dev.be.goodgid.common.dto.Deserializer.LocalDateTimeEpochTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class BaseRequestInfo {
    private String name;
    private Long age;

    // Serialize와 Deserialize 관련해서 좋은 블로그
    // https://perfectacle.github.io/2018/01/16/jackson-local-date-time-serialize/
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeEpochTimeDeserializer.class)
    @JsonFormat(pattern = PATTERN)
    private LocalDateTime requestDate;
}
