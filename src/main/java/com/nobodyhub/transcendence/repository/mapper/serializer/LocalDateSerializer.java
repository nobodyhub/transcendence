package com.nobodyhub.transcendence.repository.mapper.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.nobodyhub.transcendence.common.Tconst;

import java.io.IOException;
import java.time.LocalDate;

/**
 * @author yan_h
 * @since 2018/6/15
 */
public class LocalDateSerializer extends StdSerializer<LocalDate> {

    public LocalDateSerializer() {
        super(LocalDate.class);
    }

    @Override
    public void serialize(LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeRawValue(localDate.format(Tconst.dateFormatter));
    }
}
