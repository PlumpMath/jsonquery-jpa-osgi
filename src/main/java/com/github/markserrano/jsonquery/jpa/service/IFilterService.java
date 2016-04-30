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

import org.springframework.data.domain.mock.Page;
import org.springframework.data.domain.mock.Pageable;

import com.github.markserrano.jsonquery.jpa.builder.JsonBooleanBuilder;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.OrderSpecifier;
import javax.persistence.EntityManager;

/**
 * 
 * @author Mark Anthony L. Serrano
 */
public interface IFilterService<T extends Serializable> {

	T find(EntityManager em,BooleanBuilder builder, Class<T> clazz);
	Page<T> readAndCount(EntityManager em,BooleanBuilder builder, Pageable page, Class<T> clazz, OrderSpecifier order); 
	List<T> read(EntityManager em,BooleanBuilder builder, Pageable page, Class<T> clazz, OrderSpecifier order);
	Long count(EntityManager em,BooleanBuilder builder, Class<T> clazz, OrderSpecifier order);
	Page<T> read(EntityManager em,String filter, Class<T> clazz, Pageable page, OrderSpecifier order);
	JsonBooleanBuilder getJsonBooleanBuilder(Class<T> clazz);
	
	List<T> read(EntityManager em,BooleanBuilder builder, Pageable page, Class<T> clazz, OrderSpecifier order, BooleanBuilder joinChildBuilder, String joinChildField, Class<?> joinChildClass);
	Long count(EntityManager em,BooleanBuilder builder, Class<T> clazz, OrderSpecifier order, BooleanBuilder joinChildBuilder, String joinChildField, Class<?> joinChildClass);
	Page<T> readAndCount(EntityManager em,BooleanBuilder builder, Pageable page, Class<T> clazz, OrderSpecifier order, BooleanBuilder joinChildBuilder, String joinChildField, Class<?> joinChildClass);
	
	T findOne(EntityManager em,BooleanBuilder builder, Class<T> clazz);
	Page<T> readAndCount(EntityManager em,String filter, Pageable page, Class<T> clazz,
			OrderSpecifier order, String joinChildField,
			Class<?> joinChildClass, List<String> childFields);
        
        

}