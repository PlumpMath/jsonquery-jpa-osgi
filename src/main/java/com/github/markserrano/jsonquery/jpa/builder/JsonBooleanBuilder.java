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
package com.github.markserrano.jsonquery.jpa.builder;

import org.apache.log4j.Logger;

import com.github.markserrano.jsonquery.jpa.builder.operator.BeginsWithBuilder;
import com.github.markserrano.jsonquery.jpa.builder.operator.ContainsBuilder;
import com.github.markserrano.jsonquery.jpa.builder.operator.EndsWithBuilder;
import com.github.markserrano.jsonquery.jpa.builder.operator.EqualBuilder;
import com.github.markserrano.jsonquery.jpa.builder.operator.GreaterEqualBuilder;
import com.github.markserrano.jsonquery.jpa.builder.operator.GreaterThanBuilder;
import com.github.markserrano.jsonquery.jpa.builder.operator.LessThanBuilder;
import com.github.markserrano.jsonquery.jpa.builder.operator.LesserEqualBuilder;
import com.github.markserrano.jsonquery.jpa.builder.operator.NotEqualBuilder;
import com.github.markserrano.jsonquery.jpa.filter.JsonFilter;
import com.github.markserrano.jsonquery.jpa.mapper.JsonObjectMapper;
import com.github.markserrano.jsonquery.jpa.util.ClassUtil;
import com.github.markserrano.jsonquery.jpa.util.QueryUtil;
import com.mysema.query.BooleanBuilder;

/**
 * Produces a {@link BooleanBuilder} artifact from a JSON query {@link String}
 * 
 * @param <T> the domain class
 * @param clazz the domain class
 * @param variable the literal name of the domain class
 * 
 * @author Mark Anthony L. Serrano
 */
public class JsonBooleanBuilder {
	
	protected static Logger logger = Logger.getLogger("jsonquery");
	
	private Class<?> clazz;
	private String variable;
	
	public JsonBooleanBuilder(Class<?> clazz) {
		super();
		this.clazz = clazz;
		this.variable = ClassUtil.getVariableName(clazz);
	}
	
	public BooleanBuilder build(JsonFilter jsonFilter) {
		if (jsonFilter.getSource() == null) {
			jsonFilter.setSource(QueryUtil.init());
			//throw new RuntimeException("Source filter is null!");
		}
		
		JsonFilter filter = JsonObjectMapper.map(jsonFilter.getSource());
		
		BooleanBuilder builder = new BooleanBuilder();
		for (JsonFilter.Rule rule: filter.getRules()) {
			builder = build(builder, rule);
		}
		return builder;
	}
	
	public BooleanBuilder build(BooleanBuilder builder, JsonFilter.Rule rule) {
		BooleanBuilder tempBuilder = EqualBuilder.get(clazz, variable, builder, rule);
		if (tempBuilder != null) {
			return tempBuilder;
		}
		
		tempBuilder = NotEqualBuilder.get(clazz, variable, builder, rule);
		if (tempBuilder != null) {
			return tempBuilder;
		}
		
		tempBuilder = LessThanBuilder.get(clazz, variable, builder, rule);
		if (tempBuilder != null) {
			return tempBuilder;
		}
		
		tempBuilder = GreaterThanBuilder.get(clazz, variable, builder, rule);
		if (tempBuilder != null) {
			return tempBuilder;
		}
		
		tempBuilder = LesserEqualBuilder.get(clazz, variable, builder, rule);
		if (tempBuilder != null) {
			return tempBuilder;
		}

		tempBuilder = GreaterEqualBuilder.get(clazz, variable, builder, rule);
		if (tempBuilder != null) {
			return tempBuilder;
		}
		
		tempBuilder = EndsWithBuilder.get(clazz, variable, builder, rule);
		if (tempBuilder != null) {
			return tempBuilder;
		}
		
		tempBuilder = BeginsWithBuilder.get(clazz, variable, builder, rule);
		if (tempBuilder != null) {
			return tempBuilder;
		}
		
		tempBuilder = ContainsBuilder.get(clazz, variable, builder, rule);
		if (tempBuilder != null) {
			return tempBuilder;
		}
		
		throw new RuntimeException("Unexpected operator [" + rule.getOp() + "] and field [" + rule.getField() +"]");
	}
}
