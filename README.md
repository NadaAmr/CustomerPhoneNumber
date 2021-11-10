# Customer Phones Categorization Service

This is a spring boot Application which contains a simple business logic , it validate international Phone Numbers and filter it.

#### Dependency & pre-requsite
- Java 11
- Spring 
- SQLITE3

#### The API method
- GET /customer/phone - find all customer with filters

#### Example

  GET
 
 - http://localhost:8080/api/customer/phone
 - http://localhost:8080/api/customer/phone?country=Morocco
 - http://localhost:8080/api/customer/phone?state=valid
 - http://localhost:8080/api/customer/phone?country=Morcco&state=valid
 
 



	
	

