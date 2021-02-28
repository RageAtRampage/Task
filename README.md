# Task 
The task is to consistently increment a number in a database when parallel threads  are racing to increment the number. 
Create a table Number in MySQL database with one integer type field. Create a RESTful API using Spring MVC architecture that increments this number. Use Jmeter (Do not use postman because it does not send parallel requests) to call  this API with 5000 users so that a lot of parallel requests are sent to server to  increment the number. 
Now set the initial value of Number to 0. 
After the execution of Jmeter, the value of the number in the database shall be 5000.  (Try the same with a bigger number 100000) 
The API should be scalable i.e. if deployed on multiple machines with same  database, the result should be consistent. 
Deliverables: 
Github link to the complete Java project 
Jmeter (.jmx) file. 
Documentation with Instructions 
Code shall be well documented using Javadoc standards with all Exception Handling  and validations.

# Note
The project is made using Java 11. You need to change application.properties file present in "src/main/resources" to connect to the database. The database with name taskdb and table with Numbers will be created automatically. Example application.properties

spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/taskdb
spring.datasource.username=root
spring.datasource.password=

You need to change username, password according to your MySQL and "${MYSQL_HOST:localhost}:3306" from url according to your MySQL host url.

# Instructions
Follow the following guide to deploy after changing application.properties file.
https://www.edureka.co/blog/spring-boot-setup-helloworld-microservices-example/

The url to get number value: GET http://localhost:8080/task
The url to increment number value: POST http://localhost:8080/task
