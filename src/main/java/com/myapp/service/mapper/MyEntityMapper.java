package com.myapp.service.mapper;

import com.myapp.domain.MyEntity;
import com.myapp.service.dto.MyEntityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MyEntity} and its DTO {@link MyEntityDTO}.
 */
@Mapper(componentModel = "spring")
public interface MyEntityMapper extends EntityMapper<MyEntityDTO, MyEntity> {}
