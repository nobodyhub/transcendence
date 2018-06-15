package com.nobodyhub.transcendence.repository.mapper.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.nobodyhub.transcendence.common.Tconst;

import java.io.IOException;
import java.time.LocalDate;

/**
 * @author yan_h
 * @since 2018/6/15
 */
public class LocalDateDeserializer extends StdDeserializer<LocalDate> {
    public LocalDateDeserializer() {
        super(LocalDate.class);
    }

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        return LocalDate.parse(node.asText(), Tconst.dateFormatter);
    }
}
