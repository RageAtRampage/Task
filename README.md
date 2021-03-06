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

# Database setup
Run the following queries on your database
```
CREATE DATABASE taskdb;
CREATE TABLE numbers(
	id int PRIMARY KEY,
	number int,
	version int
);
INSERT INTO `numbers`(`id`, `number`, `version`) VALUES (1,0,0);
```
# Note
The project is made using Java 11 and Eclipse. You need to change application.properties file present in "src/main/resources" to connect to the database.  Example application.properties
```
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/taskdb
spring.datasource.username=root
spring.datasource.password=
```
You need to change username, password according to your MySQL and "${MYSQL_HOST:localhost}:3306" from url according to your MySQL host url.

The increment value of number in the database using Optimistic locking, Pessimistic locking and Using combination of update query and @Transactional based on the "incrementNumbers" method defined in "TaskServiceImpl" class. It is set to use optimistic locking by default.

For Optimistic locking change "incrementNumbers" to 
------------------------------------------------------------------------
```
@Override
@Retryable(value = { LockTimeoutException.class, StaleObjectStateException.class, SQLTransientConnectionException.class }, maxAttempts = 10000)
public boolean incrementNumbers() {
	Optional<Numbers> num = taskRepo.findById((long) 1);
	if (num.isPresent()) {
		num.get().setNumber(num.get().getNumber() + 1);
		taskRepo.save(num.get());
	} else {
		Numbers n = new Numbers();
		n.setNumber((long) 1);
		taskRepo.save(n);
	}
	return true;
}
```
-------------------------------------------------------------------------

For Pessimistic locking change "incrementNumbers" to 
------------------------------------------------------------------------
```
@Override
@Transactional
@Retryable(value = { LockTimeoutException.class, StaleObjectStateException.class, SQLTransientConnectionException.class }, maxAttempts = 10000)
public boolean incrementNumbers() {
	Optional<Numbers> num = taskRepo.getNumbersUsingPessimisticLock((long) 1);
	if (num.isPresent()) {
		num.get().setNumber(num.get().getNumber() + 1);
		taskRepo.save(num.get());
	} else {
		Numbers n = new Numbers();
		n.setNumber((long) 1);
		taskRepo.save(n);
	}
	return true;
}
```
-------------------------------------------------------------------------

For Using combination of update query and @Transactional locking change "incrementNumbers" to 
------------------------------------------------------------------------
```
@Override
@Transactional
@Retryable(value = { LockTimeoutException.class, StaleObjectStateException.class, SQLTransientConnectionException.class }, maxAttempts = 10000)
public boolean incrementNumbers() {
	taskRepo.incrementNumbersInDB((long) 1);
	return true;
}
```
-------------------------------------------------------------------------

# Instructions
Follow the following guide to deploy after changing application.properties file.
https://www.edureka.co/blog/spring-boot-setup-helloworld-microservices-example/

There are 2 mehods described in the tutorial you can use any one of the methods.

The url to get number value: GET http://localhost:8080/task
The url to increment number value: POST http://localhost:8080/task
