package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.MyEntity;
import com.mycompany.myapp.repository.MyEntityRepository;
import com.mycompany.myapp.service.MyEntityService;
import com.mycompany.myapp.service.dto.MyEntityDTO;
import com.mycompany.myapp.service.mapper.MyEntityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.MyEntity}.
 */
@Service
@Transactional
public class MyEntityServiceImpl implements MyEntityService {

    private static final Logger LOG = LoggerFactory.getLogger(MyEntityServiceImpl.class);

    private final MyEntityRepository myEntityRepository;

    private final MyEntityMapper myEntityMapper;

    public MyEntityServiceImpl(MyEntityRepository myEntityRepository, MyEntityMapper myEntityMapper) {
        this.myEntityRepository = myEntityRepository;
        this.myEntityMapper = myEntityMapper;
    }

    @Override
    public MyEntityDTO save(MyEntityDTO myEntityDTO) {
        LOG.debug("Request to save MyEntity : {}", myEntityDTO);
        MyEntity myEntity = myEntityMapper.toEntity(myEntityDTO);
        myEntity = myEntityRepository.save(myEntity);
        return myEntityMapper.toDto(myEntity);
    }

    @Override
    public MyEntityDTO update(MyEntityDTO myEntityDTO) {
        LOG.debug("Request to update MyEntity : {}", myEntityDTO);
        MyEntity myEntity = myEntityMapper.toEntity(myEntityDTO);
        myEntity = myEntityRepository.save(myEntity);
        return myEntityMapper.toDto(myEntity);
    }

    @Override
    public Optional<MyEntityDTO> partialUpdate(MyEntityDTO myEntityDTO) {
        LOG.debug("Request to partially update MyEntity : {}", myEntityDTO);

        return myEntityRepository
            .findById(myEntityDTO.getId())
            .map(existingMyEntity -> {
                myEntityMapper.partialUpdate(existingMyEntity, myEntityDTO);

                return existingMyEntity;
            })
            .map(myEntityRepository::save)
            .map(myEntityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MyEntityDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all MyEntities");
        return myEntityRepository.findAll(pageable).map(myEntityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MyEntityDTO> findOne(Long id) {
        LOG.debug("Request to get MyEntity : {}", id);
        return myEntityRepository.findById(id).map(myEntityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete MyEntity : {}", id);
        myEntityRepository.deleteById(id);
    }
}
