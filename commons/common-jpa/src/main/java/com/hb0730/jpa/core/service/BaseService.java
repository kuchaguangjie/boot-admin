package com.hb0730.jpa.core.service;

import com.hb0730.jpa.core.repository.BaseJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/2
 */
public class BaseService<M extends BaseJpaRepository<E, ID>, E, ID> implements IService<E, ID> {
    @Autowired
    protected M baseRepository;

    @Override
    public List<E> findAll() {
        return baseRepository.findAll();
    }

    @Override
    public List<E> findAll(Specification<E> specification) {
        return baseRepository.findAll(specification);
    }

    @Override
    public List<E> findAll(Sort sort) {
        return baseRepository.findAll(sort);
    }

    @Override
    public List<E> findAll(Specification<E> specification, Sort sort) {
        return baseRepository.findAll(specification, sort);
    }

    @Override
    public Page<E> findAll(Specification<E> specification, Pageable pageable) {
        return baseRepository.findAll(specification, pageable);
    }

    @Override
    public List<E> findAllByIds(Iterable<ID> ids) {
        return baseRepository.findAllById(ids);
    }

    @Override
    public E getById(ID id) {
        if (null == id) {
            return null;
        }
        return baseRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(E entity) {
        baseRepository.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(Iterable<E> entities) {
        baseRepository.saveAll(entities);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateById(E entity) {
        baseRepository.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBatch(Iterable<E> entities) {
        baseRepository.saveAll(entities);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(ID id) {
        baseRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(Iterable<ID> ids) {
        baseRepository.deleteAllByIdInBatch(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(E entity) {
        baseRepository.delete(entity);
    }
}
