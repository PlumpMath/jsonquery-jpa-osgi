/*
 * Copyright 2011-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.markserrano.jsonquery.jpa.service;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.data.domain.mock.Page;
import org.springframework.data.domain.mock.PageImpl;
import org.springframework.data.domain.mock.Pageable;

import com.github.markserrano.jsonquery.jpa.builder.JsonBooleanBuilder;
import com.github.markserrano.jsonquery.jpa.filter.JsonFilter;
import com.github.markserrano.jsonquery.jpa.util.QueryUtil;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.path.PathBuilder;
import org.apache.felix.dm.annotation.api.Component;

/**
 *
 * @author Mark Anthony L. Serrano
 */
@Component(provides = IFilterService.class)
public class FilterService<T extends Serializable> implements IFilterService<T> {

    
    @Override
    public T findOne(EntityManager em,BooleanBuilder builder, Class<T> clazz) {
        String variable = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
        PathBuilder<T> entityPath = new PathBuilder<T>(clazz, variable);

        EntityPath<T> path = entityPath;

        JPQLQuery result = new JPAQuery(em).from(path).where(builder);

        return result.uniqueResult(entityPath);
    }

    @Override
    public T find(EntityManager em,BooleanBuilder builder, Class<T> clazz) {
        String variable = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
        PathBuilder<T> entityPath = new PathBuilder<T>(clazz, variable);

        EntityPath<T> path = entityPath;

        JPQLQuery result = new JPAQuery(em).from(path).where(builder);

        return result.uniqueResult(entityPath);
    }

    @Override
    public Page<T> readAndCount(EntityManager em,BooleanBuilder builder, Pageable page, Class<T> clazz, OrderSpecifier order) {
        Page<T> pageImpl = new PageImpl<T>(read(em,builder, page, clazz, order), page, count(em,builder, clazz, order));
        return pageImpl;
    }

    @Override
    public Page<T> readAndCount(EntityManager em,BooleanBuilder builder, Pageable page, Class<T> clazz, OrderSpecifier order, BooleanBuilder joinChildBuilder, String joinChildField, Class<?> joinChildClass) {
        Page<T> pageImpl = new PageImpl<T>(read(em,builder, page, clazz, order, joinChildBuilder, joinChildField, joinChildClass), page, count(em,builder, clazz, order, joinChildBuilder, joinChildField, joinChildClass));
        return pageImpl;
    }

    @Override
    public Page<T> readAndCount(EntityManager em,String filter, Pageable page, Class<T> clazz, OrderSpecifier order, String joinChildField, Class<?> joinChildClass, List<String> childFields) {
        Map<String, String> filters = QueryUtil.createParentAndChildFilter(filter, childFields);
        BooleanBuilder parentBuilder = new JsonBooleanBuilder(clazz).build(new JsonFilter(filters.get("parentFilter").toString()));
        BooleanBuilder joinChildBuilder = new JsonBooleanBuilder(joinChildClass).build(new JsonFilter(filters.get("childFilter").toString()));

        Page<T> pageImpl = new PageImpl<T>(read(em,parentBuilder, page, clazz, order, joinChildBuilder, joinChildField, joinChildClass),
                page, count(em,parentBuilder, clazz, order, joinChildBuilder, joinChildField, joinChildClass));
        return pageImpl;
    }

    @Override
    public List<T> read(EntityManager em,BooleanBuilder builder, Pageable page, Class<T> clazz, OrderSpecifier order) {
        String variable = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
        PathBuilder<T> entityPath = new PathBuilder<T>(clazz, variable);

        EntityPath<T> path = entityPath;

        JPQLQuery result = new JPAQuery(em).from(path).where(builder).orderBy(order);

        if (page != null) {
            result.offset(page.getOffset());
            result.limit(page.getPageSize());
        }

        return result.list(entityPath);
    }

    @Override
    public List<T> read(EntityManager em,BooleanBuilder builder, Pageable page, Class<T> clazz, OrderSpecifier order, BooleanBuilder joinChildBuilder, String joinChildField, Class<?> joinChildClass) {
        String variable = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
        PathBuilder<T> entityPath = new PathBuilder<T>(clazz, variable);

        EntityPath<T> path = entityPath;

        if (joinChildBuilder.getValue() != null) {
            String childVariable = joinChildClass.getSimpleName().substring(0, 1).toLowerCase() + joinChildClass.getSimpleName().substring(1);
            PathBuilder<T> joinPath = new PathBuilder<T>(clazz, variable);
            PathBuilder<Object> joinAlias = new PathBuilder<Object>(joinChildClass, childVariable);

            EntityPath<Object> jPath = joinPath.get(joinChildField);
            EntityPath<Object> jAlias = joinAlias;

            JPQLQuery result = new JPAQuery(em).from(path).join(jPath, jAlias).on(joinChildBuilder).where(builder).orderBy(order);

            if (page != null) {
                result.offset(page.getOffset());
                result.limit(page.getPageSize());
            }

            return result.list(entityPath);
        } else {
            return read(em,builder, page, clazz, order);
        }
    }

    @Override
    public Long count(EntityManager em,BooleanBuilder builder, Class<T> clazz, OrderSpecifier order) {
        String variable = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
        PathBuilder<T> entityPath = new PathBuilder<T>(clazz, variable);

        EntityPath<T> path = entityPath;

        JPQLQuery result = new JPAQuery(em).from(path).where(builder).orderBy(order);

        return result.count();
    }

    @Override
    public Long count(EntityManager em,BooleanBuilder builder, Class<T> clazz, OrderSpecifier order, BooleanBuilder joinChildBuilder, String joinChildField, Class<?> joinChildClass) {
        String variable = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
        PathBuilder<T> entityPath = new PathBuilder<T>(clazz, variable);

        EntityPath<T> path = entityPath;

        if (joinChildBuilder.getValue() != null) {
            String childVariable = joinChildClass.getSimpleName().substring(0, 1).toLowerCase() + joinChildClass.getSimpleName().substring(1);
            PathBuilder<T> joinPath = new PathBuilder<T>(clazz, variable);
            PathBuilder<Object> joinAlias = new PathBuilder<Object>(joinChildClass, childVariable);

            EntityPath<Object> jPath = joinPath.get(joinChildField);
            EntityPath<Object> jAlias = joinAlias;

            JPQLQuery result = new JPAQuery(em).from(path).join(jPath, jAlias).on(joinChildBuilder).where(builder).orderBy(order);

            return result.count();
        } else {
            return count(em,builder, clazz, order);
        }
    }

    @Override
    public Page<T> read(EntityManager em,String filter, Class<T> clazz, Pageable page, OrderSpecifier order) {
        return readAndCount(em,getJsonBooleanBuilder(clazz).build(new JsonFilter(filter)), page, clazz, order);
    }

    @Override
    public JsonBooleanBuilder getJsonBooleanBuilder(Class<T> clazz) {
        return new JsonBooleanBuilder(clazz);
    }

   

}
