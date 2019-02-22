package com.flowable.oa.core.util.mybatis;

import com.flowable.oa.core.util.ReflectionUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.flowable.editor.language.json.converter.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 基础类
 *
 * @author lukew
 * @eamil 13507615840@163.com
 * @create 2018-09-06 18:54
 **/
public class BaseServiceImpl<T> implements IBaseService<T> {

    private Class<?> entityClass;

    private List<Field> fields;

    public BaseServiceImpl() {
        entityClass = ReflectionUtils.getClassGenricType(this.getClass());
        fields = ReflectionUtils.getFields(entityClass);
    }

    @Autowired
    protected Mapper<T> mapper;

    @Override
    public List<T> selectAll() {

        return this.mapper.selectAll();
    }

    @Override
    public T selectByKey(String key) {

        return mapper.selectByPrimaryKey(key);
    }

    @Override
    public T selectOne(T entity) {

        return mapper.selectOne(entity);
    }

    @Override
    public List<T> select(T entity) {

        return this.mapper.select(entity);
    }


    @Override
    @Transactional
    public int deleteById(String key) {
        return mapper.deleteByPrimaryKey(key);
    }

    @Override
    @Transactional
    public int deleteByModel(T t) {
        return mapper.delete(t);
    }

    @Override
    @Transactional
    public void deleteByIds(List<String> list) {

        if (CollectionUtils.isNotEmpty(list)) {
            for (String key : list) {
                this.mapper.deleteByPrimaryKey(key);
            }
        }
    }

    @Override
    @Transactional
    public int save(T entity) {
        return mapper.insert(entity);
    }

    @Override
    @Transactional
    public void saveOrUpdate(T entity) {

        String key = ReflectionUtils.getter(entity, "id") + "";
        if (StringUtils.isBlank(key)) {
            this.save(entity);
        } else {
            this.updateNotNull(entity);
        }
    }

    @Override
    @Transactional
    public int updateAll(T entity) {
        return mapper.updateByPrimaryKey(entity);
    }

    @Override
    @Transactional
    public int updateNotNull(T entity) {
        return mapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public List<T> selectByExample(Example example) {
        return mapper.selectByExample(example);
    }

    @Override
    public List<T> findByModel(T t, boolean isLike) {

        Example example = this.getExample(t, isLike);
        return this.selectByExample(example);
    }

    @Override
    public PageInfo<T> findByModel(PageInfo<T> page, T t, boolean isLike) {

        Example example = this.getExample(t, isLike);
        if (page != null) {
            PageHelper.startPage(page.getPageNum(), page.getPageSize());
            example.orderBy("id").desc();
        }
        return new PageInfo<>(this.selectByExample(example));
    }

    private Example getExample(T t, boolean isLike) {

        Example example = new Example(entityClass);
        Example.Criteria criteria = example.createCriteria();
        for (Field field : fields) {
            String property = field.getName();
            Class<?> type = field.getType();
            Object value = ReflectionUtils.getter(t, property);
            if (value != null) {
                if (type == String.class && isLike) {
                    criteria.andLike(property, "%" + value + "%");
                } else {
                    criteria.andEqualTo(property, value);
                }
            }
        }
        return example;
    }

    @Override
    public boolean check(Serializable uid, List<T> list) {

        if (CollectionUtils.isEmpty(list)) {
            return true;
        }
        int size = list.size();
        if (size > 1) {
            return false;
        }
        T t = list.get(0);
        String oid = ReflectionUtils.getter(t, "id") + "";
        return oid.equals(uid);
    }
}