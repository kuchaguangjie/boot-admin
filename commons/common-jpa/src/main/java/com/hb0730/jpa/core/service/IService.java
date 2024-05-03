package com.hb0730.jpa.core.service;

import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/2
 */
public interface IService<E, ID> {

    /**
     * 查询所有
     *
     * @return .
     */
    List<E> findAll();

    /**
     * 查询所有
     *
     * @param specification .
     * @return .
     */
    List<E> findAll(Specification<E> specification);

    /**
     * 查询所有
     *
     * @param sort .
     * @return .
     */
    List<E> findAll(Sort sort);

    /**
     * 查询所有
     *
     * @param specification .
     * @param sort          .
     * @return .
     */
    List<E> findAll(Specification<E> specification, Sort sort);

    /**
     * 查询所有
     *
     * @param specification .
     * @param pageable      .
     * @return .
     */
    Page<E> findAll(Specification<E> specification, Pageable pageable);

    /**
     * 查询所有
     *
     * @param pageable .
     * @return .
     */
    List<E> findAllByIds(Iterable<ID> ids);

    /**
     * 根据ID查询
     *
     * @param id .
     * @return .
     */
    @Nullable
    E getById(ID id);

    /**
     * 保存
     *
     * @param entity .
     */
    void save(E entity);

    /**
     * 批量保存
     *
     * @param entities .
     */
    void saveBatch(Iterable<E> entities);

    /**
     * 根据ID更新
     *
     * @param entity .
     */
    void updateById(E entity);

    /**
     * 批量更新
     *
     * @param entities .
     */
    void updateBatch(Iterable<E> entities);

    /**
     * 根据ID删除
     *
     * @param id .
     */
    void removeById(ID id);

    /**
     * 根据ID删除
     *
     * @param ids .
     */
    void removeByIds(Iterable<ID> ids);

    /**
     * 删除
     *
     * @param entity .
     */
    void remove(E entity);
}
