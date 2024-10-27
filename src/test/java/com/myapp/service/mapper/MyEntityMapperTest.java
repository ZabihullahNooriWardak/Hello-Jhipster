package com.myapp.service.mapper;

import static com.myapp.domain.MyEntityAsserts.*;
import static com.myapp.domain.MyEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MyEntityMapperTest {

    private MyEntityMapper myEntityMapper;

    @BeforeEach
    void setUp() {
        myEntityMapper = new MyEntityMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMyEntitySample1();
        var actual = myEntityMapper.toEntity(myEntityMapper.toDto(expected));
        assertMyEntityAllPropertiesEquals(expected, actual);
    }
}
