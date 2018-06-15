package com.nobodyhub.transcendence.repository.mapper;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

/**
 * @author yan_h
 * @since 2018/6/15
 */
public class ValueMapperTest {
    @Test
    public void testTo() {
        assertEquals("string",
                ValueMapper.to("string"));
        assertEquals("12343.400",
                ValueMapper.to(new BigDecimal("12343.400")));
        assertEquals("20180619",
                ValueMapper.to(LocalDate.of(2018, 6, 19)));
    }

    @Test(expected = ValueMapperFailException.class)
    public void testTo_() {
        ValueMapper.to(new Object());
    }

    @Test
    public void testFrom() {
        assertEquals("string",
                ValueMapper.from("string", String.class));
        assertEquals(new BigDecimal("12343.400"),
                ValueMapper.from("12343.400", BigDecimal.class));
        assertEquals(LocalDate.of(2018, 6, 19),
                ValueMapper.from("20180619", LocalDate.class));

    }

    @Test(expected = ValueMapperFailException.class)
    public void testFrom_() {
        ValueMapper.from("20180619", Object.class);
    }
}