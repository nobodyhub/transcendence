package com.nobodyhub.transcendence.repository.entity.mapper;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

/**
 * @author yan_h
 * @since 2018/6/15
 */
public class KeyMapperTest {
    @Test
    public void testTo() {
        assertEquals("string",
                KeyMapper.to("string"));
        assertEquals("DEC12343_400",
                KeyMapper.to(new BigDecimal("12343.400")));
        assertEquals("DATE20180619",
                KeyMapper.to(LocalDate.of(2018, 6, 19)));
    }

    @Test
    public void testFrom() {
        assertEquals("string",
                KeyMapper.from("string", String.class));
        assertEquals(new BigDecimal("12343.400"),
                KeyMapper.from("DEC12343_400", BigDecimal.class));
        assertEquals(LocalDate.of(2018, 6, 19),
                KeyMapper.from("DATE20180619", LocalDate.class));

    }
}