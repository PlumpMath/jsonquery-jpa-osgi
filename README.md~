# JSONQuery JPA

JSONQuery JPA translates JSON queries to JPA queries. Using [QueryDSL] (https://github.com/mysema/querydsl) each 
translated query is type-safe and fluent . JSON query syntax is based on the popular [jqGrid] (http://www.trirand.com/blog/) 
plugin for jQuery. Paging implementation is simplified with [Spring Data JPA] (https://github.com/SpringSource/spring-data-jpa/)

Its main purpose is to provide easy wrapper for JavaScript plugins and widgets that query Java-based backend data layer.

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
    

## Sample Backend Usage

Create a backend controller and map the JSON query parameter as follows (this is example uses a Spring MVC and Spring Data JPA):

	public @ResponseBody ListWrapper<Person> records(@RequestParam("query") Boolean query) {	
		...
		Page<Person> records = service.read(query);
		ListWrapper<Person> response = new ListWrapper<Person>();
		response.setRows(records.getRows());
    	return response;
    	...
	}

Here the `query` argument maps to the `query` request parameter, and it contains the JSON query string. For a list of examples, please see [JSONQuery Samples] (https://github.com/markserrano/jsonquery-jpa-samples) project. There's a great chance that your favorite jQuery table are covered already.


## Working with Maven

You need to install Maven first. If you don't have it yet, please visit http://maven.apache.org/

To import the project in Eclipse

	$ mvn eclipse:eclipse -Dwtpversion=2.0

To install to your repository

    $ mvn install
	
To create a jar

	$ mvn package
	
To use the dependency add the following to your pom.xml

	<dependency>
		<groupId>com.github.markserrano</groupId>
		<artifactId>jsonquery-jpa</artifactId>
		<version>1.0.1.RELEASE</version>
		<type>jar</type>
		<scope>compile</scope>
	</dependency>
