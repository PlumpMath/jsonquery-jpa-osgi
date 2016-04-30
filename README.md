# JSONQuery JPA

JSONQuery JPA translates JSON queries to JPA queries. Using [QueryDSL] (https://github.com/mysema/querydsl) each 
translated query is type-safe and fluent . JSON query syntax is based on the popular [jqGrid] (http://www.trirand.com/blog/) 
plugin for jQuery. Paging implementation is simplified with [Spring Data JPA] (https://github.com/SpringSource/spring-data-jpa/)

Its main purpose is to provide easy wrapper for JavaScript plugins and widgets that query Java-based OSGI backend data layer.
It uses apache felix dependency manager

Here's a sample JSON query: 

    "{"groupOp":"AND","rules":" +
      "[{"field":"id","op":"eq","data":"1"}," +
    	"{"field":"name","op":"bw","data":"Jane Adams"}," +
    	"{"field":"age","op":"gt","data":"20"}," +
    	"{"field":"money","op":"lt","data":"2000.75"}," +
    	"{"field":"birthDate","op":"eq","data":"1959-09-30T00:00:00.000Z"}," +
    	"{"field":"parent.id","op":"eq","data":"2"}," +
    	"{"field":"creationDate","op":"eq","data":"01-31-1980 +5"}" +
    	"]}";
    	
Note: The framework is designed only for reading data. It doesn't create, update, or delete.


# Getting Started

Dowload or clone from Git and then use Maven:

    $ git clone ...
    $ mvn install

Then install on Apache Karaf



<feature name="jsonquery-jpa" description="JSONQuery JPA" version="1.0.0">
        <details>JSONQuery JPA translates JSON queries to JPA queries</details>
        <bundle>mvn:com.mysema.querydsl/querydsl-core/3.7.2</bundle>
        <bundle>mvn:com.google.guava/guava/18.0</bundle>
        <bundle>wrap:mvn:com.google.code.findbugs/jsr305/1.3.9</bundle>
        <bundle>mvn:com.mysema.commons/mysema-commons-lang/0.2.4</bundle>
        <bundle>wrap:mvn:com.infradna.tool/bridge-method-annotation/1.13</bundle>
        <bundle>mvn:com.mysema.querydsl/querydsl-jpa/3.7.2</bundle>

        <bundle>mvn:commons-codec/commons-codec/1.4</bundle>
        <bundle>mvn:commons-collections/commons-collections/3.2.1</bundle>
        <bundle>mvn:commons-io/commons-io/2.4</bundle>
        <bundle>mvn:commons-beanutils/commons-beanutils/1.8.3</bundle>
        <bundle>wrap:mvn:commons-logging/commons-logging/1.1.1</bundle>
        <bundle>mvn:joda-time/joda-time/1.6.2</bundle>
        <bundle>mvn:joda-time/joda-time-hibernate/1.3</bundle>
        <bundle>mvn:com.fasterxml.jackson.core/jackson-databind/2.6.3</bundle>
        <bundle>mvn:com.fasterxml.jackson.core/jackson-annotations/2.6.0</bundle>
        <bundle>mvn:com.fasterxml.jackson.core/jackson-core/2.6.3</bundle>
        
        <bundle start-level="90">mvn:com.sycliff.andromeda/jsonquery-jpa/1.0.0</bundle>
</feature>    
    

## Sample Backend Usage



import com.github.markserrano.jsonquery.jpa.response.JqgridResponse;


import com.google.common.collect.Lists;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.apache.felix.dm.annotation.api.Start;
import org.apache.felix.dm.annotation.api.Stop;
import com.github.markserrano.jsonquery.jpa.builder.JsonBooleanBuilder;
import com.github.markserrano.jsonquery.jpa.enumeration.OrderEnum;
import com.github.markserrano.jsonquery.jpa.filter.JsonFilter;
import com.github.markserrano.jsonquery.jpa.response.JqgridResponse;
import com.github.markserrano.jsonquery.jpa.specifier.Order;

import org.springframework.data.domain.mock.Page;
import org.springframework.data.domain.mock.PageRequest;
import org.springframework.data.domain.mock.Pageable;

import com.github.markserrano.jsonquery.jpa.service.IFilterService;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.OrderSpecifier;




@ServiceDependency(required = true)
private IFilterService<JPAAppUser> service;




public JqgridResponse searchAppUsers(String token, Boolean search, String filters, Integer page, Integer rows, String sidx, String sord)  {        
        Order order = new Order(JPAAppUser.class);
        // Prepare arguments before reading from service
        Pageable pageable = new PageRequest(page - 1, rows);
        
        OrderSpecifier<?> orderSpecifier = order.by(sidx, OrderEnum.getEnum(sord));
        BooleanBuilder booleanBuilder = service.getJsonBooleanBuilder(JPAAppUser.class).build(new JsonFilter(filters));

        // Add extra filters manually if necessary
        // Read from service
        Page<JPAAppUser> results = service.readAndCount(entityManager,booleanBuilder, pageable, JPAAppUser.class, orderSpecifier);

        // Map response
        JqgridResponse<AppUser> response = new JqgridResponse<AppUser>();
        response.setRows(results.getContent().stream().map(JPAAppUser::AppUserFromJPAAppUser).collect(Collectors.toList()));
        response.setRecords(Long.valueOf(results.getTotalElements()).toString());
        response.setTotal(Integer.valueOf(results.getTotalPages()).toString());
        response.setPage(Integer.valueOf(results.getNumber() + 1).toString());

        return response;
    }


for more examples 


Here the `query` argument maps to the `query` request parameter, and it contains the JSON query string. For a list of examples, please see [JSONQuery Samples] (https://github.com/markserrano/jsonquery-jpa-samples) project. There's a great chance that your favorite jQuery table are covered already. 




## Working with Maven

This project is not yet on the central Maven for now. 
Install in your local maven repo  and  reference like this 




<dependency>
        <groupId>com.sycliff.andromeda</groupId>
         <artifactId>jsonquery-jpa</artifactId>
         <version>1.0.0</version>
         <type>jar</type>
 </dependency>


