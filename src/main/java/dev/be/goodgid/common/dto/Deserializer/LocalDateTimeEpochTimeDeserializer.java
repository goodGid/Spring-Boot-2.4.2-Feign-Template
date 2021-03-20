package dev.be.goodgid.common.dto.Deserializer;

import java.io.IOException;
import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class LocalDateTimeEpochTimeDeserializer extends StdDeserializer<LocalDateTime> {

    protected LocalDateTimeEpochTimeDeserializer() {
        super(LocalDateTime.class);
    }

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        /*
        이렇게 생성하면 다음과 같이 LocalDateTime이 생성된다.
        ex)
        LocalDateTime.ofInstant(Instant.ofEpochMilli(1000L), TimeZone.getDefault().toZoneId());
        --> 1970-01-01T09:00:01

        LocalDateTime.ofInstant(Instant.ofEpochMilli(2000L), TimeZone.getDefault().toZoneId());
        --> 1970-01-01T09:00:02
         */
        return LocalDateTime.parse(p.getText());
    }
}
