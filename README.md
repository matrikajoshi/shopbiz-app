Spring-Boot and Angular e-commerce app

Technologies
Java 8
Angular 8
Spring Boot 2

Features:
Full stack CRUD operations on product, shopping cart, shopping list and creating order
Adding product to shopping cart without logging into application
User registration / authentication  and access control with Json Web Token, using Spring Security and authguard in Angular
Spring actuator to check /health endpoint

App uses h2 in memory database for testing and mysql database while app is running

To run the backend applcation with maven installed:

go to shopbiz directory - $ cd shopbiz

To set up mysql 
create database shopbiz in mysql 
add mysql login info to database properties in application.properties


Run as spring-boot application - mvn spring-boot:run

In browser navigate to http://localhost:8080/shopbiz/

HAL browser will list available REST API endpoints.

To run frontend with Angular CLI and NodeJS

go to $ cd shopbiz-UI
npm install
ng serve

Frontend will be running at http://localhost:4200

To login 
Customer role - email = customer@test.com, password = password
Admin role - email = admin@test.com, password = password

Admin role can add/update/delete products.






