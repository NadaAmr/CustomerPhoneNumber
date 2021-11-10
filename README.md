# Customer Phones Categorization Service

This is a spring boot Application which contains a simple business logic , it validate international Phone Numbers and filter it.

#### Dependency & pre-requsite
- Java 11
- Spring 
- SQLITE3

#### How to start 
1- Run maven package cmd [ mvn clean pacakge]
2- docker build image [ docker build --tag=demo:latest .]
3- docker run [ docker run -p 8080:8080 demo:latest]

#### The API method
- GET /customer/phone - find all customer with filters
    Request Param :
    	country [optional] : string
    	state   [optional] : string , values [valid , Not_Valid]

#### Example

  GET
 
 - http://localhost:8080/api/customer/phone
 - http://localhost:8080/api/customer/phone?country=Morocco
 - http://localhost:8080/api/customer/phone?state=valid
 - http://localhost:8080/api/customer/phone?country=Morcco&state=valid
 - http://localhost:8080/api/customer/phone?country=Morcco&state=Not_valid
 
 



 
 



	
	

